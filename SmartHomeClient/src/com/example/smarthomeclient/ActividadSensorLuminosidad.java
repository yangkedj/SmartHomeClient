package com.example.smarthomeclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Dispositivos.SensorLuminosidad;



public class ActividadSensorLuminosidad extends ActividadDispositivo{
	private SensorLuminosidad dispositivo;
	private TextView textoNombre,textoHabitacion,textoValorSensor;
	private TextView textoDescripcion;
	private ImageView imagen;
	private double valorSensor;
	private boolean actualizar;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_sensorluminosidad);
		Bundle extras = getIntent().getExtras();
		dispositivo=(SensorLuminosidad)SingletonDoha.getDispositivo(extras.getString("uuid"));
		textoNombre=(TextView)findViewById(R.id.textoNombre);
		textoDescripcion=(TextView)findViewById(R.id.textoDescripcion);
		textoHabitacion=(TextView)findViewById(R.id.textoHabitacion);
		imagen=(ImageView)findViewById(R.id.imageView1);
		textoValorSensor=(TextView)findViewById(R.id.textValorSensor);

		
		new TareaIniciarActividad(this).execute(dispositivo);
	}
	
	private static class TareaIniciarActividad extends AsyncTask<SensorLuminosidad, String, String> {
		ActividadSensorLuminosidad act;
		ProgressDialog mDialog;
		String textNombre;
		String textDescripcion;
		String textHabitacion;
		double vs;
		
		TareaIniciarActividad(ActividadSensorLuminosidad act){
			this.act=act;
		}
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        mDialog = new ProgressDialog(act);
	        mDialog.setMessage("Please wait...");
	        mDialog.show();
	    }
		protected String doInBackground(SensorLuminosidad... params) {
			textHabitacion=params[0].getLocalizacionTemp();
			vs= params[0].leerSensor();
			textDescripcion=params[0].getDescripcionTemp();
			textNombre=params[0].getNombreTemp();
			return null;
		}
		protected void onPostExecute(String res) {
			mDialog.dismiss();
			act.textoHabitacion.setText(textHabitacion);
			act.valorSensor=vs;
			act.textoDescripcion.setText(textDescripcion);
			act.textoNombre.setText(textNombre);	
			act.textoValorSensor.setText(String.valueOf(vs));
		 }
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
	
	private static class TareaRefrescoValor extends AsyncTask<Void, Double, String> {
		ActividadSensorLuminosidad act;
		double vs;
		TareaRefrescoValor(ActividadSensorLuminosidad act){
			this.act=act;
		}
		protected String doInBackground(Void... params) {
			while(act.actualizar){
				vs= act.dispositivo.leerSensor();
				publishProgress(vs);
				try {
					Thread.sleep(SingletonDoha.TIEMPO_REFRESCO_SENSORES);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}
		protected void onProgressUpdate(Double... progress) {
			act.textoValorSensor.setText(String.valueOf(progress[0]));
	      }
	}
}
