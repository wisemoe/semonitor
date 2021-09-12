package com.example.semonitor.react;

import com.example.semonitor.react.config.Config;
import com.example.semonitor.react.hendlers.web_services.AddHandler;
import com.example.semonitor.react.hendlers.web_services.AllHandler;
import com.example.semonitor.react.hendlers.web_services.ShowHandler;
import com.example.semonitor.react.jobs.WebServicePooler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    WebServicePooler.recurring(vertx, 60000);
    Router router = Router.router(vertx);
    router.route().handler(CorsHandler.create("*")
      .allowedMethod(io.vertx.core.http.HttpMethod.GET)
      .allowedMethod(io.vertx.core.http.HttpMethod.POST)
      .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
      .allowedHeader("Access-Control-Request-Method")
      .allowedHeader("Access-Control-Allow-Credentials")
      .allowedHeader("Access-Control-Allow-Origin")
      .allowedHeader("Access-Control-Allow-Headers")
      .allowedHeader("Content-Type"));
    router.route().handler(BodyHandler.create());

    router.get("/api/webServices/:id").handler(new ShowHandler());
    router.get("/api/webServices").handler(new AllHandler());
    router.post("/api/webServices").handler(new AddHandler());
    router.route("/").handler(new Handler<RoutingContext>() {
      @Override
      public void handle(RoutingContext event) {
        event.response()
          .putHeader("content-type", "text/plain")
          .end("Hello from Vert.x!");
      }
    });
    vertx.createHttpServer().requestHandler(router).listen(Config.serverPort(), http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port"+Config.serverPort());
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
