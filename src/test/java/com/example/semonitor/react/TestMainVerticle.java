package com.example.semonitor.react;

import com.example.semonitor.react.config.Config;
import io.vertx.core.Vertx;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @BeforeAll
  static void cleanup_db(Vertx vertx, VertxTestContext testContext) {
    MongoClient mongoClient = MongoClient.createShared(Vertx.vertx(), Config.dbConfig());
    mongoClient.dropCollection("web_services", drop -> testContext.completeNow());
  }

  @Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient webClient = WebClient.create(vertx);
    webClient.get(Config.serverPort(),"localhost", "/").send(res -> {
      if(res.succeeded()){
        assertEquals(200, res.result().statusCode());
        assertEquals("Hello from Vert.x!", res.result().bodyAsString());
        testContext.completeNow();
      }else{
        testContext.failNow(new Exception("verticle not deployed"));
      }
    });
  }
}
