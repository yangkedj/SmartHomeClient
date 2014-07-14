package com.example.Dispositivos;

import SmartHomeNetwork.Constantes;
import SmartHomeNetwork.DatosInvocacionServicio;
import SmartHomeNetwork.OperacionesSmartHomeDevice;
import SmartHomeNetwork.Interfaces.Dispositivos.Int_Luz_Dimmer;


public class LuzNiveles extends SmartHomeDeviceAndroid implements Int_Luz_Dimmer {

	public LuzNiveles(DatosInvocacionServicio datosInvocacion) {
		super(Constantes.LUZ_DIMMER, datosInvocacion);
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

	@Override
	public boolean setNivel(int nivel) {
		return OperacionesSmartHomeDevice.setNivel(datosInvocacion, nivel);
	}

	@Override
	public double getNivel() {
		return OperacionesSmartHomeDevice.getNivel(datosInvocacion);
	}

}
