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
import com.uphyca.idobata.http.Request;
import com.uphyca.idobata.http.Response;

import java.io.IOException;

/**
 * Represents cookie authentication.
 * 
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class CookieAuthenticator implements RequestInterceptor {

    @Override
    public Response execute(Client client, Request request) throws IdobataError {
        try {
            return client.execute(request);
        } catch (IOException e) {
            throw new IdobataError(e);
        }
    }
}
