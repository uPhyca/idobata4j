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

package com.uphyca.idobata.pusher;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;

/**
 * Build a new {@link com.pusher.client.Pusher}
 * 
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class PusherBuilder {

    private static final String API_KEY = "44ffe67af1c7035be764";

    private PusherOptions options;

    /** Options for the Pusher client library to use. */
    public PusherBuilder setOptions(PusherOptions options) {
        this.options = options;
        return this;
    }

    public PusherBuilder buildUpon() {
        return new PusherBuilder().setOptions(options);
    }

    /**
     * Create the {@link com.pusher.client.Pusher} instances.
     */
    public Pusher build() {
        if (options == null) {
            return new Pusher(API_KEY);
        }
        return new Pusher(API_KEY, options);
    }
}
