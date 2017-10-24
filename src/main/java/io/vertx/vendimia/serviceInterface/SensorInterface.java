package io.vertx.vendimia.serviceInterface;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.vendimia.domain.dto.SensorDTO;

@VertxGen
@ProxyGen
public interface SensorInterface {

  public static final Logger LOGGER = LoggerFactory.getLogger( SensorInterface.class );

  public static final String SERVICE_NAME = "sensor-eb-service";
  public static final String SERVICE_ADDRESS = "service.sensor";

  public abstract void saveSensor(SensorDTO dto, Handler<AsyncResult<SensorDTO>> resultHandler);

  public abstract void getSensor(String id, Handler<AsyncResult<SensorDTO>> resultHandler);

  public abstract void removeSensor(String id, Handler<AsyncResult<Void>> resultHandler);

}

