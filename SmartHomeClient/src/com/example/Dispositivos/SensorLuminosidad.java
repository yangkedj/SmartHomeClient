package com.example.Dispositivos;

import SmartHomeNetwork.Constantes;
import SmartHomeNetwork.DatosInvocacionServicio;
import SmartHomeNetwork.OperacionesSmartHomeDevice;
import SmartHomeNetwork.Interfaces.Dispositivos.Int_Sensor_Luminosidad;

public class SensorLuminosidad extends SmartHomeDeviceAndroid implements Int_Sensor_Luminosidad {
	
	public SensorLuminosidad(DatosInvocacionServicio datosInvocacion) {
		super(Constantes.SENSOR_LUMINOSIDAD,datosInvocacion);
		
	}

	@Override
	public double getSensibilidad() {
		return 0;
	}

	@Override
	public double leerSensor() {
		return OperacionesSmartHomeDevice.leerSensor(datosInvocacion);
	}

}
