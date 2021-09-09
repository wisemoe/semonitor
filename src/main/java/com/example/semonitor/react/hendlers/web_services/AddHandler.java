package com.example.semonitor.react.hendlers.web_services;

import com.example.semonitor.react.config.Config;
import com.example.semonitor.react.hendlers.BaseServicesHandler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

import java.net.MalformedURLException;
import java.net.URL;

public class AddHandler extends BaseServicesHandler {

  @Override
  public void handle(RoutingContext event) {
    mongoClient = MongoClient.createShared(Vertx.vertx(), Config.dbConfig());
    JsonObject requestParams = event.getBodyAsJson();
    String name = requestParams.getString("name");
    String url = requestParams.getString("url");

    if(name==null || name.length()<1){
      event.response().setStatusCode(400).end(new JsonObject().put("error", "name should not be empty").encode());
      return;
    }
    try {
      new URL(url);
    } catch (MalformedURLException e) {
      event.response().setStatusCode(400).end(new JsonObject().put("error", "URI error:" + e.getMessage()).encode());
      return;
    }

    JsonObject params = new JsonObject().put("name", name).put("url", url).put("statuses", new JsonArray());
    JsonObject query = new JsonObject().
      put("$or", new JsonArray()
        .add(new JsonObject().put("name", name))
        .add(new JsonObject().put("url", url))
      );
    mongoClient.find("web_services", query, res -> {
      if (res.succeeded()) {
        if(res.result().size() == 0) {
          mongoClient.insert("web_services", params, create -> {
            if (res.succeeded()) {
              event.response().setStatusCode(201).end();
            }else {
              create.cause().printStackTrace();
              event.response().setStatusCode(500).end(new JsonObject().put("error", "server error").encode());
            }
          });
        }else {
          event.response().setStatusCode(400).end(new JsonObject().put("error", "object exists").encode());
        }
      } else {
        res.cause().printStackTrace();
        event.response().setStatusCode(500).end(new JsonObject().put("error", "server error").encode());
      }
    });
  }
}
