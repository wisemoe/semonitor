package com.example.semonitor.react.hendlers.web_services;

import com.example.semonitor.react.config.Config;
import com.example.semonitor.react.hendlers.BaseServicesHandler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

public class ShowHandler extends BaseServicesHandler {
  @Override
  public void handle(RoutingContext event) {
    mongoClient = MongoClient.createShared(Vertx.vertx(), Config.dbConfig());
    JsonObject query = new JsonObject().put("_id", event.request().getParam("id"));
    JsonObject options = new JsonObject();
    mongoClient.findOne("web_services", query, options, res -> {
      if (res.succeeded()) {
        event.response().end(res.result().encode());
      } else {
        res.cause().printStackTrace();
        event.response().setStatusCode(500).end(new JsonObject().put("error", "server error").encode());
      }
    });
  }
}