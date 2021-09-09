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

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);
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
