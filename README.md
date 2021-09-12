#Semonitor.react
this is a full stack application which helps you to monitor web services

##Running
first make sure you have nodejs and yarn installed, then:
1. run mongodb on local machine (you can use docker-compose in docker folder)
2. run `./gradlew run`
3. go to `src/main/frontend` and run `yarn build && yarn start`

##Building

To launch your tests:
```
./gradlew clean test
```

To package your application:
```
./gradlew clean assemble
```

To run your application:
```
./gradlew clean run
```

##Help

* https://vertx.io/docs/[Vert.x Documentation]
* https://stackoverflow.com/questions/tagged/vert.x?sort=newest&pageSize=15[Vert.x Stack Overflow]
* https://groups.google.com/forum/?fromgroups#!forum/vertx[Vert.x User Group]
* https://gitter.im/eclipse-vertx/vertx-users[Vert.x Gitter]


