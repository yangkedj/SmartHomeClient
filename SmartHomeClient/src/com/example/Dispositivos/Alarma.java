package com.example.Dispositivos;

import SmartHomeNetwork.Constantes;
import SmartHomeNetwork.DatosInvocacionServicio;
import SmartHomeNetwork.OperacionesSmartHomeDevice;
import SmartHomeNetwork.Interfaces.Dispositivos.Int_Alarma;

public class Alarma extends SmartHomeDeviceAndroid implements Int_Alarma{

	public Alarma(DatosInvocacionServicio datosInvocacion) {
		super(Constantes.ALARMA_SONORA, datosInvocacion);
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
