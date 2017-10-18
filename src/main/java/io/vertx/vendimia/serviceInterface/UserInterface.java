package io.vertx.vendimia.serviceInterface;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.vendimia.domain.dto.UserDTO;

@VertxGen
@ProxyGen
public interface UserInterface {

  public static final String SERVICE_NAME = "user-eb-service";

  public static final String SERVICE_ADDRESS = "user.sensor";

  public abstract void saveUser( UserDTO userDTO, Handler<AsyncResult < UserDTO > > resultHandler );

  public abstract void getUser( String id, Handler<AsyncResult < UserDTO > > resultHandler );

  public abstract void removeUser( String id, Handler<AsyncResult < Void > > resultHandler );
}
