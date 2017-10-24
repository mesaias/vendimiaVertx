package io.vertx.vendimia.repositories;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.vendimia.domain.dto.UserDTO;

public class UserRepository {

  private static final String COLLECTION = "users";

  private final MongoClient mongoClient;
  protected final Vertx vertx;

  public UserRepository( Vertx vertx, JsonObject jsonConfig ){

    JsonObject jsonObject = jsonConfig.getJsonObject( "mongo" );
    this.mongoClient = MongoClient.createNonShared( vertx, jsonObject );
    this.vertx = vertx;
  }

  public void save( JsonObject jsonObject, Handler<AsyncResult< UserDTO >> resultHandler ){
    //this.mongoClient.save( COLLECTION, jsonObject, resultHandler );
  }
}
