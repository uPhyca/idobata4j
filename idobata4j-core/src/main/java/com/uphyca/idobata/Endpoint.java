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

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Helper class for building URI string.
 * 
 * @author Sosuke Masui (masui@uphyca.com)
 */
class Endpoint {

    private static final Charset ENCODE_CHARSET = Charset.forName("UTF-8");

    private final StringBuilder queryString = new StringBuilder();
    private final StringBuilder url;

    public Endpoint(String url) {
        this.url = new StringBuilder(url);
    }

    /**
     * Encodes the given segment and appends it to the path.
     */
    public Endpoint addPath(String segment) {
        if (segment.startsWith("/")) {
            if (endsWithDelimiter(url)) {
                url.append(segment.substring(1));
            } else {
                url.append(segment);
            }
        } else {
            if (!endsWithDelimiter(url)) {
                url.append('/');
            }
            url.append(segment);
        }
        return this;
    }

    /**
     * Encodes the given segment and appends it to the path.
     */
    public Endpoint addPath(long path) {
        return addPath(Long.toString(path));
    }

    /**
     * Encodes the key and value and then appends the parameter to the query string.
     */
    public Endpoint addQuery(String name, String value) {
        if (queryString.length() > 0) {
            queryString.append('&');
        }

        try {
            queryString.append(URLEncoder.encode(name, ENCODE_CHARSET.name()));
            queryString.append('=');
            queryString.append(URLEncoder.encode(value, ENCODE_CHARSET.name()));
            return this;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Encodes the key and value and then appends the parameter to the query string.
     */
    public Endpoint addQuery(String name, long value) {
        return addQuery(name, Long.toString(value));
    }

    /**
     * Encodes the key and value and then appends the parameter to the query string.
     */
    public Endpoint addQuery(String name, List<?> values) {
        for (Object each : values) {
            addQuery(name, String.valueOf(each));
        }
        return this;
    }

    /**
     * Constructs a Uri with the current attributes.
     */
    public String build() {
        if (queryString.length() == 0) {
            return url.toString();
        }
        return url + "?" + queryString;
    }

    private static boolean endsWithDelimiter(CharSequence seq) {
        return seq.charAt(seq.length() - 1) == '/';
    }
}
