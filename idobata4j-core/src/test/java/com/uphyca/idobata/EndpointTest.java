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

import org.junit.Test;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class EndpointTest {

    @Test
    public void noQueryString() throws Exception {
        assertThat(new Endpoint("http://example.com").build()).isEqualTo("http://example.com");
    }

    @Test
    public void addPathWithString() throws Exception {
        assertThat(new Endpoint("http://example.com").addPath("foo")
                                                     .build()).isEqualTo("http://example.com/foo");
    }

    @Test
    public void addPathWithSlash() throws Exception {
        assertThat(new Endpoint("http://example.com").addPath("/foo")
                                                     .build()).isEqualTo("http://example.com/foo");
    }

    @Test
    public void addPathWithTrailingSlash() throws Exception {
        assertThat(new Endpoint("http://example.com/").addPath("/foo")
                                                      .build()).isEqualTo("http://example.com/foo");
    }

    @Test
    public void addPathWithLong() throws Exception {
        assertThat(new Endpoint("http://example.com").addPath(1L)
                                                     .build()).isEqualTo("http://example.com/1");
    }

    @Test
    public void addQueryWithString() throws Exception {
        assertThat(new Endpoint("http://example.com").addQuery("foo", "bar")
                                                     .build()).isEqualTo("http://example.com?foo=bar");
    }

    @Test
    public void addQueryWithMultipleString() throws Exception {
        assertThat(new Endpoint("http://example.com").addQuery("foo", "bar")
                                                     .addQuery("hoge", "piyo")
                                                     .build()).isEqualTo("http://example.com?foo=bar&hoge=piyo");
    }

    @Test
    public void addQueryWithLong() throws Exception {
        assertThat(new Endpoint("http://example.com").addQuery("foo", 1L)
                                                     .build()).isEqualTo("http://example.com?foo=1");
    }

    @Test
    public void addQueryWithList() throws Exception {
        List<String> list = Arrays.asList(new String[] {
                "foo", "bar", "buz"
        });
        assertThat(new Endpoint("http://example.com").addQuery("meta", list)
                                                     .build()).isEqualTo("http://example.com?meta=foo&meta=bar&meta=buz");
    }

    @Test
    public void queryMustBeEncoded() throws Exception {
        assertThat(new Endpoint("http://example.com").addQuery("[foo]", "[bar]")
                                                     .build()).isEqualTo(String.format("http://example.com?%s=%s", URLEncoder.encode("[foo]", "UTF-8"), URLEncoder.encode("[bar]", "UTF-8")));
    }
}
