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
import java.util.ArrayList;
import java.util.List;

/**
 * Represents token authentication.
 * 
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class TokenAuthenticator implements RequestInterceptor {

    private final String token;

    /**
     * @param token The token for authentication.
     */
    public TokenAuthenticator(String token) {
        this.token = token;
    }

    @Override
    public Response execute(Client client, Request request) throws IdobataError {
        List<Header> requestHeaders = new ArrayList<Header>(request.getHeaders());
        requestHeaders.add(new Header("X-API-Token", token));
        try {
            Response response = client.execute(new Request(request.getMethod(), request.getUrl(), requestHeaders, request.getBody()));
            int status = response.getStatus();
            if (status >= 400) {
                throw new HttpError(request.getUrl(), status, response.getReason());
            }
            return response;
        } catch (IOException e) {
            throw new IdobataError(e);
        }
    }
}
