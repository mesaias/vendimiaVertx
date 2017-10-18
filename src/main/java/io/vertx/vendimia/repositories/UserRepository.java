package io.vertx.vendimia.repositories;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class UserRepository {

  private static final String COLLECTION = "users";

  private final MongoClient mongoClient;
  protected final Vertx vertx;

  public UserRepository( Vertx vertx, JsonObject jsonConfig ){

    JsonObject jsonObject = jsonConfig.getJsonObject( "mongo" );
    this.mongoClient = MongoClient.createNonShared( vertx, jsonObject );
    this.vertx = vertx;
  }


}
