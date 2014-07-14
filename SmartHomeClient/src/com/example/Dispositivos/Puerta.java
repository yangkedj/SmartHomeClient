package com.example.Dispositivos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import SmartHomeNetwork.Accion;
import SmartHomeNetwork.Constantes;
import SmartHomeNetwork.DatosInvocacionServicio;
import SmartHomeNetwork.OperacionesSmartHomeDevice;

public class Puerta extends SmartHomeDeviceAndroid{
	private ArrayList<Accion> onAlertActions;
	private ArrayList<Accion> onAlertStopActions;
	
	public Puerta(DatosInvocacionServicio datosInvocacion) {
		super(Constantes.PUERTA, datosInvocacion);
	}
	
	public boolean isAbierta(){
		return OperacionesSmartHomeDevice.isAbierto(datosInvocacion);
	}
	
	public void registrarAccionOnAlert(Accion accion) {
		if(OperacionesSmartHomeDevice.registrarEventAction(datosInvocacion, accion, "onAlert")){
			onAlertActions.add(accion);
		}
	}

	public void registrarAccionOnAlertStop(Accion accion) {
		if(OperacionesSmartHomeDevice.registrarEventAction(datosInvocacion, accion, "onAlertStop")){
			onAlertStopActions.add(accion);
		}
	}

	public void elimitarAccionOnAlert(String identificadorAccion) {
		if(OperacionesSmartHomeDevice.borrarEventAction(datosInvocacion, identificadorAccion, "onAlert")){
			/*Iterator<Accion> it=onAlertActions.iterator();
			for(int i=0;it.hasNext();i++){
				if(onAlertActions.get(i).getId().equals(identificadorAccion)){
					onAlertActions.remove(i);
				}
			}*/
			getAccionesOnAlert();
		}
	}

	public void elimitarAccionOnAlertStop(String identificadorAccion) {
		if(OperacionesSmartHomeDevice.borrarEventAction(datosInvocacion, identificadorAccion, "onAlertStop")){
			/*Iterator<Accion> it=onAlertStopActions.iterator();
			for(int i=0;it.hasNext();i++){
				if(onAlertStopActions.get(i).getId().equals(identificadorAccion)){
					onAlertStopActions.remove(i);
				}
			}*/
			getAccionesOnAlertStop();
		}
	}

	public ArrayList<Accion> getAccionesOnAlert() {
		onAlertActions=OperacionesSmartHomeDevice.getListaEventAction(datosInvocacion, "onAlert");
		return onAlertActions;
	}
	
	public ArrayList<Accion> getAccionesOnAlertTemp() {
		if(onAlertActions==null){
			return getAccionesOnAlert();
		}
		return onAlertActions;
	}
	
	public ArrayList<Accion> getAccionesOnAlertStop() {
		onAlertStopActions=OperacionesSmartHomeDevice.getListaEventAction(datosInvocacion, "onAlertStop");
		return onAlertStopActions;
	}
	
	public ArrayList<Accion> getAccionesOnAlertStopTemp() {
		if(onAlertStopActions==null){
			return getAccionesOnAlertStop();
		}
		return onAlertStopActions;
	}
}
