package com.example.semonitor.react.jobs;

import com.example.semonitor.react.config.Config;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.client.WebClient;

import java.net.MalformedURLException;
import java.net.URL;

public class WebServicePooler {

  public static void recurring(Vertx vertx, int period){
    vertx.setPeriodic(period, id -> {
      MongoClient mongoClient = MongoClient.createShared(Vertx.vertx(), Config.dbConfig());
      mongoClient.findBatch("web_services", new JsonObject() )
        .exceptionHandler(throwable -> throwable.printStackTrace())
        .handler( service -> run(vertx, service) );
    });
  }

  public static void run(Vertx vertx, JsonObject webService){
      WebClient client = WebClient.create(vertx);
      MongoClient mongoClient = MongoClient.createShared(Vertx.vertx(), Config.dbConfig());
      URL url = null;
      try {
        url = new URL(webService.getString("url"));
      } catch (MalformedURLException e) {
        System.out.println("URI error:" + e.getMessage());
      }
      client
        .get(url.getPort(), url.getHost(), url.getPath())
        .ssl(url.getDefaultPort()==443)
        .send(response -> {
          if(response.succeeded()){
            System.out.println("Received response with status code " + response.result().statusCode());
            webService.getJsonArray("statuses")
              .add(new JsonObject()
                .put("status", "OK")
                .put("time", System.currentTimeMillis())
              );
            mongoClient.save("web_services", webService, res -> {
              if(res.failed())res.cause().printStackTrace();
            });
          }else{
            response.cause().printStackTrace();
            webService.getJsonArray("statuses")
              .add(new JsonObject()
                .put("status", "FAIL")
                .put("time", System.currentTimeMillis())
              );
            mongoClient.save("web_services", webService, res -> {
              if(res.failed())res.cause().printStackTrace();
            });
          }
        });
  }
}
