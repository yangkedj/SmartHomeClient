package com.example.smarthomeclient;

import java.util.ArrayList;
import java.util.Iterator;

import SmartHomeNetwork.Constantes;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Dispositivos.Enchufe;
import com.example.Dispositivos.Luz;



public class ActividadEnchufe extends ActividadDispositivo{
	private Enchufe dispositivo;
	private boolean encendido=false;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_enchufe);
		
		Bundle extras = getIntent().getExtras();
		dispositivo=(Enchufe)SingletonDoha.getDispositivo(extras.getString("uuid"));
		
		textoNombre=(TextView)findViewById(R.id.textoNombre);
		textoDescripcion=(TextView)findViewById(R.id.textoDescripcion);
		textoHabitacion=(TextView)findViewById(R.id.textoHabitacion);
		imagen=(ImageView)findViewById(R.id.imageView1);
		
		new TareaIniciarActividad(this).execute(dispositivo);
	}
	
	private static class TareaIniciarActividad extends AsyncTask<Enchufe, String, String> {
		ActividadEnchufe act;
		ProgressDialog mDialog;
		String textNombre;
		String textDescripcion;
		String textHabitacion;
		boolean encendido;
		
		TareaIniciarActividad(ActividadEnchufe act){
			this.act=act;
		}
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        mDialog = new ProgressDialog(act);
	        mDialog.setMessage("Please wait...");
	        mDialog.show();
	    }
		protected String doInBackground(Enchufe... params) {
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
			act.pintarImagen();
		 }
	}
	
	/*
	 * Dibuja la bombilla encendida o apagada segun el valor de
	 * encendido
	 */
	private void pintarImagen(){
		Drawable dr;
		if(encendido){
			dr=this.getResources().getDrawable(R.drawable.enchufe_on);
		}
		else{
			dr=this.getResources().getDrawable(R.drawable.enchufe_off);
		}
		imagen.setImageDrawable(dr);
		imagen.invalidate();
	}
	
	/*
	 * Metodo llamado al pulsar la imagen de la bombilla
	 */
	public void toggle(View view){
		encendido=!encendido;
		pintarImagen();
		if(encendido){
			dispositivo.encender();
		}else{
			dispositivo.apagar();
		}
	}
}
