package com.example.smarthomeclient;

import java.util.ArrayList;
import java.util.Iterator;

import SmartHomeNetwork.Accion;
import SmartHomeNetwork.Constantes;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.Dispositivos.SmartHomeDeviceAndroid;
import com.example.Dispositivos.Ventana;



public class ActividadVentana extends ActividadDispositivo{
	private Ventana dispositivo;
	private boolean abierta=false;
	private ListView lista1,lista2;
	private SmartHomeDeviceAndroid dispositivoAccionTemp;
	private String operacionAccionTemp;
	private Accion accionTemp;
	private boolean actualizar;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_ventana);
		Bundle extras = getIntent().getExtras();
		dispositivo=(Ventana)SingletonDoha.getDispositivo(extras.getString("uuid"));
		textoNombre=(TextView)findViewById(R.id.textoNombre);
		textoDescripcion=(TextView)findViewById(R.id.textoDescripcion);
		textoHabitacion=(TextView)findViewById(R.id.textoHabitacion);
		imagen=(ImageView)findViewById(R.id.imageView1);
		lista1= (ListView)findViewById(R.id.listView1);
		lista2=(ListView)findViewById(R.id.listView2);
		
		new TareaIniciarActividad(this).execute(dispositivo);
		
		Resources res = getResources();
		TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
		tabs.setup();
		 
		TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
		spec.setContent(R.id.tab1);
		spec.setIndicator("General",null);
		tabs.addTab(spec);
		 
		spec=tabs.newTabSpec("mitab2");
		spec.setContent(R.id.tab2);
		spec.setIndicator("Evento OnAlert",null);
		tabs.addTab(spec);
		
		spec=tabs.newTabSpec("mitab3");
		spec.setContent(R.id.tab3);
		
		spec.setIndicator("Evento OnAlertStop",null);
		tabs.addTab(spec); 
		tabs.setCurrentTab(0);
	
		lista1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int posicion, long id) {
		        dispositivo.elimitarAccionOnAlert(dispositivo.getAccionesOnAlertTemp().get(posicion).getId());
		        AdaptadorListaDispositivos adaptador=(AdaptadorListaDispositivos) lista1.getAdapter();
		        adaptador.notifyDataSetChanged();
				return true;
		    }
		});

		lista2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int posicion, long id) {
		        dispositivo.elimitarAccionOnAlertStop(dispositivo.getAccionesOnAlertStopTemp().get(posicion).getId());
		        AdaptadorListaDispositivos adaptador=(AdaptadorListaDispositivos) lista2.getAdapter();
		        adaptador.notifyDataSetChanged();
				return true;
		    }
		});
	}
	
	private static class TareaIniciarActividad extends AsyncTask<Ventana, String, String> {
		ActividadVentana act;
		ProgressDialog mDialog;
		String textNombre;
		String textDescripcion;
		String textHabitacion;
		boolean abierta;
		ArrayList<Accion> l1;
		ArrayList<Accion> l2;
		
		TareaIniciarActividad(ActividadVentana act){
			this.act=act;
		}
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        mDialog = new ProgressDialog(act);
	        mDialog.setMessage("Please wait...");
	        mDialog.show();
	    }
		protected String doInBackground(Ventana... params) {
			textHabitacion=params[0].getLocalizacionTemp();
			abierta= params[0].isAbierta();
			textDescripcion=params[0].getDescripcionTemp();
			textNombre=params[0].getNombreTemp();
			l1=params[0].getAccionesOnAlertTemp();
			l2=params[0].getAccionesOnAlertStopTemp();
			return null;
		}
		protected void onPostExecute(String res) {
			mDialog.dismiss();
			act.textoHabitacion.setText(textHabitacion);
			act.abierta=abierta;
			act.textoDescripcion.setText(textDescripcion);
			act.textoNombre.setText(textNombre);	
			act.actualizar(abierta);
			act.rellenarLista1(l1);
			act.rellenarLista2(l2);
		 }
	}
	
	public void actualizar(boolean abierta){
		this.abierta=abierta;
		pintarImagen();
	}
	
	/**
	 * Pinta la lista de onAlert
	 * @param listaEntrada
	 */
	private void rellenarLista1(ArrayList<Accion> listaEntrada){
		lista1.setAdapter(new AdaptadorListaDispositivos(this, R.layout.elemento_eventlist, listaEntrada){
			@Override
			public void onEntrada(Object accion, View view) {
				Accion disp=(Accion)accion;
				TextView textoNombre=(TextView)view.findViewById(R.id.textNombre);
				TextView textoOperacion=(TextView)view.findViewById(R.id.textOperacion);
				textoNombre.setText(SingletonDoha.getDispositivo(disp.getDis().getIdDispositivo()).getNombre());
				textoOperacion.setText(disp.getNombreOperacion());
			}
		});
	}
	/**
	 * Pinta la lista de onAlertStop
	 * @param listaEntrada
	 */
	private void rellenarLista2(ArrayList<Accion> listaEntrada){
		lista2.setAdapter(new AdaptadorListaDispositivos(this, R.layout.elemento_eventlist, listaEntrada){
			@Override
			public void onEntrada(Object accion, View view) {
				Accion disp=(Accion)accion;
				TextView textoNombre=(TextView)view.findViewById(R.id.textNombre);
				TextView textoOperacion=(TextView)view.findViewById(R.id.textOperacion);
				textoNombre.setText(SingletonDoha.getDispositivo(disp.getDis().getIdDispositivo()).getNombre());
				textoOperacion.setText(disp.getNombreOperacion());
			}
		});
	}
		
	/**
	 * Dibuja la bombilla encendida o apagada segun el valor de
	 * encendido
	 */
	private void pintarImagen(){
		Drawable dr;
		if(abierta){
			dr=this.getResources().getDrawable(R.drawable.puerta_abierta);
		}
		else{
			dr=this.getResources().getDrawable(R.drawable.puerta_cerrada);
		}
		imagen.setImageDrawable(dr);
		imagen.invalidate();
	}
	
	public void onResume(){
		super.onResume();
		actualizar=true;
		new TareaRefrescoValor(this).execute();
	}
	public void onPause(){
		super.onPause();
		actualizar=false;
	}
	
	private static class TareaRefrescoValor extends AsyncTask<Void, Void, String> {
		ActividadVentana act;
		boolean abierta;
		TareaRefrescoValor(ActividadVentana act){
			this.act=act;
		}
		protected String doInBackground(Void... params) {
			while(act.actualizar){
				abierta= act.dispositivo.isAbierta();
				publishProgress();
				try {
					Thread.sleep(SingletonDoha.TIEMPO_REFRESCO_SENSORES);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}
		protected void onProgressUpdate(Void... progress) {
			act.abierta=abierta;
			act.pintarImagen();
	    }
	}
	
	public void botonAniadirAlert(View view){
		final ArrayList<SmartHomeDeviceAndroid> temp=SingletonDoha.getListaDispositivos();
		final String[] items= new String[temp.size()] ;
		Iterator<SmartHomeDeviceAndroid> it=temp.iterator();
		for(int i=0;it.hasNext();i++){
			SmartHomeDeviceAndroid disp=it.next();
			items[i]=disp.getNombre();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona el dispositivo sobre el que se efectuara la accion");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	dispositivoAccionTemp=temp.get(item);
            	elegirOperacionAlert();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
	}
	
	private void elegirOperacionAlert(){
		final String[] items;
		switch(dispositivoAccionTemp.getTipo()){
		case Constantes.LUZ_DIMMER:
			items=new String[2];
			items[0]=Constantes.OP_ENCENDER;
			items[1]=Constantes.OP_APAGAR;
			break;
		case Constantes.LUZ:
			items=new String[2];
			items[0]=Constantes.OP_ENCENDER;
			items[1]=Constantes.OP_APAGAR;
			break;
		case Constantes.ALARMA_SONORA:
			items=new String[2];
			items[0]=Constantes.OP_ENCENDER;
			items[1]=Constantes.OP_APAGAR;
			break;
		case Constantes.ENCHUFE:
			items=new String[2];
			items[0]=Constantes.OP_ENCENDER;
			items[1]=Constantes.OP_APAGAR;
			break;
		default:items=null;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona el dispositivo sobre el que se efectuara la accion");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	operacionAccionTemp=items[item];
            	accionTemp=new Accion(dispositivoAccionTemp.getDatosInvocacion(), operacionAccionTemp, null);
            	registrarEvento(true);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
	}
	
	public void botonAniadirAlertStop(View view){
		final ArrayList<SmartHomeDeviceAndroid> temp=SingletonDoha.getListaDispositivos();
		final String[] items= new String[temp.size()] ;
		Iterator<SmartHomeDeviceAndroid> it=temp.iterator();
		for(int i=0;it.hasNext();i++){
			SmartHomeDeviceAndroid disp=it.next();
			items[i]=disp.getNombre();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona el dispositivo sobre el que se efectuara la accion");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	dispositivoAccionTemp=temp.get(item);
            	elegirOperacionAlertStop();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
	}
	
	private void elegirOperacionAlertStop(){
		final String[] items;
		switch(dispositivoAccionTemp.getTipo()){
		case Constantes.LUZ_DIMMER:
			items=new String[2];
			items[0]=Constantes.OP_ENCENDER;
			items[1]=Constantes.OP_APAGAR;
			break;
		case Constantes.LUZ:
			items=new String[2];
			items[0]=Constantes.OP_ENCENDER;
			items[1]=Constantes.OP_APAGAR;
			break;
		case Constantes.ALARMA_SONORA:
			items=new String[2];
			items[0]=Constantes.OP_ENCENDER;
			items[1]=Constantes.OP_APAGAR;
			break;
		case Constantes.ENCHUFE:
			items=new String[2];
			items[0]=Constantes.OP_ENCENDER;
			items[1]=Constantes.OP_APAGAR;
			break;
		default:items=null;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona el dispositivo sobre el que se efectuara la accion");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	operacionAccionTemp=items[item];
            	accionTemp=new Accion(dispositivoAccionTemp.getDatosInvocacion(), operacionAccionTemp, null);
            	registrarEvento(false);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
	}
	
	public void registrarEvento(boolean onAlert){
		if(onAlert){
			dispositivo.registrarAccionOnAlert(accionTemp);
		}
		else{
			dispositivo.registrarAccionOnAlertStop(accionTemp);
		}
        AdaptadorListaDispositivos adaptador=(AdaptadorListaDispositivos) lista1.getAdapter();
        adaptador.notifyDataSetChanged();
        AdaptadorListaDispositivos adaptador2=(AdaptadorListaDispositivos) lista2.getAdapter();
        adaptador2.notifyDataSetChanged();
	}
}
