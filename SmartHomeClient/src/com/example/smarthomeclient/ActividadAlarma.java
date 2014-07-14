package com.example.smarthomeclient;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Dispositivos.Alarma;



public class ActividadAlarma extends ActividadDispositivo{
	private Alarma dispositivo;
	private boolean encendido=false;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_alarma);
		Bundle extras = getIntent().getExtras();
		dispositivo=(Alarma)SingletonDoha.getDispositivo(extras.getString("uuid"));
		textoNombre=(TextView)findViewById(R.id.textoNombre);
		textoDescripcion=(TextView)findViewById(R.id.textoDescripcion);
		textoHabitacion=(TextView)findViewById(R.id.textoHabitacion);
		imagen=(ImageView)findViewById(R.id.imageView1);
		
		encendido=dispositivo.isEncendido();
		textoNombre.setText(dispositivo.getNombre());
		textoDescripcion.setText(dispositivo.getDescripcionTemp());
		
		textoHabitacion.setText(dispositivo.getLocalizacionTemp());
		pintarBombilla();
		
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
