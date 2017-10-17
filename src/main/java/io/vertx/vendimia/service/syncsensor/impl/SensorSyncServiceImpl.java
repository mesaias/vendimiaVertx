package io.vertx.vendimia.service.syncsensor.impl;

import io.vertx.vendimia.service.sensor.SensorService;
import io.vertx.vendimia.domain.dto.SensorDTO;
import io.vertx.vendimia.service.sensor.impl.SensorServiceImpl;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * Simulación de evento síncrono
 *
 * @author manuel
 *
 */
public class SensorSyncServiceImpl extends SensorServiceImpl {



	/**
	 * Builder para la creación de una instancia genérica del servicio
	 *
	 * @author manuel
	 *
	 */
	public static class Builder {
		public SensorService create(Vertx vertx, JsonObject config) {
			return new SensorSyncServiceImpl(vertx, config);
		}
	}

	public SensorSyncServiceImpl(Vertx vertx, JsonObject config) {
		super(vertx, config);
	}

	@Override
	public void getSensor(String id, Handler<AsyncResult<SensorDTO>> resultHandler) {

		/* Paramos X simulando sincronía y realizamos la operación */
		vertx.setTimer(200, h -> {
			/* Invocamos al método del padre */
			SensorSyncServiceImpl.super.getSensor(id, resultHandler);
		});

	}

}
