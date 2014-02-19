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

/**
 * Intercepts calls on HTTP requests.
 * The user should implement the {@link #execute(com.uphyca.idobata.http.Client, com.uphyca.idobata.http.Request)} method to modify the original behavior.
 * 
 * @author Sosuke Masui (masui@uphyca.com)
 */
public interface RequestInterceptor {

    /**
     * Implement this method to perform extra treatments before and after the invocation.
     * Polite implementations would certainly like to invoke {@link com.uphyca.idobata.http.Client#execute(com.uphyca.idobata.http.Request)}.
     */
    Response execute(Client client, Request request) throws IdobataError;
}
