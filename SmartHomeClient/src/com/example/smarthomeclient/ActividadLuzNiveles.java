package com.example.smarthomeclient;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.Dispositivos.LuzNiveles;



public class ActividadLuzNiveles extends ActividadDispositivo{
	private LuzNiveles dispositivo;
	private TextView textoProgresion;
	private SeekBar barraNivel;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_luz_niveles);
		Bundle extras = getIntent().getExtras();
		dispositivo=(LuzNiveles)SingletonDoha.getDispositivo(extras.getString("uuid"));
		textoNombre=(TextView)findViewById(R.id.textoNombre);
		textoDescripcion=(TextView)findViewById(R.id.textoDescripcion);
		textoProgresion=(TextView)findViewById(R.id.textoProgresion);
		textoHabitacion=(TextView)findViewById(R.id.textoHabitacion);
		imagen=(ImageView)findViewById(R.id.imageView1);
		barraNivel=(SeekBar)findViewById(R.id.barraNivel);
		barraNivel.setMax(100);
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
		new TareaIniciarActividad(this).execute(dispositivo);
		//encendido=dispositivo.isEncendido();
		textoHabitacion.setText(dispositivo.getLocalizacionTemp());
		asignarNivel((int) dispositivo.getNivel());
		textoDescripcion.setText(dispositivo.getDescripcionTemp());
		textoNombre.setText(dispositivo.getNombreTemp());		
		
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
	
	private static class TareaIniciarActividad extends AsyncTask<LuzNiveles, String, String> {
		ActividadLuzNiveles act;
		ProgressDialog mDialog;
		String textNombre;
		String textDescripcion;
		String textHabitacion;
		int nivel;
		
		TareaIniciarActividad(ActividadLuzNiveles act){
			this.act=act;
		}
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        mDialog = new ProgressDialog(act);
	        mDialog.setMessage("Please wait...");
	        mDialog.show();
	    }
		protected String doInBackground(LuzNiveles... params) {
			textHabitacion=params[0].getLocalizacionTemp();
			nivel=((int) params[0].getNivel());
			textDescripcion=params[0].getDescripcionTemp();
			textNombre=params[0].getNombreTemp();
			return null;
		}
		protected void onPostExecute(String res) {
			mDialog.dismiss();
			act.textoHabitacion.setText(textHabitacion);
			act.asignarNivel((int) nivel);
			act.textoDescripcion.setText(textDescripcion);
			act.textoNombre.setText(textNombre);	
	
		 }
	}

	
}
