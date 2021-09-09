package com.example.semonitor.react.config;

import io.vertx.core.json.JsonObject;

import java.util.Map;

public class Config {
  static Map<String, String> env = System.getenv();

  public static int serverPort(){
    String port = env.get("SEMANITOR_PORT");
    if(port!=null && port.length()>0) {
      return Integer.parseInt(port);
    }else{
      return 8080;
    }
  }

  public static JsonObject dbConfig(){
    String environment = env.get("SEMANITOR_ENV");
    if(environment !=null && environment.equals("production")) {
      return prodDbConfig();
    }else return testDbConfig();
  }
  public static JsonObject prodDbConfig(){
    return new JsonObject()
      .put("db_name ", "semonitor_prod")
      .put("host", "127.0.0.1")
      .put("port", 27017);
  }
  public static JsonObject testDbConfig(){
    return new JsonObject()
      .put("db_name", "semonitor_test")
      .put("host", "127.0.0.1")
      .put("port", 27017);
  }

}
