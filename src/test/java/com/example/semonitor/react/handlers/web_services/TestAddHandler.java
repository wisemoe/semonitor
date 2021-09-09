package com.example.semonitor.react.handlers.web_services;

import com.example.semonitor.react.MainVerticle;
import com.example.semonitor.react.config.Config;
import com.github.javafaker.Faker;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(VertxExtension.class)
public class TestAddHandler {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void adds_successfully(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient webClient = WebClient.create(vertx);
    Faker faker = new Faker();
    MongoClient mongoClient = MongoClient.createShared(Vertx.vertx(), Config.dbConfig());

    JsonObject body = new JsonObject().put("name", faker.name().name()).put("url", "http://"+faker.internet().url());
    webClient.post(Config.serverPort(),"localhost", "/api/webServices")
      .putHeader("content-type", "application/json")
      .sendJsonObject(body, res -> {
        if(res.failed()){
          testContext.failNow(new Exception("server is not working"));
          return;
        }
        assertEquals(201, res.result().statusCode());
        assertNull(res.result().bodyAsString());
        testContext.completeNow();
      }
    );
  }
}
