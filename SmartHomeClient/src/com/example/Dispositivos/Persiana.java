package com.example.Dispositivos;

import java.io.IOException;

import SmartHomeNetwork.Constantes;
import SmartHomeNetwork.DatosInvocacionServicio;
import SmartHomeNetwork.OperacionesSmartHomeDevice;
import SmartHomeNetwork.Interfaces.Dispositivos.Int_Persiana;


public class Persiana extends SmartHomeDeviceAndroid implements Int_Persiana {

	public Persiana(DatosInvocacionServicio datosInvocacion) {
		super(Constantes.PERSIANA, datosInvocacion);
	}

	@Override
	public boolean setNivel(int nivel) {
		return OperacionesSmartHomeDevice.setNivel(datosInvocacion, nivel);
	}

	@Override
	public double getNivel() {
		return OperacionesSmartHomeDevice.getNivel(datosInvocacion);
	}

	@Override
	public boolean abrir() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean cerrar() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAbierto() throws IOException {
		return OperacionesSmartHomeDevice.isAbierto(datosInvocacion);
	}

}
