package io.vertx.vendimia.verticles;

import io.vertx.blueprint.microservice.common.BaseMicroserviceVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.serviceproxy.ProxyHelper;
import io.vertx.vendimia.controller.UserController;
import io.vertx.vendimia.servicesProvider.UserServiceProvider;
import io.vertx.vendimia.serviceInterface.UserInterface;
import io.vertx.vendimia.syncServices.verticles.UserWorkerGetVerticle;

public class UserVerticle extends BaseMicroserviceVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserVerticle.class);

	@Override
	public void start(Future<Void> future) throws Exception {
		super.start();
    UserServiceProvider userServiceProvider = UserServiceProvider.getInstance();
		userServiceProvider.init(vertx, config());

    ProxyHelper.registerService(UserInterface.class, vertx, userServiceProvider.getUserInterface(), UserInterface.SERVICE_ADDRESS);

		deployVerticle().compose(ar -> deploySyncVerticle()).setHandler(future.completer());
	}

  /**
	 * Despliegue de otro verticle de tipo worker con operación síncrona
	 *
	 */
	private Future<Void> deployVerticle() {
		Future<Void> future = Future.future();
		DeploymentOptions deploymentOptions = new DeploymentOptions();
		deploymentOptions.setConfig(config());

		// deploymentOptions.setInstances(1);
		deploymentOptions.setInstances(Runtime.getRuntime().availableProcessors() * 2);
		/* Desplegamos nuestro verticle http server con los datos adecuados */
		vertx.deployVerticle( UserController.class.getName(), deploymentOptions, ar -> {
			if (ar.succeeded()) {
				LOGGER.info(String.format("Deployment verticle  ok vendimia "));
				future.complete();
			} else {
				LOGGER.info(String.format("Deployment verticle ko vendimia " + ar.cause()));
				future.fail(ar.cause());
			}
		});
		return future.map(r -> null);

	}

	/**
	 * Despliegue de otro verticle de tipo worker con operación síncrona
	 *
	 */
	private Future<Void> deploySyncVerticle() {
		Future<Void> future = Future.future();
		DeploymentOptions deploymentOptions = new DeploymentOptions();
		deploymentOptions.setConfig(config());
		deploymentOptions.setWorker(true);
		deploymentOptions.setInstances(5);

		vertx.deployVerticle( UserWorkerGetVerticle.class.getName(), deploymentOptions, ar -> {
			if (ar.succeeded()) {
				LOGGER.info(String.format("Deployment verticle  ok UserWorkerGetVerticle "));
				future.succeeded();
			} else {
				LOGGER.info(String.format("Deployment verticle ko UserWorkerGetVerticle " + ar.cause()));
				future.fail(ar.cause());
			}
		});
		return future.map(r -> null);

	}

}
