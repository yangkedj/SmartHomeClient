package com.example.smarthomeclient;


import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.Dispositivos.Persiana;



public class ActividadPersiana extends ActividadDispositivo{
	private Persiana dispositivo;
	private TextView textoProgresion;
	private SeekBar barraNivel;
	private boolean actualizar;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_persiana);
		Bundle extras = getIntent().getExtras();
		dispositivo=(Persiana)SingletonDoha.getDispositivo(extras.getString("uuid"));
		textoNombre=(TextView)findViewById(R.id.textoNombre);
		textoDescripcion=(TextView)findViewById(R.id.textoDescripcion);
		textoProgresion=(TextView)findViewById(R.id.textoProgresion);
		textoHabitacion=(TextView)findViewById(R.id.textoHabitacion);
		imagen=(ImageView)findViewById(R.id.imageView1);
		barraNivel=(SeekBar)findViewById(R.id.barraNivel);
		barraNivel.setMax(100);
		new TareaIniciarActividad(this).execute(dispositivo);
		barraNivel.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       
		
		    @Override       
		    public void onStopTrackingTouch(SeekBar seekBar) {      
		        dispositivo.setNivel(barraNivel.getProgress());    
		        //Toast.makeText(getApplicationContext(), "envio",Toast.LENGTH_LONG).show();
		    }       

		    @Override       
		    public void onStartTrackingTouch(SeekBar seekBar) {     
		        // TODO Auto-generated method stub      
		    }       

		    @Override       
		    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {     
		        textoProgresion.setText(String.valueOf(progress)+"%");
		        asignarNivel(barraNivel.getProgress());
		        //Toast.makeText(getApplicationContext(), String.valueOf(progress),Toast.LENGTH_LONG).show();

		    }       
		});  	
		
	}
	
	private void asignarNivel(int nivel){
		Drawable dr;
		if(nivel>0){
			dr=this.getResources().getDrawable(R.drawable.bombilla_on);
		}
		else{
			dr=this.getResources().getDrawable(R.drawable.bombilla_off);
		}
		imagen.setImageDrawable(dr);
		barraNivel.setProgress(nivel);
		textoProgresion.setText(String.valueOf(nivel)+"%");
		imagen.setAlpha(50+2*nivel);
		imagen.invalidate();	
	}
	
	private static class TareaIniciarActividad extends AsyncTask<Persiana, String, String> {
		ActividadPersiana act;
		ProgressDialog mDialog;
		String textNombre;
		String textDescripcion;
		String textHabitacion;
		int nivel;
		
		TareaIniciarActividad(ActividadPersiana act){
			this.act=act;
		}
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        mDialog = new ProgressDialog(act);
	        mDialog.setMessage("Please wait...");
	        mDialog.show();
	    }
		protected String doInBackground(Persiana... params) {
			textHabitacion=params[0].getLocalizacionTemp();
			textDescripcion=params[0].getDescripcionTemp();
			textNombre=params[0].getNombreTemp();
			nivel=(int)params[0].getNivel();
			return null;
		}
		protected void onPostExecute(String res) {
			mDialog.dismiss();
			act.textoHabitacion.setText(textHabitacion);
			act.textoDescripcion.setText(textDescripcion);
			act.textoNombre.setText(textNombre);	
			act.asignarNivel(nivel);
		 }
	}
	
	/*public void onResume(){
		super.onResume();
		actualizar=true;
		new TareaRefrescoValor(this).execute();
	}
	public void onPause(){
		super.onPause();
		actualizar=false;
	}
	
	private static class TareaRefrescoValor extends AsyncTask<Void, Integer, String> {
		ActividadPersiana act;
		double nivel;
		TareaRefrescoValor(ActividadPersiana act){
			this.act=act;
		}
		protected String doInBackground(Void... params) {
			while(act.actualizar){
				nivel= act.dispositivo.getNivel();
				publishProgress((int)nivel);
				try {
					Thread.sleep(SingletonDoha.TIEMPO_REFRESCO_SENSORES);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}
		protected void onProgressUpdate(Integer... progress) {
			act.asignarNivel(progress[0]);
	      }
	}*/
	
}
