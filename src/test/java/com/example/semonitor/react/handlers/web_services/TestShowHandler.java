package com.example.semonitor.react.handlers.web_services;

import com.example.semonitor.react.MainVerticle;
import com.example.semonitor.react.config.Config;
import com.github.javafaker.Faker;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestShowHandler {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void adds_successfully(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient webClient = WebClient.create(vertx);
    Faker faker = new Faker();
    MongoClient mongoClient = MongoClient.createShared(Vertx.vertx(), Config.dbConfig());
    JsonObject object = new JsonObject().put("name", faker.name().name()).put("url", "http://"+faker.internet().url());
    Set fields = new TreeSet<String>(){
      {
        add("_id");
        add("name");
        add("url");
      }
    };

    mongoClient.insert("web_services", object, insertResult -> {
      if(insertResult.failed()){
        testContext.failNow(new Exception("database error"));
        return;
      }

      webClient.get(Config.serverPort(),"localhost", "/api/webServices/"+insertResult.result())
        .putHeader("content-type", "application/json")
        .send( res -> {
          if(res.failed()){
            testContext.failNow(new Exception("server is not working"));
            return;
          }
          assertEquals(200, res.result().statusCode());
          assertEquals(fields, res.result().bodyAsJsonObject().fieldNames());
          testContext.completeNow();
        });
    });

  }
}
