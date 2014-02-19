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
import com.uphyca.idobata.http.UrlConnectionClient;
import com.uphyca.idobata.transform.Converter;
import com.uphyca.idobata.transform.JSONConverter;

/**
 * Build a new {@link Idobata}
 * <p>
 * Calling the following method is required before calling {@link #build()}
 * <ul>
 * <li>{@link #setRequestInterceptor(RequestInterceptor)}</li>
 * </ul>
 * </p>
 * 
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class IdobataBuilder {

    private Client client;

    private RequestInterceptor requestInterceptor;

    private Converter converter;

    /**
     * The HTTP client used for requests.
     */
    public IdobataBuilder setClient(Client client) {
        this.client = client;
        return this;
    }

    /**
     * A request interceptor for executing HTTP client every request.
     */
    public IdobataBuilder setRequestInterceptor(RequestInterceptor requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
        return this;
    }

    /**
     * The converter used for deserialization of objects.
     */
    public IdobataBuilder setConverter(Converter converter) {
        this.converter = converter;
        return this;
    }

    /**
     * Create the {@link Idobata} instances.
     */
    public Idobata build() {
        if (requestInterceptor == null) {
            throw new IllegalArgumentException("RequestInterceptor may not be null.");
        }

        ensureDefaults();
        return new IdobataImpl(client, requestInterceptor, converter);
    }

    private void ensureDefaults() {
        if (client == null) {
            client = new UrlConnectionClient();
        }
        if (converter == null) {
            converter = new JSONConverter();
        }
    }
}
