package io.vertx.vendimia.syncServices.implementation;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.vendimia.domain.dto.UserDTO;
import io.vertx.vendimia.implementation.UserInterfaceImplementation;
import io.vertx.vendimia.serviceInterface.UserInterface;

/**
 * Simulación de evento síncrono
 *
 * @author manuel
 *
 */
public class UserSyncInterfaceImplementation extends UserInterfaceImplementation {

	public static class Builder {
		public UserInterface create(Vertx vertx, JsonObject config) {
			return new UserSyncInterfaceImplementation(vertx, config);
		}
	}

	public UserSyncInterfaceImplementation(Vertx vertx, JsonObject config) {
		super(vertx, config);
	}

	@Override
	public void getUser( String id, Handler<AsyncResult<UserDTO>> resultHandler ) {

		/* Paramos X simulando sincronía y realizamos la operación */
		vertx.setTimer(200, h -> {
			/* Invocamos al método del padre */
			UserSyncInterfaceImplementation.super.getUser(id, resultHandler);
		});

	}

}
