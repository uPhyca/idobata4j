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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * Represents an encoded form data.
 *
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class FormUrlEncodedTypedOutput implements TypedOutput {

    private static final String MIME_TYPE = "application/x-www-form-urlencoded; charset=UTF-8";

    private static final Charset ENCODE_CHARSET = Charset.forName("UTF-8");

    private final ByteArrayOutputStream content = new ByteArrayOutputStream();

    public FormUrlEncodedTypedOutput addField(String name, String value) {
        if (content.size() > 0) {
            content.write('&');
        }

        try {
            content.write(URLEncoder.encode(name, ENCODE_CHARSET.name())
                                    .getBytes(ENCODE_CHARSET));
            content.write('=');
            content.write(URLEncoder.encode(value, ENCODE_CHARSET.name())
                                    .getBytes(ENCODE_CHARSET));
            return this;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public FormUrlEncodedTypedOutput addField(String name, long value) {
        return addField(name, Long.toString(value));
    }

    @Override
    public String mimeType() {
        return MIME_TYPE;
    }

    @Override
    public long length() {
        return content.size();
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        out.write(content.toByteArray());
    }
}
