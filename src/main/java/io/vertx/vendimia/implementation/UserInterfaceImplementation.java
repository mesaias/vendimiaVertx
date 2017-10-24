package io.vertx.vendimia.implementation;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.vendimia.domain.dto.UserDTO;
import io.vertx.vendimia.serviceInterface.UserInterface;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserInterfaceImplementation implements UserInterface {

	private static final String COLLECTION = "users";

	private final MongoClient client;
	protected final Vertx vertx;

	public UserInterfaceImplementation(Vertx vertx, JsonObject config) {
		JsonObject jsonObject = config.getJsonObject("mongo");
    this.client = MongoClient.createNonShared(vertx, jsonObject);
		this.vertx = vertx;
	}

	@Override
	public void saveUser(UserDTO userDTO, Handler<AsyncResult< UserDTO >> resultHandler) {

		userDTO.setId(new ObjectId().toString());
		client.save(COLLECTION, new JsonObject().put( "_id", userDTO.getId()).put( "username", userDTO.getUsername()).put( "password", userDTO.getPassword())
				.put("createdAt", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())), ar -> {
					if (ar.succeeded()) {
						UserDTO newUserDTO = new UserDTO( userDTO );
						resultHandler.handle(Future.succeededFuture( newUserDTO ) );
					} else {
						resultHandler.handle(Future.failedFuture(ar.cause()));
					}
				});
	}

	@Override
	public void getUser(String id, Handler<AsyncResult< UserDTO>> resultHandler) {
    LOGGER.info(String.format(" get user data ", id));
		JsonObject query = new JsonObject().put("_id", id);
		client.findOne(COLLECTION, query, null, ar -> {
			if (ar.succeeded()) {
				if (ar.result() == null) {
					resultHandler.handle(Future.succeededFuture());
				} else {
					UserDTO userDTO = new UserDTO(ar.result().put("id", ar.result().getString("_id")));
					resultHandler.handle(Future.succeededFuture( userDTO ));
				}
			} else {
				resultHandler.handle(Future.failedFuture(ar.cause()));
			}
		});
	}

	@Override
	public void removeUser(String id, Handler<AsyncResult<Void>> resultHandler) {
    LOGGER.info(String.format(" remove user data ", id));
		JsonObject query = new JsonObject().put("_id", id);
		client.removeDocument(COLLECTION, query, ar -> {
			if (ar.succeeded()) {
				resultHandler.handle(Future.succeededFuture());
			} else {
				resultHandler.handle(Future.failedFuture(ar.cause()));
			}
		});
	}
}
