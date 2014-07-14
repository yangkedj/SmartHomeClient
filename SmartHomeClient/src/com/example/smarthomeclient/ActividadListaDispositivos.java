package com.example.smarthomeclient;

import java.util.ArrayList;
import java.util.Iterator;

import SmartHomeNetwork.Constantes;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Dispositivos.SmartHomeDeviceAndroid;

public class ActividadListaDispositivos extends Activity implements Notificable {
	private GridView lista;
	private boolean actualizarLista=true;//variable que indica si la tarea asincrona de revisar nuevos dispositivos debe seguir activa
	private Notificable notificable=this;
	private int tipo=Constantes.TODOS;
	private String habitacion=null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actividad_lista_dispositivos);
		Bundle extras = getIntent().getExtras();
		tipo=extras.getInt("tipo");
		habitacion=extras.getString("habitacion");
		
		//Inicio el servicio DOHA
		SingletonDoha.iniciarDoha(this);
		SingletonDoha.actualizarListaDispositivos(this);
		
		lista = (GridView) findViewById(R.id.gridDispositivos);
	    
	    rellenarLista(filtrarLista(SingletonDoha.getListaDispositivos()));
	    lista.setOnItemClickListener(new OnItemClickListener() { 
	        @Override
	        public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
	            SmartHomeDeviceAndroid elegido = (SmartHomeDeviceAndroid) pariente.getItemAtPosition(posicion); 
	            switch(elegido.getTipo()){
	            case Constantes.LUZ:
	            	lanzarActividad(ActividadLuz.class,elegido.getIdentificador());
	            	break;
	            case Constantes.LUZ_DIMMER:
	            	lanzarActividad(ActividadLuzNiveles.class,elegido.getIdentificador());
	            	break;
	            case Constantes.ENCHUFE:
	            	lanzarActividad(ActividadEnchufe.class,elegido.getIdentificador());
	            	break;
	            case Constantes.SENSOR_LUMINOSIDAD:
	            	lanzarActividad(ActividadSensorLuminosidad.class,elegido.getIdentificador());
	            	break;
	            case Constantes.SENSOR_PRESENCIA:
	            	lanzarActividad(ActividadSensorPresencia.class,elegido.getIdentificador());
	            	break;
	            case Constantes.PUERTA:
	            	lanzarActividad(ActividadPuerta.class,elegido.getIdentificador());
	            	break;
	            case Constantes.ALARMA_SONORA:
	            	lanzarActividad(ActividadAlarma.class,elegido.getIdentificador());
	            	break;
	            case Constantes.PERSIANA:
	            	lanzarActividad(ActividadPersiana.class,elegido.getIdentificador());
	            	break;
	            case Constantes.VENTANA:
	            	lanzarActividad(ActividadVentana.class,elegido.getIdentificador());
	            	break;
	            default:
	            	CharSequence texto = "Seleccionado: " + elegido.getNombre();
	                Toast toast = Toast.makeText(ActividadListaDispositivos.this, texto, Toast.LENGTH_LONG);
	                toast.show();
	            	break;
	            }
	        }
	     });
	}
	
	public void onResume(){
		super.onResume();
		SingletonDoha.setNotificable(this);
	}
	
	public void onPause(){
		super.onPause();
		SingletonDoha.releaseNotificable();
	}
	
	/*
	 * Metodo estandar para lanzar actividaddes introduciendo como 
	 * parametro la clase
	 */
	private void lanzarActividad(Class actividad_a_lanzar,String uuid){
        Intent intent=new Intent(this,actividad_a_lanzar);
        intent.putExtra("uuid", uuid);
		this.startActivity(intent);
	}
	
	/*
	 * Metodo llamado cuando se actualiza la lista de dispositivos. Este metodo se encarga
	 * de repintar la lista
	 */
	@Override
	public void notificarCambioLista() {
		rellenarLista(filtrarLista(SingletonDoha.getListaDispositivos()));	
	}
	
	/**
	 * Filtra una lista de dispositivos segun su tipo
	 * @param listaEntrada lista de dispositivos inicial
	 * @param tipo tipo de dispositivo del cual queremos obtener una lista
	 * @return lista del tipo de dispositivo deseado
	 */
	private ArrayList<SmartHomeDeviceAndroid> filtrarLista(ArrayList<SmartHomeDeviceAndroid> listaEntrada){
		ArrayList<SmartHomeDeviceAndroid> listaSalida=new ArrayList<>();
		if(habitacion==null){
			if(tipo==Constantes.TODOS ){
				return listaEntrada;
			}
			//filtro solo tipo
			else{
				Iterator<SmartHomeDeviceAndroid> it=listaEntrada.iterator();
				while(it.hasNext()){
					SmartHomeDeviceAndroid temp=it.next();
					if(temp.getTipo()==tipo){
						listaSalida.add(temp);
					}
				}
			}
		}
		//filtro habitacion y tipo
		else{
			Iterator<SmartHomeDeviceAndroid> it=listaEntrada.iterator();
			while(it.hasNext()){
				SmartHomeDeviceAndroid temp=it.next();
				if(temp.getTipo()==tipo || tipo==Constantes.TODOS){
					if(temp.getLocalizacionTemp().equals(habitacion)){
						listaSalida.add(temp);
					}
				}
			}
		}
		return listaSalida;	
	}
	
	/*
	 * Metodo encargado de pintar la lista de dispositivos
	 */
	private void rellenarLista(ArrayList<SmartHomeDeviceAndroid> listaEntrada){
		lista.setAdapter(new AdaptadorListaDispositivos(this, R.layout.elemento_grid, listaEntrada){
			@Override
			public void onEntrada(Object dispositivo, View view) {
					SmartHomeDeviceAndroid disp=(SmartHomeDeviceAndroid)dispositivo;
					TextView textoNombre=(TextView)view.findViewById(R.id.textNombre);
		            ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imageView_imagen); 
		            
		            switch(disp.getTipo()){
		            case Constantes.LUZ:
		            	imagen_entrada.setImageResource(R.drawable.luz);
		            	break;
		            case Constantes.LUZ_DIMMER:
		            	imagen_entrada.setImageResource(R.drawable.luz);
		            	break;
		            case Constantes.ENCHUFE:
		            	imagen_entrada.setImageResource(R.drawable.enchufe);
		            	break;
		            case Constantes.SENSOR_LUMINOSIDAD:
		            	imagen_entrada.setImageResource(R.drawable.sensor_lum);
		            	break;
		            case Constantes.SENSOR_PRESENCIA:
		            	imagen_entrada.setImageResource(R.drawable.sensorpresencia);
		            	break;
		            case Constantes.PUERTA:
		            	imagen_entrada.setImageResource(R.drawable.puerta);
		            	break;
		            case Constantes.ALARMA_SONORA:
		            	imagen_entrada.setImageResource(R.drawable.alarma);
		            	break;
		            case Constantes.PERSIANA:
		            	imagen_entrada.setImageResource(R.drawable.persiana);
		            	break;
		            case Constantes.VENTANA:
		            	imagen_entrada.setImageResource(R.drawable.ventana);
		            	break;
		            }
		            
		            textoNombre.setText(disp.getNombreTemp());  
			}
		});
	}
	
}
