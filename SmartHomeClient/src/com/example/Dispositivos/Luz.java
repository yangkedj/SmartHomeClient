package com.example.Dispositivos;

import SmartHomeNetwork.Constantes;
import SmartHomeNetwork.DatosInvocacionServicio;
import SmartHomeNetwork.OperacionesSmartHomeDevice;
import SmartHomeNetwork.Interfaces.Dispositivos.Int_Luz;

public class Luz extends SmartHomeDeviceAndroid implements Int_Luz{

	public Luz(DatosInvocacionServicio datosInvocacion) {
		super(Constantes.LUZ, datosInvocacion);
	}

	@Override
	public boolean encender() {
		return OperacionesSmartHomeDevice.Encender(datosInvocacion);
	}

	@Override
	public boolean apagar() {
		return OperacionesSmartHomeDevice.Apagar(datosInvocacion);
	}

	@Override
	public boolean isEncendido() {
		return OperacionesSmartHomeDevice.isEncendido(datosInvocacion);
	}

}
