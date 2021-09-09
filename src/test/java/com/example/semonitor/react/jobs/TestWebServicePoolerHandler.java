package com.example.semonitor.react.jobs;

import com.example.semonitor.react.MainVerticle;
import com.example.semonitor.react.config.Config;
import com.github.javafaker.Faker;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
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
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestWebServicePoolerHandler {
  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void adds_successfully(Vertx vertx, VertxTestContext testContext) throws Throwable {
    Faker faker = new Faker();
    MongoClient mongoClient = MongoClient.createShared(Vertx.vertx(), Config.dbConfig());
    JsonObject object = new JsonObject()
      .put("name", faker.name().name())
      .put("url", "http://localhost:"+Config.serverPort())
      .put("statuses", new JsonArray());

    mongoClient.insert("web_services", object, insertResult -> {
      if(insertResult.failed()){
        testContext.failNow(new Exception("database error"));
        return;
      }
      JsonObject searchParams = new JsonObject().put("_id", insertResult.result());
      mongoClient.findOne("web_services", searchParams, null, findResult -> {
        WebServicePooler.run(vertx, findResult.result());

        try {
          TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
          //TODO make it async
          e.printStackTrace();
        }
        mongoClient.findOne("web_services", searchParams, null, findResult2 -> {
          if(findResult2.failed()){
            testContext.failNow(new Exception("database error"));
            return;
          }
          String firstStatus = findResult2.result()
            .getJsonArray("statuses")
            .getJsonObject(0)
            .getString("status");

          assertEquals(1, findResult2.result().getJsonArray("statuses").size());
          assertEquals("OK", firstStatus);
          testContext.completeNow();
        });
      });
    });

  }
}
