package com.example.smarthomeclient;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Dispositivos.Luz;
import com.example.Dispositivos.LuzNiveles;



public class ActividadLuz extends ActividadDispositivo{
	private Luz dispositivo;
	private boolean encendido=false;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_luz);
		Bundle extras = getIntent().getExtras();
		dispositivo=(Luz)SingletonDoha.getDispositivo(extras.getString("uuid"));
		textoNombre=(TextView)findViewById(R.id.textoNombre);
		textoDescripcion=(TextView)findViewById(R.id.textoDescripcion);
		textoHabitacion=(TextView)findViewById(R.id.textoHabitacion);
		imagen=(ImageView)findViewById(R.id.imageView1);
		
		new TareaIniciarActividad(this).execute(dispositivo);
		
	}
	private static class TareaIniciarActividad extends AsyncTask<Luz, String, String> {
		ActividadLuz act;
		ProgressDialog mDialog;
		String textNombre;
		String textDescripcion;
		String textHabitacion;
		boolean encendido;
		
		TareaIniciarActividad(ActividadLuz act){
			this.act=act;
		}
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        mDialog = new ProgressDialog(act);
	        mDialog.setMessage("Please wait...");
	        mDialog.show();
	    }
		protected String doInBackground(Luz... params) {
			textHabitacion=params[0].getLocalizacionTemp();
			encendido= params[0].isEncendido();
			textDescripcion=params[0].getDescripcionTemp();
			textNombre=params[0].getNombreTemp();
			return null;
		}
		protected void onPostExecute(String res) {
			mDialog.dismiss();
			act.textoHabitacion.setText(textHabitacion);
			act.encendido=encendido;
			act.textoDescripcion.setText(textDescripcion);
			act.textoNombre.setText(textNombre);	
			act.pintarBombilla();
		 }
	}
	
	/*
	 * Dibuja la bombilla encendida o apagada segun el valor de
	 * encendido
	 */
	private void pintarBombilla(){
		Drawable dr;
		if(encendido){
			dr=this.getResources().getDrawable(R.drawable.bombilla_on);
		}
		else{
			dr=this.getResources().getDrawable(R.drawable.bombilla_off);
		}
		imagen.setImageDrawable(dr);
		imagen.invalidate();
	}
	
	/*
	 * Metodo llamado al pulsar la imagen de la bombilla
	 */
	public void toggle(View view){
		encendido=!encendido;
		pintarBombilla();
		if(encendido){
			dispositivo.encender();
		}else{
			dispositivo.apagar();
		}
	}
	
	}
