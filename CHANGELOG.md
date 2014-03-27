Change Log
==========

Version 0.9.5 *(2014-03-27)*
----------------------------

- Added POST /api/rooms/${id}/bots
- Added POST /api/bots


Version 0.9.4 *(2014-03-02)*
----------------------------

- Added /api/user/rooms/${id}/touch.
- Added room_touched event.
- Added Idobata.getRoom(organizationSlug, roomName):Room
- Added TokenAuthenticator.TokenProvider to provides api-token at runtime.
- Changed Introduce base class for User and Bot.
- Fixed some issues.


Version 0.9.3 *(2014-02-27)*
----------------------------

- Added support for uploading image at /api/messages.
- Added new RequestInterceptor to interact with browser's cookie.
- Added new interface to handle WebSocket connection.
- Move listener interface into inner class of IdobataStream.
- Fixed some build issue with IntelliJ.


Version 0.9.2 *(2014-02-21)*
----------------------------

Embedding following dependencies,
- org.json:json
- com.pusher:pusher-java-client


Version 0.9.1 *(2014-02-21)*
----------------------------

Support WebSocket API.


Version 0.9.0 *(2014-02-19)*
----------------------------

Initial release.
