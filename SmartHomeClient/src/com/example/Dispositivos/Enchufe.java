package com.example.Dispositivos;

import SmartHomeNetwork.Constantes;
import SmartHomeNetwork.DatosInvocacionServicio;
import SmartHomeNetwork.OperacionesSmartHomeDevice;

public class Enchufe extends SmartHomeDeviceAndroid implements SmartHomeNetwork.Interfaces.Dispositivos.Int_Enchufe {
	
	public Enchufe(DatosInvocacionServicio datosInvocacion) {
		super(Constantes.ENCHUFE,datosInvocacion);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean encender() {
		OperacionesSmartHomeDevice.Encender(datosInvocacion);
		return false;
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
