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

import com.uphyca.idobata.http.Client;
import com.uphyca.idobata.http.Header;
import com.uphyca.idobata.http.Request;
import com.uphyca.idobata.http.Response;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.*;

/**
 * Represents cookie authentication.
 * 
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class CookieAuthenticator implements RequestInterceptor {

    private static final String COOKIE = "Cookie";

    private final CookieHandler cookieHandler;

    private final AuthenticityTokenHandler authenticityTokenHandler;

    /**
     * @param cookieHandler The cookieHandler for authentication.
     */
    public CookieAuthenticator(CookieHandler cookieHandler) {
        this(cookieHandler, EMPTY_AUTHENTICITY_TOKEN_HANDLER);
    }

    /**
     * @param cookieHandler The cookieHandler for authentication.
     * @param authenticityTokenHandler authenticityTokenHandler for authentication.
     */
    public CookieAuthenticator(CookieHandler cookieHandler, AuthenticityTokenHandler authenticityTokenHandler) {
        this.cookieHandler = cookieHandler;
        this.authenticityTokenHandler = authenticityTokenHandler;
    }

    @Override
    public Response execute(Client client, Request request) throws IdobataError {
        try {
            Response response = executeInternal(client, request);
            int status = response.getStatus();
            if (status >= 500) {
                throw new HttpError(request.getUrl(), status, response.getReason());
            }
            if (status >= 400) {
                return authAndRetry(client, request);
            }
            return response;
        } catch (IOException e) {
            throw new IdobataError(e);
        }
    }

    private Response authAndRetry(Client client, Request request) throws IOException {
        Map<String, List<String>> requestHeaders = cookieHandler.get(URI.create(request.getUrl()), Collections.<String, List<String>> emptyMap());
        List<String> cookies = requestHeaders.get(COOKIE);
        List<Header> headers = new ArrayList<Header>();
        for (String each : cookies) {
            headers.add(new Header(COOKIE, each));
        }
        return executeInternal(client, new Request(request.getMethod(), request.getUrl(), headers, request.getBody()));
    }

    private Response executeInternal(Client client, Request request) throws IOException {
        String authenticityToken = authenticityTokenHandler.get();
        if (authenticityToken != null) {
            ArrayList<Header> requestHeaders = new ArrayList<Header>();
            if (request.getHeaders() != null) {
                requestHeaders.addAll(request.getHeaders());
            }
            requestHeaders.add(new Header("X-CSRF-Token", authenticityToken));
            request = new Request(request.getMethod(), request.getUrl(), requestHeaders, request.getBody());
        }
        Response response = client.execute(request);
        Map<String, List<String>> headers = headerListToMap(response.getHeaders());
        cookieHandler.put(URI.create(response.getUrl()), headers);
        return response;
    }

    private Map<String, List<String>> headerListToMap(List<Header> headers) {
        Map<String, List<String>> headerMap = new HashMap<String, List<String>>();
        for (Header each : headers) {
            String name = each.getName();
            List<String> values = headerMap.get(name);
            if (values == null) {
                values = new ArrayList<String>();
                headerMap.put(name, values);
            }
            values.add(each.getValue());
        }
        return headerMap;
    }

    private static final AuthenticityTokenHandler EMPTY_AUTHENTICITY_TOKEN_HANDLER = new AuthenticityTokenHandler() {
        @Override
        public String get() {
            return null;
        }

        @Override
        public void set(String authenticityToken) {
        }
    };
}
