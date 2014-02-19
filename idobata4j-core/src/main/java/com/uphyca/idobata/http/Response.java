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

import java.util.List;

/**
 * An HTTP response.
 * 
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class Response {

    private final String url;
    private final int status;
    private final String reason;
    private final List<Header> headers;
    private final TypedInput body;

    public Response(String url, int status, String reason, List<Header> headers, TypedInput body) {
        this.url = url;
        this.status = status;
        this.reason = reason;
        this.headers = headers;
        this.body = body;
    }

    /** Request URL. */
    public String getUrl() {
        return url;
    }

    /** Status line code. */
    public int getStatus() {
        return status;
    }

    /** Status line reason phrase. */
    public String getReason() {
        return reason;
    }

    /** An unmodifiable collection of headers. */
    public List<Header> getHeaders() {
        return headers;
    }

    /** Response body. May be {@code null}. */
    public TypedInput getBody() {
        return body;
    }
}
