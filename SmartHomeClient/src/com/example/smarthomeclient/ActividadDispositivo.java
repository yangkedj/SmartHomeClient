package com.example.smarthomeclient;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Dispositivos.SmartHomeDeviceAndroid;

public abstract class ActividadDispositivo extends Activity{
	private SmartHomeDeviceAndroid dispositivo;
	protected TextView textoNombre,textoHabitacion;
	protected TextView textoDescripcion;
	protected ImageView imagen;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		dispositivo=SingletonDoha.getDispositivo(extras.getString("uuid"));
	}
	
	public void cambiarHabitacion(View view){
		ArrayList<String> temp=SingletonDoha.getListaHabitaciones();
		final String[] items= new String[temp.size()] ;
		Iterator<String> it=temp.iterator();
		for(int i=0;it.hasNext();i++){
			items[i]=it.next();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona la habitacion");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	dispositivo.setLocalizacion(items[item]);
    	        textoHabitacion.setText(dispositivo.getLocalizacionTemp());
            }
        });
		builder.setPositiveButton("Añadir", new DialogInterface.OnClickListener() { 
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	    	anniadirHabitacion();
    	    }
    	});
        builder.setNegativeButton("Borrar", new DialogInterface.OnClickListener() { 
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	    	borrarHabitacion();
    	    }
    	});
        AlertDialog alert = builder.create();
        alert.show();
	}
	
	private void borrarHabitacion(){
		ArrayList<String> temp=SingletonDoha.getListaHabitaciones();
		final String[] items= new String[temp.size()] ;
		Iterator<String> it=temp.iterator();
		for(int i=0;it.hasNext();i++){
			items[i]=it.next();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona la habitacion");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	SingletonDoha.borrarHabitacion(items[item]);
            	cambiarHabitacion(null);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
	}
	
	private void anniadirHabitacion(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Introduza el nombre de la habitacion a añadir");
    	// Set up the input
    	final EditText input = new EditText(this);
    	// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
    	input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
    	builder.setView(input);

    	// Set up the buttons
    	builder.setPositiveButton("Añadir", new DialogInterface.OnClickListener() { 
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	    	SingletonDoha.anniadirHabitacion(input.getText().toString());
    	    	cambiarHabitacion(null);
    	    }
    	});
    	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	        dialog.cancel();
    	    }
    	});
    	builder.show();
	}
	
	public void cambiarNombre(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Introduza el nuevo nombre para el dispositivo");
    	// Set up the input
    	final EditText input = new EditText(this);
    	// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
    	input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
    	builder.setView(input);

    	// Set up the buttons
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	    	dispositivo.setNombre(input.getText().toString());
    	        textoNombre.setText(dispositivo.getNombreTemp());
    	    }
    	});
    	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	        dialog.cancel();
    	    }
    	});

    	builder.show();	
	}
	
	public void cambiarDescripcion(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Introduza la nueva descripcion para el dispositivo");
    	// Set up the input
    	final EditText input = new EditText(this);
    	input.setText(dispositivo.getDescripcionTemp());
    	// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
    	input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
    	builder.setView(input);

    	// Set up the buttons
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	    	dispositivo.setdescripcion(input.getText().toString());
    	        textoDescripcion.setText(dispositivo.getDescripcionTemp());
    	    }
    	});
    	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	        dialog.cancel();
    	    }
    	});

    	builder.show();	
	}
}
