package io.vertx.vendimia.verticles;

import io.vertx.vendimia.HttpServerVerticles.HttpServerVerticle;
import io.vertx.vendimia.service.sensor.SensorService;
import io.vertx.vendimia.service.sensor.SensorServiceProvider;
import io.vertx.vendimia.service.syncsensor.verticle.SensorWorkerGetVerticle;
import io.vertx.blueprint.microservice.common.BaseMicroserviceVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.serviceproxy.ProxyHelper;

/**
 * Verticle que arrancará el resto de los de la aplicación
 *
 * @author manuel
 *
 */
public class MainVerticle extends BaseMicroserviceVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

	@Override
	public void start(Future<Void> future) throws Exception {
		super.start();
		/* Creamos una única instancia de nuestro servicio de sensores */
    System.out.println( vertx );
    SensorServiceProvider sensorServiceProvider = SensorServiceProvider.getInstance();
		sensorServiceProvider.init(vertx, config());
    System.out.println( future);

		/* Registramos el servicio (Para el ejemplo del uso del bus ) */
		ProxyHelper.registerService(SensorService.class, vertx, sensorServiceProvider.getSensorService(), SensorService.SERVICE_ADDRESS);

		/* MODO INCORRECTO DE DESPLEGAR VERTICLES no asíncrono !!!! Vert.x es asíncrono desde su arranque !!!*/
		/* Desplegamos el sync verticle */
		//deployVerticle();
		/* Ejemplo de verticle worker (esto se debería de componer en el arranque global de la aplicación controlando el resultado del despliegue) */
		//deploySyncVerticle();

		/* Modo correcto de desplegar los verticles coordinado VERT.X*/
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

		/** Importante observar las trazas y ver la asignación que se nos ha realizado del Event Loop = siempre el mismo nº */
		// deploymentOptions.setInstances(1);
		deploymentOptions.setInstances(Runtime.getRuntime().availableProcessors() * 2);
		/* Desplegamos nuestro verticle http server con los datos adecuados */
		vertx.deployVerticle(HttpServerVerticle.class.getName(), deploymentOptions, ar -> {
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
		/* Podemos probar si lo asignamos como no worker ... que pasaría ? */
		deploymentOptions.setWorker(true);
		deploymentOptions.setInstances(5);

		/* Desplegamos nuestro verticle http server con los datos adecuados */
		vertx.deployVerticle(SensorWorkerGetVerticle.class.getName(), deploymentOptions, ar -> {
			if (ar.succeeded()) {
				LOGGER.info(String.format("Deployment verticle  ok SensorWorkerGetVerticle "));
				future.succeeded();
			} else {
				LOGGER.info(String.format("Deployment verticle ko SensorWorkerGetVerticle " + ar.cause()));
				future.fail(ar.cause());
			}
		});
		return future.map(r -> null);

	}

}
