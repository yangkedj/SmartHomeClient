package com.example.smarthomeclient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.ws4d.java.io.fs.FileSystem;
import org.ws4d.java.platform.io.fs.LocalFileSystem;

import SmartHomeNetwork.Cliente;
import SmartHomeNetwork.Constantes;
import SmartHomeNetwork.DatosInvocacionServicio;
import SmartHomeNetwork.SmartHome;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.AsyncTask;
import android.provider.Settings.Secure;

import com.example.Dispositivos.SmartHomeDeviceAndroid;

import doha.Service;

public final class SingletonDoha extends Cliente {
	private static boolean encendido=false;
	private static LinkedHashMap<String,SmartHomeDeviceAndroid> dispositivosMap;
	private static LinkedHashMap<String,String> habitaciones;
	private static Notificable notificable;
	protected static int TIEMPO_ENTRE_ACTUALIZACIONES=10000;
	protected static int TIEMPO_REFRESCO_SENSORES=1000;
	
	/*
	 * Devuelve la lista de dispositivos descubiertos en la red DOHA
	 */
	public static ArrayList<SmartHomeDeviceAndroid> getListaDispositivos(){
		ArrayList<SmartHomeDeviceAndroid> dispositivosArray=new ArrayList<SmartHomeDeviceAndroid>();
		dispositivosArray.addAll(dispositivosMap.values());
		System.out.println("El map tiene "+dispositivosMap.size()+" el array "+dispositivosArray.size());
		return dispositivosArray;
	}
	
	/**
	 * Este metodo es llamado cuando despues de buscar dispositivos en una red con DS este recibe un cambio
	 * y notifica la nueva lista de dispositivos
	 */
	public void recibidoReporteLista(ArrayList<DatosInvocacionServicio> listaRecibida){
		generarListaDispositivos(listaRecibida);
		if(notificable!=null){
			notificable.notificarCambioLista();
		}
	}
	
	public static boolean isEncendido(){
		return encendido;
	}

	/**
	 * Devuelve el dispositivo con el identificador dado
	 * @param id identificador
	 * @return dispositivo en caso de encontrarse, null en caso contrario
	 */
	public static SmartHomeDeviceAndroid getDispositivo(String id){
		return dispositivosMap.get(id);
	}
	
	/**
	 * Devuelve un arrayList con las habitaciones
	 * @return
	 */
	public static ArrayList<String> getListaHabitaciones(){
		return new ArrayList<String>(habitaciones.values());
	}
	
	/**
	 * Añade una habitacion a la lista de habitaciones
	 * @param habitacion
	 */
	public static void anniadirHabitacion(String habitacion){
		if(habitacion!=null && !habitaciones.containsKey(habitacion)){
			habitaciones.put(habitacion, habitacion);
		}
	}
	/**
	 * Borra una habitacion de la lista de habitaciones
	 * @param habitacion
	 */
	public static void borrarHabitacion(String habitacion){
		if(habitacion!=null && habitaciones.containsKey(habitacion)){
			habitaciones.remove(habitacion);
		}
	}
	
	/**
	 * Inicializa toda la parte de SmartHome
	 * @param act actividad principal
	 */
	public static void iniciarDoha(Context act){
		if(!encendido){
			dispositivosMap=new LinkedHashMap<String, SmartHomeDeviceAndroid>();
			habitaciones=new LinkedHashMap<String,String>();
			
			//indico que cliente es un singletonDoha lo uso para poder hacer los callbacks
			cliente=new SingletonDoha();
			
			//preparacion necesaria para DOHA			
			try {
				LocalFileSystem lfs = (LocalFileSystem) FileSystem.getInstance();
				lfs.setAndroidContext(act.getApplicationContext());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			WifiManager wifiManager =(WifiManager) act.getSystemService(Context.WIFI_SERVICE);
			if (wifiManager != null){
				MulticastLock mcLock = wifiManager.createMulticastLock("DPWS_Multicast_lock");
				mcLock.acquire();
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				}
			/*
			 * Inicio el servicio de SmartHome en modo cliente Android
			 */
			//obtengo un identificador unico de android
			String id = Secure.getString(act.getContentResolver(), Secure.ANDROID_ID);
			File f=new File(File.separator+"mnt"+File.separator+"sdcard"+File.separator+"doha"+File.separator+"CompositionMap.xml");
			System.out.println(f.exists());
			encendido=SmartHome.iniciarComoClienteAndroid("cliente"+id,File.separator+"mnt"+File.separator+"sdcard"+File.separator+"doha"+File.separator+"CompositionMap.xml",0);
			try{
				new TareaActualizadora().execute(TIEMPO_ENTRE_ACTUALIZACIONES);
			}catch(Exception e){}
		}	
	}
	/**
	 * Apaga correctamente el servicio SmartHome
	 */
	public static void shutdown(){
		apagar();
		encendido=false;
		try {
			Thread.sleep(TIEMPO_ENTRE_ACTUALIZACIONES);
		} catch (InterruptedException e) {e.printStackTrace();}
		releaseNotificable();
	}
	
	
	/**
	 * Rellena la lista de dispositivos a partir de un arrayList con los servicios de la red
	 * @param listaServicios lista de servicios de la cual se desean extraer los dispositivos
	 */
	private static void generarListaDispositivos(ArrayList<DatosInvocacionServicio> listaServicios){
		if(listaServicios!=null){
			LinkedHashMap<String,SmartHomeDeviceAndroid> tempListaLocal=dispositivosMap;
			//creo una lista nueva y voy metiendo los viejos que coincidan con la nueva
			dispositivosMap=new LinkedHashMap<String, SmartHomeDeviceAndroid>();
			
			Iterator<DatosInvocacionServicio> it=listaServicios.iterator();
			while(it.hasNext()){
				DatosInvocacionServicio temp=it.next();
				//compruebo que no existe el dispositivo ya en la lista
				if(!tempListaLocal.containsKey(temp.getIdDispositivo())){
					SmartHomeDeviceAndroid dispositivo=SmartHomeDeviceAndroid.crearDispositivo(temp);
					if(dispositivo!=null){
						dispositivosMap.put(dispositivo.getIdentificador(), dispositivo);
						new DescargarInformacionDispositivo().execute(dispositivo);
					}
				}
				else{
					//cojo la copia local en caso de existir
					dispositivosMap.put(temp.getIdDispositivo(), tempListaLocal.get(temp.getIdDispositivo()));
				}
			}
		}
	}
	
	/*
	 * Metodo llamado cuando se quiere iniciar la tarea asincrona de añadir nuevos dispostivos
	 */
	public static void actualizarListaDispositivos(Notificable noti){
		if(noti!=null){
			notificable=noti;
		}
		try{
			new ActualizacionServiciosActivos().execute(Service.getService());
		}catch(Exception e){}
	}
	
	public static void setNotificable(Notificable noti){
		notificable=noti;
	}
	
	public static void releaseNotificable(){
		notificable=null;
	}
	
	// Ejecución de operación en tarea asíncrona que a partir de los servicios crea una lista de dispositivos asociados // 
	public static class ActualizacionServiciosActivos extends AsyncTask<doha.Service, String, String> {
		protected String doInBackground(doha.Service... serv) {
			generarListaDispositivos(Cliente.buscarServiciosRed());
			return "";
		}		 		
		 protected void onPostExecute(String res) {
			 if(notificable!=null){
				 notificable.notificarCambioLista();
			 }
		 }
	}
	
	// Tarea asincrona que descarga los datos del dispositivo en caso de no haberse descargado antes
	private static class DescargarInformacionDispositivo extends AsyncTask<SmartHomeDeviceAndroid, String, String> {
		protected String doInBackground(SmartHomeDeviceAndroid... device) {
			if(device[0]!=null){
				device[0].getNombreTemp();
				device[0].getLocalizacionTemp();
				device[0].getDescripcionTemp();
			}
			return "";
		}		 		
		protected void onPostExecute(String res) {}
	}
	
	
	private static class TareaActualizadora extends AsyncTask<Integer, String, String> {
		protected String doInBackground(Integer... arg0) {
			while(encendido){
				SingletonDoha.actualizarListaDispositivos(notificable);
				try {
					Thread.sleep(arg0[0]);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
			return null;
		}
		 protected void onPostExecute(String res) { }
	}

}
