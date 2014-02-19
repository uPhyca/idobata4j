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

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTTP client that uses {@link HttpURLConnection} for communication.
 * 
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class UrlConnectionClient implements Client {

    private static final int CHUNK_SIZE = 4096;

    private static final CookieHandler DEFAULT_COOKIE_HANDLER = new CookieHandlerDelegate(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    private final CookieHandler cookieHandler;

    public UrlConnectionClient() {
        this(DEFAULT_COOKIE_HANDLER);
    }

    public UrlConnectionClient(CookieHandler cookieHandler) {
        if (cookieHandler == null) {
            throw new NullPointerException("cookieHandler must not be null.");
        }
        this.cookieHandler = cookieHandler;
    }

    @Override
    public Response execute(Request request) throws IOException {

        HttpURLConnection connection = openConnection(request);
        prepareRequest(connection, request);
        Response response = readResponse(connection);
        return followRedirectsIfNecessary(request, response);
    }

    protected HttpURLConnection openConnection(Request request) throws IOException {
        return (HttpURLConnection) URI.create(request.getUrl())
                                      .toURL()
                                      .openConnection();
    }

    private Response followRedirectsIfNecessary(Request originalRequest, Response originalResponse) throws IOException {
        int status = originalResponse.getStatus();
        if (status < 300 || status >= 400) {
            return originalResponse;
        }

        String movedLocation = extractLocation(originalResponse.getHeaders());
        Request request = new Request("GET", movedLocation, originalRequest.getHeaders(), null);
        Response response = execute(request);
        return followRedirectsIfNecessary(originalRequest, response);
    }

    private void prepareRequest(HttpURLConnection connection, Request request) throws IOException {

        //Disable follow redirects feature to captures cookies for keeping http session.
        connection.setInstanceFollowRedirects(false);

        connection.setRequestMethod(request.getMethod());
        connection.setDoInput(true);
        copyHeadersToConnection(connection, request.getHeaders());
        Map<String, List<String>> cookieHeader = cookieHandler.get(URI.create(request.getUrl()), Collections.<String, List<String>> emptyMap());
        copyHeadersToConnection(connection, cookieHeader);

        TypedOutput body = request.getBody();
        if (body == null) {
            return;
        }
        connection.setDoOutput(true);
        connection.addRequestProperty("Content-Type", body.mimeType());
        long length = body.length();
        if (length != -1) {
            connection.setFixedLengthStreamingMode(length);
            connection.addRequestProperty("Content-Length", String.valueOf(length));
        } else {
            connection.setChunkedStreamingMode(CHUNK_SIZE);
        }
        body.writeTo(connection.getOutputStream());
    }

    private Response readResponse(HttpURLConnection connection) throws IOException {
        String url = connection.getURL()
                               .toString();
        int status = connection.getResponseCode();
        String reason = connection.getResponseMessage();
        List<Header> headers = copyHeaders(connection.getHeaderFields());

        cookieHandler.put(URI.create(url), connection.getHeaderFields());

        String mimeType = connection.getContentType();
        int length = connection.getContentLength();
        final InputStream stream;
        if (status < 400) {
            stream = connection.getInputStream();
        } else {
            stream = connection.getErrorStream();
        }
        TypedInputStream body = new TypedInputStream(mimeType, length, stream);
        return new Response(url, status, reason, headers, body);
    }

    private static String extractLocation(List<Header> headers) {
        for (Header header : headers) {
            if (header.getName()
                      .equalsIgnoreCase("Location")) {
                return header.getValue();
            }
        }
        return null;
    }

    private static void copyHeadersToConnection(HttpURLConnection connection, Map<String, List<String>> headers) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String name = entry.getKey();
            for (String value : entry.getValue()) {
                connection.addRequestProperty(name, value);
            }
        }
    }

    private static void copyHeadersToConnection(HttpURLConnection connection, List<Header> headers) {
        for (Header header : headers) {
            connection.addRequestProperty(header.getName(), header.getValue());
        }
    }

    private static List<Header> copyHeaders(Map<String, List<String>> src) {
        List<Header> headers = new ArrayList<Header>();
        for (Map.Entry<String, List<String>> entry : src.entrySet()) {
            String name = entry.getKey();
            if (name == null) {
                continue;
            }
            for (String value : entry.getValue()) {
                headers.add(new Header(name, value));
            }
        }
        return headers;
    }

    private static final class TypedInputStream implements TypedInput {

        private final String mimeType;
        private final long length;
        private final InputStream stream;

        private TypedInputStream(String mimeType, long length, InputStream stream) {
            this.mimeType = mimeType;
            this.length = length;
            this.stream = stream;
        }

        @Override
        public String mimeType() {
            return mimeType;
        }

        @Override
        public long length() {
            return length;
        }

        @Override
        public InputStream in() throws IOException {
            return stream;
        }
    }

    private static final class CookieHandlerDelegate extends CookieHandler {

        private static final Pattern EXPIRES_PATTERN = Pattern.compile("(expires=[a-zA-Z]{3}, \\d{1,2} [a-zA-Z]{3} \\d{4} \\d{2}:\\d{2}:\\d{2}) -0000;");

        private final CookieHandler delegate;

        private CookieHandlerDelegate(CookieHandler delegate) {
            this.delegate = delegate;
        }

        @Override
        public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
            return delegate.get(uri, requestHeaders);
        }

        @Override
        public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
            if (responseHeaders != null) {
                Map<String, List<String>> copyHeaders = new HashMap<String, List<String>>(responseHeaders);
                responseHeaders = copyHeaders;
                for (String headerKey : responseHeaders.keySet()) {
                    if (headerKey == null || !(headerKey.equalsIgnoreCase("Set-Cookie2") || headerKey.equalsIgnoreCase("Set-Cookie"))) {
                        continue;
                    }
                    List<String> newHeaderValues = new ArrayList<String>();
                    for (String headerValue : responseHeaders.get(headerKey)) {
                        Matcher mt = EXPIRES_PATTERN.matcher(headerValue);
                        if (mt.find()) {
                            newHeaderValues.add(mt.replaceFirst("$1 GMT;"));
                        } else {
                            newHeaderValues.add(headerValue);
                        }
                    }
                    responseHeaders.put(headerKey, newHeaderValues);
                }
            }
            delegate.put(uri, responseHeaders);
        }
    }
}
