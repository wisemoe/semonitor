package com.example.semonitor.react.hendlers;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

public class BaseServicesHandler implements Handler<RoutingContext> {
  protected MongoClient mongoClient;

  @Override
  public void handle(RoutingContext event) {

  }
}
