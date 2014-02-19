Idobata4J is a Idobata API binding library for the Java language licensed under Apache License 2.0.

Idobata4J includes software from JSON.org to parse JSON response from the Twitter API. You can see the license term at http://www.JSON.org/license.html

Usage
----

As the user.

```Java
String email = ...
String password = ...
String organizationSlug = ...
String roomName = ...
String source = ...

FormAuthenticator authenticator = new FormAuthenticator(email, password);
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

TokenAuthenticator authenticator = new TokenAuthenticator(apiToken);
Idobata idobata = new IdobataBuilder().setRequestInterceptor(authenticator)
                                      .build();

List<Room> rooms = idobata.getRooms(organizationSlug, roomName);
Room room = rooms.get(0);

idobata.postMessage(room.getId(), source);

```

Download
-----

Gradle
```groovy
compile "com.uphyca.idobata:idobata4j-core:${daggerVersion}"
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
