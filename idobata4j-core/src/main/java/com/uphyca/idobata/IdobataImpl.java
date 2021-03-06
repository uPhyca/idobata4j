/*
 * Copyright (C) 2014 uPhyca Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uphyca.idobata;

import com.pusher.client.AuthorizationFailureException;
import com.pusher.client.Authorizer;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.uphyca.idobata.http.*;
import com.uphyca.idobata.model.*;
import com.uphyca.idobata.pusher.PusherBuilder;
import com.uphyca.idobata.transform.Converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation of {@link com.uphyca.idobata.Idobata}
 * 
 * @see com.uphyca.idobata.IdobataBuilder
 * @author Sosuke Masui (masui@uphyca.com)
 */
class IdobataImpl implements Idobata {

    private final Client client;
    private final RequestInterceptor requestInterceptor;
    private final Converter converter;
    private final PusherBuilder pusherBuilder;

    IdobataImpl(Client client, RequestInterceptor requestInterceptor, Converter converter, PusherBuilder pusherBuilder) {
        this.client = client;
        this.requestInterceptor = requestInterceptor;
        this.converter = converter;
        this.pusherBuilder = pusherBuilder;
    }

    @Override
    public Seed getSeed() throws IdobataError {
        Endpoint endpoint = new Endpoint("https://idobata.io/api/seed");
        Request request = new Request("GET", endpoint.build(), Collections.<Header> emptyList(), null);
        Response response = requestInterceptor.execute(client, request);
        try {
            return converter.convert(response.getBody(), Seed.class);
        } catch (Exception e) {
            throw new IdobataError(e);
        }
    }

    @Override
    public List<Message> getMessages(List<String> ids) throws IdobataError {
        Endpoint endpoint = new Endpoint("https://idobata.io/api/messages").addQuery("ids[]", ids);
        Request request = new Request("GET", endpoint.build(), Collections.<Header> emptyList(), null);
        Response response = requestInterceptor.execute(client, request);
        try {
            return converter.convert(response.getBody(), Message[].class);
        } catch (Exception e) {
            throw new IdobataError(e);
        }
    }

    @Override
    public List<Message> getMessages(long roomId, long olderThan) throws IdobataError {
        Endpoint endpoint = new Endpoint("https://idobata.io/api/messages").addQuery("room_id", roomId)
                                                                           .addQuery("older_than", olderThan);
        Request request = new Request("GET", endpoint.build(), Collections.<Header> emptyList(), null);
        Response response = requestInterceptor.execute(client, request);
        try {
            return converter.convert(response.getBody(), Message[].class);
        } catch (Exception e) {
            throw new IdobataError(e);
        }
    }

    @Override
    public Message postMessage(long roomId, String source) throws IdobataError {
        Endpoint endpoint = new Endpoint("https://idobata.io/api/messages");
        FormUrlEncodedTypedOutput body = new FormUrlEncodedTypedOutput().addField("message[room_id]", roomId)
                                                                        .addField("message[source]", source);
        Request request = new Request("POST", endpoint.build(), Collections.<Header> emptyList(), body);
        Response response = requestInterceptor.execute(client, request);
        try {
            return converter.convert(response.getBody(), Message.class);
        } catch (Exception e) {
            throw new IdobataError(e);
        }
    }

    @Override
    public Message postMessage(long roomId, String fileName, String contentType, InputStream content) throws IdobataError {
        Endpoint endpoint = new Endpoint("https://idobata.io/api/messages");
        MultipartTypedOutput body = new MultipartTypedOutput("----Idobata4JBoundary" + Long.toHexString(System.currentTimeMillis()));

        try {
            body.addFormField(new MultipartTypedOutput.TextInput("message[room_id]", Long.toString(roomId)))
                .addFormField(new MultipartTypedOutput.FileInput("message[images][]", fileName, contentType, content));
        } catch (IOException e) {
            throw new IdobataError(e);
        }

        Request request = new Request("POST", endpoint.build(), Collections.<Header> emptyList(), body);
        Response response = requestInterceptor.execute(client, request);
        try {
            return converter.convert(response.getBody(), Message.class);
        } catch (Exception e) {
            throw new IdobataError(e);
        }
    }

    @Override
    public Message deleteMessage(long messageId) throws IdobataError {
        Endpoint endpoint = new Endpoint("https://idobata.io/api/messages").addPath(messageId);
        Request request = new Request("DELETE", endpoint.build(), Collections.<Header> emptyList(), null);
        Response response = requestInterceptor.execute(client, request);
        try {
            return converter.convert(response.getBody(), Message.class);
        } catch (Exception e) {
            throw new IdobataError(e);
        }
    }

    @Override
    public List<User> getUsers(List<Long> ids) throws IdobataError {
        Endpoint endpoint = new Endpoint("https://idobata.io/api/users").addQuery("ids[]", ids);
        Request request = new Request("GET", endpoint.build(), Collections.<Header> emptyList(), null);
        Response response = requestInterceptor.execute(client, request);
        try {
            return converter.convert(response.getBody(), User[].class);
        } catch (Exception e) {
            throw new IdobataError(e);
        }
    }

    @Override
    public User getUser(long guyId) throws IdobataError {
        Endpoint endpoint = new Endpoint("https://idobata.io/api/users").addPath(guyId);
        Request request = new Request("GET", endpoint.build(), Collections.<Header> emptyList(), null);
        Response response = requestInterceptor.execute(client, request);
        try {
            return converter.convert(response.getBody(), User.class);
        } catch (Exception e) {
            throw new IdobataError(e);
        }
    }

    @Override
    public List<Room> getRooms(List<Long> ids) throws IdobataError {
        Endpoint endpoint = new Endpoint("https://idobata.io/api/rooms").addQuery("ids[]", ids);
        Request request = new Request("GET", endpoint.build(), Collections.<Header> emptyList(), null);
        Response response = requestInterceptor.execute(client, request);
        try {
            return converter.convert(response.getBody(), Room[].class);
        } catch (Exception e) {
            throw new IdobataError(e);
        }
    }

    @Override
    public Room getRoom(String organizationSlug, String roomName) throws IdobataError {
        Endpoint endpoint = new Endpoint("https://idobata.io/api/rooms").addQuery("organization_slug", organizationSlug)
                                                                        .addQuery("room_name", roomName);
        Request request = new Request("GET", endpoint.build(), Collections.<Header> emptyList(), null);
        Response response = requestInterceptor.execute(client, request);
        try {
            List<Room> rooms = converter.convert(response.getBody(), Room[].class);
            if (rooms != null && rooms.size() >= 1) {
                return rooms.get(0);
            }
            return null;
        } catch (Exception e) {
            throw new IdobataError(e);
        }
    }

    @Override
    public List<Room> getRooms(String organizationSlug, String roomName) throws IdobataError {
        Room room = getRoom(organizationSlug, roomName);
        return room != null ? Arrays.asList(room) : Collections.<Room> emptyList();
    }

    @Override
    public Room getRoom(long roomId) throws IdobataError {
        Endpoint endpoint = new Endpoint("https://idobata.io/api/rooms").addPath(roomId);
        Request request = new Request("GET", endpoint.build(), Collections.<Header> emptyList(), null);
        Response response = requestInterceptor.execute(client, request);
        try {
            return converter.convert(response.getBody(), Room.class);
        } catch (Exception e) {
            throw new IdobataError(e);
        }
    }

    @Override
    public List<Bot> getBots(List<Long> ids) throws IdobataError {
        Endpoint endpoint = new Endpoint("https://idobata.io/api/bots").addQuery("ids[]", ids);
        Request request = new Request("GET", endpoint.build(), Collections.<Header> emptyList(), null);
        Response response = requestInterceptor.execute(client, request);
        try {
            return converter.convert(response.getBody(), Bot[].class);
        } catch (Exception e) {
            throw new IdobataError(e);
        }
    }

    @Override
    public Bot postBot(long roomId, String botName) throws IdobataError {
        Endpoint endpoint = new Endpoint("https://idobata.io/api/rooms").addPath(Long.toString(roomId)).addPath("bots");
        FormUrlEncodedTypedOutput body = new FormUrlEncodedTypedOutput().addField("bot[name]", botName);
        Request request = new Request("POST", endpoint.build(), Collections.<Header> emptyList(), body);
        Response response = requestInterceptor.execute(client, request);
        try {
            return converter.convert(response.getBody(), Bot.class);
        } catch (Exception e) {
            throw new IdobataError(e);
        }
    }

    @Override
    public Bot postBot(String botName, long organizationId) throws IdobataError {
        Endpoint endpoint = new Endpoint("https://idobata.io/api/bots");
        FormUrlEncodedTypedOutput body = new FormUrlEncodedTypedOutput().addField("bot[name]", botName)
                                                                        .addField("bot[organization_id]", Long.toString(organizationId));
        Request request = new Request("POST", endpoint.build(), Collections.<Header> emptyList(), body);
        Response response = requestInterceptor.execute(client, request);
        try {
            return converter.convert(response.getBody(), Bot.class);
        } catch (Exception e) {
            throw new IdobataError(e);
        }

    }

    @Override
    public void postTouch(long roomId) throws IdobataError {
        Endpoint endpoint = new Endpoint("https://idobata.io/api/user/rooms").addPath(roomId)
                                                                             .addPath("touch");
        Request request = new Request("POST", endpoint.build(), Collections.<Header> emptyList(), null);
        requestInterceptor.execute(client, request);
    }

    @Override
    public String postPusherAuth(String channelName, String socketId) throws IdobataError {
        Endpoint endpoint = new Endpoint("https://idobata.io/pusher/auth");
        FormUrlEncodedTypedOutput body = new FormUrlEncodedTypedOutput().addField("channel_name", channelName)
                                                                        .addField("socket_id", socketId);
        Request request = new Request("POST", endpoint.build(), Collections.<Header> emptyList(), body);
        Response response = requestInterceptor.execute(client, request);
        try {
            return drain(response.getBody()
                                 .in());
        } catch (Exception e) {
            throw new IdobataError(e);
        }
    }

    //X-CSRF-Token:4oyP0NGX+9oa6zMS83UuSL7+uxpbQT6+hPuumQI4KQk=

    @Override
    public IdobataStream openStream() throws IdobataError {
        String channelName = getSeed().getRecords()
                                      .getUser()
                                      .getChannelName();
        Authorizer authorizer = new Authorizer() {
            @Override
            public String authorize(String channelName, String socketId) throws AuthorizationFailureException {
                try {
                    return postPusherAuth(channelName, socketId);
                } catch (IdobataError error) {
                    throw new AuthorizationFailureException(error);
                }
            }
        };
        PusherOptions options = new PusherOptions().setEncrypted(true)
                                                   .setAuthorizer(authorizer);
        Pusher pusher = pusherBuilder.buildUpon()
                                     .setOptions(options)
                                     .build();
        return new IdobataStreamImpl(this, pusher, channelName, converter);
    }

    private static String drain(InputStream in) throws IOException {
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int readBytes; (readBytes = in.read(buffer)) > -1;) {
            out.write(buffer, 0, readBytes);
        }
        in.close();
        return new String(out.toByteArray(), "UTF-8");
    }
}
