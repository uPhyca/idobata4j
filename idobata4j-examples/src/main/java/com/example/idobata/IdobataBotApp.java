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

package com.example.idobata;

import com.uphyca.idobata.Idobata;
import com.uphyca.idobata.IdobataBuilder;
import com.uphyca.idobata.TokenAuthenticator;
import com.uphyca.idobata.model.Room;

import java.util.List;

/**
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class IdobataBotApp {

    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.err.println("Usage: apiToken organizationSlug roomName source");
            System.err.println("Ex: VpVuHc3FjizoY6kszGnrKttBLDJqAd78 uphyca playground \"Hello, Idobata.\"");
            System.exit(-1);
        }

        String apiToken = args[0];
        String organizationSlug = args[1];
        String roomName = args[2];
        String source = args[3];

        TokenAuthenticator authenticator = new TokenAuthenticator(apiToken);
        Idobata idobata = new IdobataBuilder().setRequestInterceptor(authenticator)
                                              .build();

        List<Room> rooms = idobata.getRooms(organizationSlug, roomName);
        Room room = rooms.get(0);

        idobata.postMessage(room.getId(), source);
    }
}
