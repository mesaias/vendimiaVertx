package com.vertx.vendimia;

import io.vertx.core.AbstractVerticle;

public class MainVerticleRespaldp extends AbstractVerticle {

  @Override
  public void start() {
    vertx.createHttpServer()
        .requestHandler(req -> req.response().end("Hello Vert.x!"))
        .listen(8080);
  }

}
