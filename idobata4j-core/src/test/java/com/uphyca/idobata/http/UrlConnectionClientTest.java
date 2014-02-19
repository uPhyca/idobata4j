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

package com.uphyca.idobata.http;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.spy;
import static org.mockito.Mockito.*;

/**
 * @author Sosuke Masui (masui@uphyca.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class UrlConnectionClientTest {

    private static final String URL = "http://example.com";

    @Spy
    UrlConnectionClient underTest;

    @Mock
    HttpURLConnection connection;

    @Mock
    CookieHandler cookieHandler;

    ByteArrayInputStream input;

    ByteArrayOutputStream output;

    ArgumentCaptor<Request> requestCaptor;

    @Before
    public void setUp() throws Exception {
        requestCaptor = ArgumentCaptor.forClass(Request.class);

        underTest = spy(new UrlConnectionClient(cookieHandler));
        doReturn(connection).when(underTest)
                            .openConnection(requestCaptor.capture());

        given(connection.getInputStream()).willReturn(input = new ByteArrayInputStream(new byte[0]));
        given(connection.getErrorStream()).willReturn(input = new ByteArrayInputStream(new byte[0]));
        given(connection.getOutputStream()).willReturn(output = new ByteArrayOutputStream());
    }

    @Test
    public void get() throws Exception {
        given(connection.getURL()).willReturn(new URL(URL));
        given(connection.getResponseCode()).willReturn(200);
        given(connection.getResponseMessage()).willReturn("OK");
        Map<String, List<String>> responseHeaderFields = new LinkedHashMap<String, List<String>>();
        responseHeaderFields.put("Set-Cookie", Arrays.asList("aaa"));
        given(connection.getHeaderFields()).willReturn(responseHeaderFields);

        Request request = new Request("GET", URL, Arrays.asList(new Header("Hoge", "Piyo")), null);

        Response response = underTest.execute(request);

        verify(connection).setRequestMethod("GET");
        verify(connection).addRequestProperty("Hoge", "Piyo");
        verify(connection, never()).getOutputStream();

        assertThat(response.getUrl()).isEqualTo(URL);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getReason()).isEqualTo("OK");
        assertThat(response.getHeaders()).isEqualTo(Arrays.asList(new Header("Set-Cookie", "aaa")));
    }

    @Test
    public void post() throws Exception {
        given(connection.getURL()).willReturn(new URL(URL));
        given(connection.getResponseCode()).willReturn(200);
        given(connection.getResponseMessage()).willReturn("OK");
        Map<String, List<String>> responseHeaderFields = new LinkedHashMap<String, List<String>>();
        responseHeaderFields.put("Set-Cookie", Arrays.asList("aaa"));
        given(connection.getHeaderFields()).willReturn(responseHeaderFields);

        Request request = new Request("POST", URL, Arrays.asList(new Header("Hoge", "Piyo")), new FormUrlEncodedTypedOutput().addField("foo", "bar"));

        Response response = underTest.execute(request);

        verify(connection).setRequestMethod("POST");
        verify(connection).addRequestProperty("Hoge", "Piyo");
        verify(connection).getOutputStream();

        assertThat(response.getUrl()).isEqualTo(URL);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getReason()).isEqualTo("OK");
        assertThat(response.getHeaders()).isEqualTo(Arrays.asList(new Header("Set-Cookie", "aaa")));
        assertThat(output.toString("UTF-8")).isEqualTo("foo=bar");
    }

    @Test
    public void postAndGet() throws Exception {
        given(connection.getURL()).willReturn(new URL(URL), new URL(URL + "/secret"));
        given(connection.getResponseCode()).willReturn(302, 200);
        Map<String, List<String>> responseHeaderFields = new LinkedHashMap<String, List<String>>();
        responseHeaderFields.put("Location", Arrays.asList("http://example.com/secret"));
        given(connection.getHeaderFields()).willReturn(responseHeaderFields, Collections.<String, List<String>> emptyMap());

        Request request = new Request("POST", URL, Collections.<Header> emptyList(), null);

        Response response = underTest.execute(request);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getUrl()).isEqualTo(URL + "/secret");
    }

    @Test
    public void errorResponseThrowsHttpError() throws Exception {
        given(connection.getURL()).willReturn(new URL(URL));
        given(connection.getResponseCode()).willReturn(400);
        Map<String, List<String>> responseHeaderFields = new LinkedHashMap<String, List<String>>();
        given(connection.getHeaderFields()).willReturn(responseHeaderFields, Collections.<String, List<String>> emptyMap());

        Request request = new Request("GET", URL, Collections.<Header> emptyList(), null);

        Response response = underTest.execute(request);

        assertThat(response.getStatus()).isEqualTo(400);
    }
}
