[![Build Status](https://travis-ci.org/uPhyca/idobata4j.png?branch=master)](http://travis-ci.org/uPhyca/idobata4j)

Idobata4J is a Idobata(https://idobata.io) API binding library for the Java language licensed under Apache License 2.0.

Supported endpoints
----

Endpoint  | Http method | Java API
------------- | ------------- | -------------
/api/seed  | GET | Seed Idobata.getSeed()
/api/messages | GET | List<Message> Idobata.getMessages(List<String> ids)
/api/messages | GET | List<Message> Idobata.getMessages(long roomId, long olderThan)
/api/messages | POST | Message Idobata.postMessage(long roomId, String source)
/api/messages | POST | Message Idobata.postMessage(long roomId, String fileName, String contentType, InputStream content)
/api/messages/${id} | DELETE | Message Idobata.deleteMessage(long messageId)
/api/users | GET | List<User> Idobata.getUsers(List<Long> ids)
/api/users/${id} | GET | User Idobata.getUser(long guyId)
/api/rooms | GET | List<Room> Idobata.getRooms(List<Long> ids)
/api/rooms | GET | Room Idobata.getRoom(String organizationSlug, String roomName)
/api/rooms | GET | @Deprecated List<Room> Idobata.getRooms(String organizationSlug, String roomName)
/api/room/${id} | GET | Room Idobata.getRoom(long roomId)
/api/rooms/${id}/bots | POST | Bot postBot(long roomId, String botName)
/api/bots | GET | List<Bot> getBots(List<Long> ids)
/api/bots | POST | Bot postBot(String botName, long organizationId)
/api/user/rooms/${id}/touch | POST | void Idobata.postTouch(long roomId)
/pusher/auth | POST | String Idobata.postPusherAuth(String channelName, String socketId)


Usage
----

Post a message as an user.

```Java
String email = "SET-YOUR-EMAIL";
String password = "SET-YOUR-PASSWORD";
String organizationSlug = "SET-YOUR-ORGANIZATION_SLUG";
String roomName = "SET-YOUR-ROOM-NAME";

String source = "Hello, Idobata.";

RequestInterceptor authenticator = new FormAuthenticator(email, password);
Idobata idobata = new IdobataBuilder().setRequestInterceptor(authenticator)
                                      .build();

Room room = idobata.getRoom(organizationSlug, roomName);
idobata.postMessage(room.getId(), source);

```

Post a message as a bot.

```Java
String apiToken = "SET-YOUR-API-TOKEN";
String organizationSlug = "SET-YOUR-ORGANIZATION_SLUG";
String roomName = "SET-YOUR-ROOM-NAME";

String source = "Hello, Idobata.";

RequestInterceptor authenticator = new TokenAuthenticator(apiToken);
Idobata idobata = new IdobataBuilder().setRequestInterceptor(authenticator)
                                      .build();

Room room = idobata.getRoom(organizationSlug, roomName);
idobata.postMessage(room.getId(), source);

```

Subscribing push events.

```Java
RequestInterceptor authenticator = ...
Idobata idobata = new IdobataBuilder().setRequestInterceptor(authenticator)
                                      .build();

idobata.openStream()
       .subscribeMessageCreated(new IdobataStream.Listener<MessageCreatedEvent>() {
           @Override
           public void onEvent(MessageCreatedEvent event) {
               System.out.println(event.getSenderName() + ":" + event.getBody());
           }
       })
       .subscribeMemberStatusChanged(new IdobataStream.Listener<MemberStatusChangedEvent>() {
           @Override
           public void onEvent(MemberStatusChangedEvent event) {
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
