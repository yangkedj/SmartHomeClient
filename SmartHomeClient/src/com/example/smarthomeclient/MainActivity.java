package com.example.smarthomeclient;

import java.util.ArrayList;
import java.util.Iterator;

import SmartHomeNetwork.Constantes;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//SingletonDoha.iniciarDoha(this.getApplicationContext());
		/*if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
		new TareaIniciarDoha(this).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void botonTodos(View view){
		lanzarActividad(null,Constantes.TODOS);
	}
	
	public void botonLuces(View view){
		lanzarActividad(null,Constantes.LUZ_DIMMER);
	}
	
	public void botonHabitaciones(View view){
		//final String[] items= (String[]) SingletonDoha.getListaHabitaciones().toArray();
		ArrayList<String> temp=SingletonDoha.getListaHabitaciones();
		final String[] items= new String[temp.size()] ;
		Iterator<String> it=temp.iterator();
		for(int i=0;it.hasNext();i++){
			items[i]=it.next();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
		
        builder.setTitle("Selecciona la habitacion");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	lanzarActividad(items[item],Constantes.TODOS);
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
            	botonHabitaciones(null);
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
    	    	botonHabitaciones(null);
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
	
	public void botonSensor(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Introduza la habitacion para listar los dispositivos");
    	// Set up the input
    	final EditText input = new EditText(this);
    	// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
    	input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
    	builder.setView(input);

    	// Set up the buttons
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
    	    @Override
    	    public void onClick(DialogInterface dialog, int which) {
    	    	lanzarActividad(input.getText().toString(),Constantes.TODOS);
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
	
	/**
	 * Metodo para invocar una lista con los dispositivos, dando la posibilidad de filtrar
	 * tanto por tipo como por habitacion
	 * @param filtroHabitacion habitacion cuyos dispositivos quiero mostrar
	 * @param filtroTipo tipo dispositivo del cual quiero ver la lista
	 */
	private void lanzarActividad(String filtroHabitacion,int filtroTipo){
        Intent intent=new Intent(this,ActividadListaDispositivos.class);
        intent.putExtra("tipo", filtroTipo);
        intent.putExtra("habitacion", filtroHabitacion);
		this.startActivity(intent);
	}
	
	private static class TareaIniciarDoha extends AsyncTask<Void, String, String> {
		Context context;
		ProgressDialog mDialog;
		
		TareaIniciarDoha(Context context){
			this.context=context;
		}
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        mDialog = new ProgressDialog(context);
	        mDialog.setMessage("Conectando con la SmartHomeNetwork...");
	        mDialog.show();
	    }
		protected String doInBackground(Void... params) {
			SingletonDoha.iniciarDoha(context);
			return null;
		}
		 protected void onPostExecute(String res) {
			 mDialog.dismiss();
			 if(!SingletonDoha.isEncendido()){
				 AlertDialog.Builder builder = new AlertDialog.Builder(context);
			     builder.setTitle("Fallo la conexion con Doha");
			     builder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() { 
			    	    @Override
			    	    public void onClick(DialogInterface dialog, int which) {
			    	    	
			    	    }
			    	});
			    	builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
			    	    @Override
			    	    public void onClick(DialogInterface dialog, int which) {
			    	        dialog.cancel();
			    	        System.exit(0);
			    	    }
			    	});
			     builder.show();
			     
			 }
		 }
	}
}
