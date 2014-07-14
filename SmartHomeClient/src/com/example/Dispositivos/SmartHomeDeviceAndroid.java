package com.example.Dispositivos;

import SmartHomeNetwork.Constantes;
import SmartHomeNetwork.DatosInvocacionServicio;
import SmartHomeNetwork.OperacionesSmartHomeDevice;

import com.example.smarthomeclient.SingletonDoha;

public class SmartHomeDeviceAndroid implements SmartHomeNetwork.Interfaces.Dispositivos.Int_HomeDevice{

	private String nombre;
    private int tipo;
    private String direccion;
    private String descripcion;
    private String localizacion;
    protected DatosInvocacionServicio datosInvocacion;

    public SmartHomeDeviceAndroid(int tipo,DatosInvocacionServicio datosInvocacion) {
		super();
		this.tipo = tipo;
		this.direccion=datosInvocacion.getIdDispositivo();
		this.datosInvocacion=datosInvocacion;
		nombre=null;
		descripcion=null;
		localizacion=null;
	}
    
    /**
     * Solicita el nombre del dispositivo a la SmartHomeNetwork y lo devuelve
     * @return nombre
     */
    public String getNombre() {
    		nombre=OperacionesSmartHomeDevice.getNombre(datosInvocacion);
    		return nombre;
    }
    /**
     * Devuelve el nombre almacenado localmente, en caso de no existir lo solicita a la red
     * @return nombre del dispositivo almacenado en el movil
     */
    public String getNombreTemp(){
    	if(nombre==null){
    		return getNombre();
    	}
    	return nombre;
    }
    

    public int getTipo() {
        return tipo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        OperacionesSmartHomeDevice.setNombre(datosInvocacion, nombre);
    }
    
	public String getIdentificador() {
		return direccion;
	}

	@Override
	public String getDescripcion() {
		descripcion=OperacionesSmartHomeDevice.getDescripcion(datosInvocacion);
		return descripcion;
	}
	
	public String getDescripcionTemp(){
		if(descripcion==null){
			return getDescripcion();
		}
		return descripcion;
	}

	@Override
	public void setdescripcion(String descripcion) {
		this.descripcion=descripcion;
		OperacionesSmartHomeDevice.setDescripcion(datosInvocacion, descripcion);
	}

	@Override
	public String getLocalizacion() {
		localizacion=OperacionesSmartHomeDevice.getLocalizacion(datosInvocacion);
		SingletonDoha.anniadirHabitacion(localizacion);
		return localizacion;
	}
	
	public String getLocalizacionTemp(){
		if(localizacion==null){
			return getLocalizacion();
		}
		SingletonDoha.anniadirHabitacion(localizacion);
		return localizacion;
	}
	
	@Override
	public void setLocalizacion(String localizacion) {
		this.localizacion=localizacion;
		OperacionesSmartHomeDevice.setLocalizacion(datosInvocacion, localizacion);
	}
	/**
	 * Genera un dispositivo del tipo apropiado a partir de sus datos de invocacion
	 * @param datosInvocacionServicio 
	 * @return Dispositivo especifico del servicio entrante, en caso de no ser uno devuelve null
	 */
	public static SmartHomeDeviceAndroid crearDispositivo(DatosInvocacionServicio datosInvocacionServicio){
		switch(datosInvocacionServicio.getTipo()){
		case Constantes.LUZ:return new Luz(datosInvocacionServicio);
		case Constantes.LUZ_DIMMER:return new LuzNiveles(datosInvocacionServicio);
		case Constantes.SENSOR_LUMINOSIDAD:return new SensorLuminosidad(datosInvocacionServicio);
		case Constantes.ENCHUFE:return new Enchufe(datosInvocacionServicio);
		case Constantes.ALARMA_SONORA:return new Alarma(datosInvocacionServicio);
		case Constantes.SENSOR_PRESENCIA:return new SensorPresencia(datosInvocacionServicio);
		case Constantes.PERSIANA:return new Persiana(datosInvocacionServicio);
		case Constantes.PUERTA:return new Puerta(datosInvocacionServicio);
		case Constantes.VENTANA:return new Ventana(datosInvocacionServicio);
		default:return null;	
		}
	}
	public DatosInvocacionServicio getDatosInvocacion(){
		return datosInvocacion;
	}
}
