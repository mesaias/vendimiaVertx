package io.vertx.vendimia.implementation;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.core.Vertx;
import io.vertx.vendimia.domain.dto.UserDTO;
import org.bson.types.ObjectId;

public class UserInterfaceImplementation {

  private static final String COLLECTION = "users";

  private final MongoClient mongoClient;
  protected final Vertx vertx;

  public UserInterfaceImplementation( Vertx vertx, JsonObject jsonConfig ){

    JsonObject jsonObject = jsonConfig.getJsonObject( "mongo" );
    this.mongoClient = MongoClient.createNonShared( vertx, jsonObject );
    this.vertx = vertx;
  }

  @Override
  public void saveUser (UserDTO userDTO, Handler<AsyncResult < UserDTO >> resultHandler ){

    userDTO.setId( new ObjectId().toString() );
    mongoClient.save( COLLECTION, UserDTOCOnverter.toJson)
  }

}
