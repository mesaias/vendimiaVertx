package io.vertx.vendimia.service.sensor;

import io.vertx.vendimia.implementation.SensorInterfaceImpl;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.vendimia.serviceInterface.SensorInterface;

/**
 * Provider para realizar el singleton de la implementación de los sensores
 *
 * @author manuel
 *
 */
public final class SensorServiceProvider {

	private SensorInterface sensorInterface;

	/* Singleton */
	private static SensorServiceProvider instance = new SensorServiceProvider();

	public synchronized SensorInterface init(Vertx vertx, JsonObject config) {
    setSensorInterface(new SensorInterfaceImpl(vertx, config));
		return getSensorInterface();
	}

	public static SensorServiceProvider getInstance() {
		return instance;
	}

	private SensorServiceProvider() {
	}

	public SensorInterface getSensorInterface() {
		return sensorInterface;
	}

	private void setSensorInterface(SensorInterface SensorInterface) {
		this.sensorInterface = SensorInterface;
	}

}
