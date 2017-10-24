package io.vertx.vendimia.controller;

import io.vertx.vendimia.serviceInterface.SensorInterface;
import io.vertx.vendimia.domain.dto.SensorDTO;
import io.vertx.blueprint.microservice.common.RestAPIVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.serviceproxy.ProxyHelper;
import io.vertx.vendimia.syncServices.verticles.SensorWorkerGetVerticle;
import io.vertx.vendimia.verticles.MainVerticle;
import org.apache.commons.lang3.StringUtils;

public class HttpServerVerticle extends RestAPIVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

	private static final String API_PATH = "sensors";

	/** Declaración de los mappings */
	private static final String API_SAVE = String.format("/%s", API_PATH);
	private static final String API_RETRIEVE = String.format("/%s/:id", API_PATH);
	private static final String API_DELETE = String.format("/%s/:id", API_PATH);

	/** ejemplo de worker síncrono */
	private static final String API_SYNC_RETRIEVE = String.format("/%s/sync/:id", API_PATH);

	public HttpServerVerticle() {
	}

	@Override
	public void start(Future<Void> future) throws Exception {

		super.start();
    Router router = Router.router(vertx);

		/* Habilitamos el CORS */
		enableCorsSupport(router);

		/* Nos permitirá parsear el body handler para obtener el payload */
		router.route().handler(BodyHandler.create());

		/* Rutas */
		router.post(API_SAVE).handler(this::apiSave);
		router.get(API_RETRIEVE).handler(this::apiGet);
		router.get(API_SYNC_RETRIEVE).handler(this::apiSyncGet);
		router.delete(API_DELETE).handler(this::apiDelete);

		/* Típico ¿Estas vivo? :p */
		enableHeartbeatCheck(router, config());

		// Datos del server de arranque y por defecto
		String host = config().getString("vertx.host", "0.0.0.0");
		int port = config().getInteger("vertx.port", 8081);
    System.out.println(port);
    System.out.println(future);
    /* Creamos nuestro servidor http */
		createHttpServer( router, host, port ).setHandler(future.completer());

		LOGGER.info(" started " + this.getClass().getSimpleName());

	}

	private void apiSave(RoutingContext context) {
		/* Parseamos a nuesto objeto de entrada */
		SensorDTO sensorDTO = new SensorDTO(new JsonObject(context.getBodyAsString()));
		LOGGER.info("apiSave with values  " + sensorDTO.toString());

		/* Estas comprobaciones serían muy mejorables con la jsr303 */
		if (StringUtils.isBlank(sensorDTO.getDescription()) || StringUtils.isBlank(sensorDTO.getType())) {
			badRequest(context, new IllegalStateException("Sensor bad request"));
		} else {
			getSensorService().saveSensor(sensorDTO, ar -> {
				if (ar.succeeded()) {
					SensorDTO newSensorDTO = ar.result();
					JsonObject result = new JsonObject().put("message", String.format("sensor %s saved", newSensorDTO.getId()));
					resultBody(context, result, 200);
				} else {
					internalError(context, ar.cause());
				}
			});
		}
	}

	private void apiGet(RoutingContext context) {
		/* Obtenemos el parámetro de la url */
		String id = context.request().getParam("id");
		getSensorService().getSensor(id, resultHandlerNonEmpty(context));
	}

	private void apiDelete(RoutingContext context) {
		/* Obtenemos el parámetro de la url */
		String id = context.request().getParam("id");
		getSensorService().removeSensor(id, deleteResultHandler(context));
	}

	/**
	 * Obtenemos de forma síncrona un sensor
	 *
	 * @param context
	 */
	private void apiSyncGet(RoutingContext context) {
		/* Obtenemos el parámetro de la url */
		String id = context.request().getParam("id");

		vertx.eventBus().send(SensorWorkerGetVerticle.VERTICLE_ADDRESS, new JsonObject().put("id", id), handler -> {
			if (handler.succeeded()) {
				resultBody(context, (JsonObject) handler.result().body(), 200);
			} else {
				notFound(context);
			}

		});
	}

	/**
	 * Método para mostrar el comportamiento de cuando una operación viaja por el bus o no lo hace. hay modos más elegantes pero hemos creido que este era muy
	 * ilustrativo.
	 *
	 * @return
	 */
	private SensorInterface getSensorService() {
		/* Uso del bus para las comunicaciones  */
    SensorInterface sensorInterface = ProxyHelper.createProxy(SensorInterface.class, vertx, SensorInterface.SERVICE_ADDRESS);
		//SensorInterface sensorInterface = SensorServiceProvider.getInstance().getSensorInterface();
		return sensorInterface;
	}

}
