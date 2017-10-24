package io.vertx.vendimia.syncServices.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.vendimia.domain.dto.UserDTO;
import io.vertx.vendimia.domain.dto.UserDTOConverter;
import io.vertx.vendimia.serviceInterface.UserInterface;
import io.vertx.vendimia.syncServices.implementation.UserSyncInterfaceImplementation;
import org.apache.commons.lang3.StringUtils;

public class UserWorkerGetVerticle extends AbstractVerticle implements Handler<Message<JsonObject>> {

	public static final Logger LOGGER = LoggerFactory.getLogger( UserInterface.class );

	/** Dirección del verticle dentro del Bus */
	public static final String VERTICLE_ADDRESS = "verticle.worker.get.user";

	@Override
	public void start() throws Exception {
		vertx.eventBus().consumer(VERTICLE_ADDRESS, this);

		LOGGER.info(String.format("Service  %s started.", VERTICLE_ADDRESS));
	}

	@Override
	public void handle(Message<JsonObject> event) {
		UserInterface service = new UserSyncInterfaceImplementation(vertx, config());

		JsonObject body = event.body();
		String id = body.getString("id");

		LOGGER.info(String.format(" get worker sync data", id));

		JsonObject resultObject = new JsonObject();
		if (StringUtils.isBlank(id)) {
			resultObject.put("error", " required id");
		} else {
			service.getUser(id, handler -> {
				UserDTO result = handler.result();
				if (handler.succeeded() && result != null) {
					UserDTOConverter.toJson(result, resultObject);
				} else {
					resultObject.put("error", "Not Found element");
				}
				event.reply(resultObject);

			});
		}
	}
}
