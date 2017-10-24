package io.vertx.vendimia.syncServices.implementation;

import io.vertx.vendimia.implementation.SensorInterfaceImpl;
import io.vertx.vendimia.serviceInterface.SensorInterface;
import io.vertx.vendimia.domain.dto.SensorDTO;
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
public class SensorSyncInterfaceImpl extends SensorInterfaceImpl {



	/**
	 * Builder para la creación de una instancia genérica del servicio
	 *
	 * @author manuel
	 *
	 */
	public static class Builder {
		public SensorInterface create(Vertx vertx, JsonObject config) {
			return new SensorSyncInterfaceImpl(vertx, config);
		}
	}

	public SensorSyncInterfaceImpl(Vertx vertx, JsonObject config) {
		super(vertx, config);
	}

	@Override
	public void getSensor(String id, Handler<AsyncResult<SensorDTO>> resultHandler) {

		/* Paramos X simulando sincronía y realizamos la operación */
		vertx.setTimer(200, h -> {
			/* Invocamos al método del padre */
			SensorSyncInterfaceImpl.super.getSensor(id, resultHandler);
		});

	}

}
