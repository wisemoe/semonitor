package com.example.semonitor.react.hendlers.web_services;

import com.example.semonitor.react.config.Config;
import com.example.semonitor.react.hendlers.BaseServicesHandler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

public class AllHandler extends BaseServicesHandler {
  @Override
  public void handle(RoutingContext event) {
    event.response().headers().add("content-type", "application/json");
    mongoClient = MongoClient.createShared(Vertx.vertx(), Config.dbConfig());
    JsonObject query = new JsonObject();
    FindOptions options = new FindOptions()
      .setFields(new JsonObject().put("name", 1).put("url", 1))
      .setLimit(100);
    mongoClient.findWithOptions("web_services", query, options, res -> {
      if (res.succeeded()) {
        event.response().end(new JsonArray(res.result()).encode());
      } else {
        res.cause().printStackTrace();
        event.response().setStatusCode(500).end(new JsonObject().put("error", "server error").encode());
      }
    });
  }
}
