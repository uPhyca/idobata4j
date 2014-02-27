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

import com.pusher.client.Pusher;
import com.uphyca.idobata.http.Client;
import com.uphyca.idobata.http.Request;
import com.uphyca.idobata.http.Response;
import com.uphyca.idobata.http.TypedInput;
import com.uphyca.idobata.model.*;
import com.uphyca.idobata.pusher.PusherBuilder;
import com.uphyca.idobata.transform.Converter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;

/**
 * @author Sosuke Masui (masui@uphyca.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class IdobataTest {

    Idobata underTest;

    @Mock
    Client client;

    @Mock
    RequestInterceptor requestInterceptor;

    @Mock
    Converter converter;

    @Mock
    Pusher pusher;

    PusherBuilder pusherBuilder = new PusherBuilder() {
        @Override
        public PusherBuilder buildUpon() {
            return this;
        }

        @Override
        public Pusher build() {
            return pusher;
        }
    };

    @Before
    public void setUp() throws Exception {
        underTest = new IdobataBuilder().setClient(client)
                                        .setRequestInterceptor(requestInterceptor)
                                        .setConverter(converter)
                                        .setPusherBuilder(pusherBuilder)
                                        .build();
    }

    @Test
    public void assertPreconditions() throws Exception {
        assertThat(client).isNotNull();
        assertThat(requestInterceptor).isNotNull();
        assertThat(converter).isNotNull();
        assertThat(underTest).isNotNull();
    }

    @Test
    public void getSeed() throws Exception {
        Response response = mock(Response.class);
        Seed expectedSeed = new SeedBean();
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        given(response.getBody()).willReturn(mock(TypedInput.class));
        given(requestInterceptor.execute(same(client), requestCaptor.capture())).willReturn(response);
        given(converter.convert(response.getBody(), Seed.class)).willReturn(expectedSeed);

        Seed actualSeed = underTest.getSeed();

        assertThat(actualSeed).isEqualTo(expectedSeed);
        Request actualRequest = requestCaptor.getValue();
        assertThat(actualRequest.getMethod()).isEqualTo("GET");
        assertThat(actualRequest.getUrl()).isEqualTo(new Endpoint("https://idobata.io/api/seed").build());
        assertThat(actualRequest.getHeaders()).isEmpty();
        assertThat(actualRequest.getBody()).isNull();
    }

    @Test
    public void getMessages() throws Exception {
        Response response = mock(Response.class);
        List<Message> expectedMessages = new ArrayList<Message>();
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        given(response.getBody()).willReturn(mock(TypedInput.class));
        given(requestInterceptor.execute(same(client), requestCaptor.capture())).willReturn(response);
        given(converter.convert(response.getBody(), Message[].class)).willReturn(expectedMessages);

        List<String> ids = Arrays.asList("1", "2", "3");
        List<Message> actualMessages = underTest.getMessages(ids);

        assertThat(actualMessages).isEqualTo(expectedMessages);
        Request actualRequest = requestCaptor.getValue();
        assertThat(actualRequest.getMethod()).isEqualTo("GET");
        assertThat(actualRequest.getUrl()).isEqualTo(new Endpoint("https://idobata.io/api/messages").addQuery("ids[]", ids)
                                                                                                    .build());
        assertThat(actualRequest.getHeaders()).isEmpty();
        assertThat(actualRequest.getBody()).isNull();
    }

    @Test
    public void getOlderMessages() throws Exception {
        Response response = mock(Response.class);
        List<Message> expectedMessages = new ArrayList<Message>();
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        given(response.getBody()).willReturn(mock(TypedInput.class));
        given(requestInterceptor.execute(same(client), requestCaptor.capture())).willReturn(response);
        given(converter.convert(response.getBody(), Message[].class)).willReturn(expectedMessages);

        long roomId = 1L;
        long olderThan = 2L;
        List<Message> actualMessages = underTest.getMessages(roomId, olderThan);

        assertThat(actualMessages).isEqualTo(expectedMessages);
        Request actualRequest = requestCaptor.getValue();
        assertThat(actualRequest.getMethod()).isEqualTo("GET");
        assertThat(actualRequest.getUrl()).isEqualTo(new Endpoint("https://idobata.io/api/messages").addQuery("room_id", roomId)
                                                                                                    .addQuery("older_than", 2)
                                                                                                    .build());
        assertThat(actualRequest.getHeaders()).isEmpty();
        assertThat(actualRequest.getBody()).isNull();
    }

    @Test
    public void postMessage() throws Exception {
        Response response = mock(Response.class);
        Message expectedMessage = new MessageBean();
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        given(response.getBody()).willReturn(mock(TypedInput.class));
        given(requestInterceptor.execute(same(client), requestCaptor.capture())).willReturn(response);
        given(converter.convert(response.getBody(), Message.class)).willReturn(expectedMessage);

        long roomId = 1L;
        String source = "foo";
        Message actualMessage = underTest.postMessage(roomId, source);

        assertThat(actualMessage).isEqualTo(expectedMessage);
        Request actualRequest = requestCaptor.getValue();
        assertThat(actualRequest.getMethod()).isEqualTo("POST");
        assertThat(actualRequest.getUrl()).isEqualTo(new Endpoint("https://idobata.io/api/messages").build());
        assertThat(actualRequest.getHeaders()).isEmpty();
        assertThat(actualRequest.getBody()).isNotNull();
        ByteArrayOutputStream actualPostBody = new ByteArrayOutputStream();
        actualRequest.getBody()
                     .writeTo(actualPostBody);
        String expectedPostBody = new StringBuilder().append(URLEncoder.encode("message[room_id]", "UTF-8"))
                                                     .append('=')
                                                     .append(Long.toString(roomId))
                                                     .append('&')
                                                     .append(URLEncoder.encode("message[source]", "UTF-8"))
                                                     .append('=')
                                                     .append(source)
                                                     .toString();
        assertThat(actualPostBody.toString("UTF-8")).isEqualTo(expectedPostBody);
    }

    @Test
    public void postImage() throws Exception {
        Response response = mock(Response.class);
        Message expectedMessage = new MessageBean();
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        given(response.getBody()).willReturn(mock(TypedInput.class));
        given(requestInterceptor.execute(same(client), requestCaptor.capture())).willReturn(response);
        given(converter.convert(response.getBody(), Message.class)).willReturn(expectedMessage);

        long roomId = 1L;
        String fileName = "idobata.png";
        String contentType = "image/png";
        InputStream content = new ByteArrayInputStream("a".getBytes("UTF-8"));
        Message actualMessage = underTest.postMessage(roomId, fileName, contentType, content);

        assertThat(actualMessage).isEqualTo(expectedMessage);
        Request actualRequest = requestCaptor.getValue();
        assertThat(actualRequest.getMethod()).isEqualTo("POST");
        assertThat(actualRequest.getUrl()).isEqualTo(new Endpoint("https://idobata.io/api/messages").build());
        assertThat(actualRequest.getHeaders()).isEmpty();
        assertThat(actualRequest.getBody()).isNotNull();
        ByteArrayOutputStream actualPostBody = new ByteArrayOutputStream();
        actualRequest.getBody()
                     .writeTo(actualPostBody);
        String actualMimeType = actualRequest.getBody()
                                             .mimeType();
        String actualBoundary = actualMimeType.split("boundary=")[1];
        String expectedPostBody = new StringBuilder().append("--" + actualBoundary + "\r\n")
                                                     .append("Content-Disposition: form-data; name=\"message[room_id]\"\r\n")
                                                     .append("\r\n")
                                                     .append("1\r\n")
                                                     .append("--" + actualBoundary + "\r\n")
                                                     .append("Content-Disposition: form-data; name=\"message[images][]\"; filename=\"idobata.png\"\r\n")
                                                     .append("Content-Type: image/png\r\n")
                                                     .append("\r\n")
                                                     .append("a\r\n")
                                                     .append("--" + actualBoundary + "--")
                                                     .toString();

        assertThat(actualPostBody.toString("UTF-8")).isEqualTo(expectedPostBody);
    }

    @Test
    public void deleteMessage() throws Exception {
        Response response = mock(Response.class);
        Message expectedMessage = new MessageBean();
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        given(response.getBody()).willReturn(mock(TypedInput.class));
        given(requestInterceptor.execute(same(client), requestCaptor.capture())).willReturn(response);
        given(converter.convert(response.getBody(), Message.class)).willReturn(expectedMessage);

        long messageId = 1L;
        Message actualMessage = underTest.deleteMessage(messageId);

        assertThat(actualMessage).isEqualTo(expectedMessage);
        Request actualRequest = requestCaptor.getValue();
        assertThat(actualRequest.getMethod()).isEqualTo("DELETE");
        assertThat(actualRequest.getUrl()).isEqualTo(new Endpoint("https://idobata.io/api/messages").addPath(messageId)
                                                                                                    .build());
        assertThat(actualRequest.getHeaders()).isEmpty();
        assertThat(actualRequest.getBody()).isNull();
    }

    @Test
    public void getUsers() throws Exception {
        Response response = mock(Response.class);
        List<User> expectedUsers = new ArrayList<User>();
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        given(response.getBody()).willReturn(mock(TypedInput.class));
        given(requestInterceptor.execute(same(client), requestCaptor.capture())).willReturn(response);
        given(converter.convert(response.getBody(), User[].class)).willReturn(expectedUsers);

        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<User> actualUsers = underTest.getUsers(ids);

        assertThat(actualUsers).isEqualTo(expectedUsers);
        Request actualRequest = requestCaptor.getValue();
        assertThat(actualRequest.getMethod()).isEqualTo("GET");
        assertThat(actualRequest.getUrl()).isEqualTo(new Endpoint("https://idobata.io/api/users").addQuery("ids[]", ids)
                                                                                                 .build());
        assertThat(actualRequest.getHeaders()).isEmpty();
        assertThat(actualRequest.getBody()).isNull();
    }

    @Test
    public void getUser() throws Exception {
        Response response = mock(Response.class);
        User expectedUser = new UserBean();
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        given(response.getBody()).willReturn(mock(TypedInput.class));
        given(requestInterceptor.execute(same(client), requestCaptor.capture())).willReturn(response);
        given(converter.convert(response.getBody(), User.class)).willReturn(expectedUser);

        long guyId = 1L;
        User actualUser = underTest.getUser(guyId);

        assertThat(actualUser).isEqualTo(expectedUser);
        Request actualRequest = requestCaptor.getValue();
        assertThat(actualRequest.getMethod()).isEqualTo("GET");
        assertThat(actualRequest.getUrl()).isEqualTo(new Endpoint("https://idobata.io/api/users").addPath(guyId)
                                                                                                 .build());
        assertThat(actualRequest.getHeaders()).isEmpty();
        assertThat(actualRequest.getBody()).isNull();
    }

    @Test
    public void getSpecificRooms() throws Exception {
        Response response = mock(Response.class);
        List<Room> expectedRooms = new ArrayList<Room>();
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        given(response.getBody()).willReturn(mock(TypedInput.class));
        given(requestInterceptor.execute(same(client), requestCaptor.capture())).willReturn(response);
        given(converter.convert(response.getBody(), Room[].class)).willReturn(expectedRooms);

        String organizationSlug = "foo";
        String roomName = "bar";
        List<Room> actualRooms = underTest.getRooms(organizationSlug, roomName);

        assertThat(actualRooms).isEqualTo(expectedRooms);
        Request actualRequest = requestCaptor.getValue();
        assertThat(actualRequest.getMethod()).isEqualTo("GET");
        assertThat(actualRequest.getUrl()).isEqualTo(new Endpoint("https://idobata.io/api/rooms").addQuery("organization_slug", organizationSlug)
                                                                                                 .addQuery("room_name", roomName)
                                                                                                 .build());
        assertThat(actualRequest.getHeaders()).isEmpty();
        assertThat(actualRequest.getBody()).isNull();
    }

    @Test
    public void getRooms() throws Exception {
        Response response = mock(Response.class);
        List<Room> expectedRooms = new ArrayList<Room>();
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        given(response.getBody()).willReturn(mock(TypedInput.class));
        given(requestInterceptor.execute(same(client), requestCaptor.capture())).willReturn(response);
        given(converter.convert(response.getBody(), Room[].class)).willReturn(expectedRooms);

        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<Room> actualRooms = underTest.getRooms(ids);

        assertThat(actualRooms).isEqualTo(expectedRooms);
        Request actualRequest = requestCaptor.getValue();
        assertThat(actualRequest.getMethod()).isEqualTo("GET");
        assertThat(actualRequest.getUrl()).isEqualTo(new Endpoint("https://idobata.io/api/rooms").addQuery("ids[]", ids)
                                                                                                 .build());
        assertThat(actualRequest.getHeaders()).isEmpty();
        assertThat(actualRequest.getBody()).isNull();
    }

    @Test
    public void getRoom() throws Exception {
        Response response = mock(Response.class);
        Room expectedUser = new RoomBean();
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        given(response.getBody()).willReturn(mock(TypedInput.class));
        given(requestInterceptor.execute(same(client), requestCaptor.capture())).willReturn(response);
        given(converter.convert(response.getBody(), Room.class)).willReturn(expectedUser);

        long roomId = 1L;
        Room actualRoom = underTest.getRoom(roomId);

        assertThat(actualRoom).isEqualTo(expectedUser);
        Request actualRequest = requestCaptor.getValue();
        assertThat(actualRequest.getMethod()).isEqualTo("GET");
        assertThat(actualRequest.getUrl()).isEqualTo(new Endpoint("https://idobata.io/api/rooms").addPath(roomId)
                                                                                                 .build());
        assertThat(actualRequest.getHeaders()).isEmpty();
        assertThat(actualRequest.getBody()).isNull();
    }

    @Test
    public void getBots() throws Exception {
        Response response = mock(Response.class);
        List<Bot> expectedBots = new ArrayList<Bot>();
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        given(response.getBody()).willReturn(mock(TypedInput.class));
        given(requestInterceptor.execute(same(client), requestCaptor.capture())).willReturn(response);
        given(converter.convert(response.getBody(), Bot[].class)).willReturn(expectedBots);

        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<Bot> actualBots = underTest.getBots(ids);

        assertThat(actualBots).isEqualTo(expectedBots);
        Request actualRequest = requestCaptor.getValue();
        assertThat(actualRequest.getMethod()).isEqualTo("GET");
        assertThat(actualRequest.getUrl()).isEqualTo(new Endpoint("https://idobata.io/api/bots").addQuery("ids[]", ids)
                                                                                                .build());
        assertThat(actualRequest.getHeaders()).isEmpty();
        assertThat(actualRequest.getBody()).isNull();
    }

    @Test
    public void testPostTouch() throws Exception {
        Response response = mock(Response.class);
        Message expectedMessage = new MessageBean();
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);

        given(response.getBody()).willReturn(mock(TypedInput.class));
        given(requestInterceptor.execute(same(client), requestCaptor.capture())).willReturn(response);
        given(converter.convert(response.getBody(), Message.class)).willReturn(expectedMessage);

        long roomId = 1L;
        underTest.postTouch(roomId);

        Request actualRequest = requestCaptor.getValue();
        assertThat(actualRequest.getMethod()).isEqualTo("POST");
        assertThat(actualRequest.getUrl()).isEqualTo(new Endpoint("https://idobata.io/api/user/rooms/").addPath(roomId)
                                                                                                       .addPath("touch")
                                                                                                       .build());
        assertThat(actualRequest.getHeaders()).isEmpty();
        assertThat(actualRequest.getBody()).isNull();
    }

    @Test
    public void postPusherAuth() throws Exception {
        Response response = mock(Response.class);
        String expectedString = "abc";
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        TypedInput in = mock(TypedInput.class);
        given(in.in()).willReturn(new ByteArrayInputStream(expectedString.getBytes("UTF-8")));
        given(response.getBody()).willReturn(in);
        given(requestInterceptor.execute(same(client), requestCaptor.capture())).willReturn(response);

        String channelName = "foo";
        String socketId = "bar";
        String actualString = underTest.postPusherAuth(channelName, socketId);

        assertThat(actualString).isEqualTo(expectedString);
        Request actualRequest = requestCaptor.getValue();
        assertThat(actualRequest.getMethod()).isEqualTo("POST");
        assertThat(actualRequest.getUrl()).isEqualTo(new Endpoint("https://idobata.io/pusher/auth").build());
        assertThat(actualRequest.getHeaders()).isEmpty();
        assertThat(actualRequest.getBody()).isNotNull();
        ByteArrayOutputStream actualPostBody = new ByteArrayOutputStream();
        actualRequest.getBody()
                     .writeTo(actualPostBody);
        String expectedPostBody = new StringBuilder().append(URLEncoder.encode("channel_name", "UTF-8"))
                                                     .append('=')
                                                     .append(channelName)
                                                     .append('&')
                                                     .append(URLEncoder.encode("socket_id", "UTF-8"))
                                                     .append('=')
                                                     .append(socketId)
                                                     .toString();
        assertThat(actualPostBody.toString("UTF-8")).isEqualTo(expectedPostBody);
    }

    @Test
    public void openStream() throws Exception {
        underTest = spy(underTest);
        Seed seed = new SeedBean();
        Records records = new RecordsBean();
        User user = new UserBean();
        user.setChannelName("space");
        seed.setRecords(records);
        records.setUser(user);
        doReturn(seed).when(underTest)
                      .getSeed();

        IdobataStream actualStream = underTest.openStream();

        assertThat(actualStream).isNotNull();
    }
}
