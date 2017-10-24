package io.vertx.vendimia.controller;

import io.vertx.blueprint.microservice.common.RestAPIVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.serviceproxy.ProxyHelper;
import io.vertx.vendimia.domain.dto.UserDTO;
import io.vertx.vendimia.serviceInterface.UserInterface;
import io.vertx.vendimia.syncServices.verticles.UserWorkerGetVerticle;
import org.apache.commons.lang3.StringUtils;

public class UserController extends RestAPIVerticle {


	private static final String API_PATH = "users";

	private static final String API_SAVE = String.format("/%s", API_PATH);
	private static final String API_RETRIEVE = String.format("/%s/:id", API_PATH);
	private static final String API_DELETE = String.format("/%s/:id", API_PATH);

	private static final String API_SYNC_RETRIEVE = String.format("/%s/sync/:id", API_PATH);


	@Override
	public void start(Future<Void> future) throws Exception {

		super.start();
    Router router = Router.router(vertx);

		enableCorsSupport(router);

		router.route().handler(BodyHandler.create());

		/* Rutas */
    router.post(API_SAVE).handler( this::apiSave );
		router.get(API_RETRIEVE).handler( this::apiGet );
		router.get(API_SYNC_RETRIEVE).handler( this::apiSyncGet );
		router.delete(API_DELETE).handler( this::apiDelete );

		enableHeartbeatCheck(router, config());

		String host = config().getString("vertx.host", "0.0.0.0");
		int port = config().getInteger("vertx.port", 8081);
		createHttpServer(router, host, port).setHandler(future.completer());
	}

  public void apiSave( RoutingContext context ) {
    UserDTO userDTO = new UserDTO(new JsonObject(context.getBodyAsString()));

    if (StringUtils.isBlank(userDTO.getUsername()) || StringUtils.isBlank(userDTO.getPassword())) {
      badRequest(context, new IllegalStateException("User bad request"));
    } else {
      this.getUserService().saveUser( userDTO, ar -> {
        if (ar.succeeded()) {
          UserDTO newUserDTO = ar.result();
          JsonObject result = new JsonObject().put("message", String.format("user %s saved", newUserDTO.getId()));
          resultBody(context, newUserDTO.toJson(), 200);
        } else {
          internalError(context, ar.cause());
        }
      });
    }
  }

  private void apiGet(RoutingContext context) {
		/* Obtenemos el parámetro de la url */
    String id = context.request().getParam("id");
    getUserService().getUser(id, resultHandlerNonEmpty(context));
  }

  private void apiDelete(RoutingContext context) {
		/* Obtenemos el parámetro de la url */
    String id = context.request().getParam("id");
    getUserService().removeUser(id, deleteResultHandler(context));
  }

  private void apiSyncGet(RoutingContext context) {
		/* Obtenemos el parámetro de la url */
    String id = context.request().getParam("id");

    vertx.eventBus().send(UserWorkerGetVerticle.VERTICLE_ADDRESS, new JsonObject().put("id", id), handler -> {
      if (handler.succeeded()) {
        resultBody(context, (JsonObject) handler.result().body(), 200);
      } else {
        notFound(context);
      }

    });
  }

  public UserInterface getUserService() {
    return ProxyHelper.createProxy( UserInterface.class, vertx, UserInterface.SERVICE_ADDRESS );
  }

}
