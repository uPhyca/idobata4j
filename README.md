[![Build Status](https://secure.travis-ci.org/uPhyca/idobata4j.png?branch=master)](http://travis-ci.org/uPhyca/idobata4j)

Idobata4J is a Idobata(https://idobata.io) API binding library for the Java language licensed under Apache License 2.0.

Usage
----

As the user.

```Java
String email = ...
String password = ...
String organizationSlug = ...
String roomName = ...
String source = ...

RequestInterceptor authenticator = new FormAuthenticator(email, password);
Idobata idobata = new IdobataBuilder().setRequestInterceptor(authenticator)
                                      .build();

List<Room> rooms = idobata.getRooms(organizationSlug, roomName);
Room room = rooms.get(0);

idobata.postMessage(room.getId(), source);

```

As the bot.

```Java
String apiToken = ...
String organizationSlug = ...
String roomName = ...
String source = ...

RequestInterceptor authenticator = new TokenAuthenticator(apiToken);
Idobata idobata = new IdobataBuilder().setRequestInterceptor(authenticator)
                                      .build();

List<Room> rooms = idobata.getRooms(organizationSlug, roomName);
Room room = rooms.get(0);

idobata.postMessage(room.getId(), source);

```

Subscribing stream.

```Java
RequestInterceptor authenticator = ...
Idobata idobata = new IdobataBuilder().setRequestInterceptor(authenticator)
                                      .build();

idobata.openStream()
       .subscribeMessageCreated(new ResponseListener<MessageCreatedEvent>() {
           @Override
           public void onResponse(MessageCreatedEvent event) {
               System.out.println(event.getSenderName() + ":" + event.getBody());
           }
       })
       .subscribeMemberStatusChanged(new ResponseListener<MemberStatusChangedEvent>() {
           @Override
           public void onResponse(MemberStatusChangedEvent event) {
               System.out.println(event.getId() + ":" + event.getStatus());
           }
       });
```

Download
-----

Idobata4J releases are available on [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.uphyca.idobata%22).

Gradle
```groovy
compile "com.uphyca.idobata:idobata4j-core:${idobata4jVersion}"
```

Maven
```xml
<dependencies>
  <dependency>
    <groupId>com.uphyca.idobata</groupId>
    <artifactId>idobata4j-core</artifactId>
    <version>${idobata4j.version}</version>
  </dependency>
</dependencies>
```

License
-------

    Copyright 2014 uPhyca, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
