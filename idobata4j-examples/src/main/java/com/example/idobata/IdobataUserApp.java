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

import com.uphyca.idobata.FormAuthenticator;
import com.uphyca.idobata.Idobata;
import com.uphyca.idobata.IdobataBuilder;
import com.uphyca.idobata.model.Room;

import java.util.List;

/**
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class IdobataUserApp {

    public static void main(String[] args) throws Exception {
        if (args.length < 5) {
            System.err.println("Usage: email password organizationSlug roomName source");
            System.err.println("Ex: foo@example.com secret uphyca playground \"Hello, Idobata.\"");
            System.exit(-1);
        }

        String email = args[0];
        String password = args[0];
        String organizationSlug = args[0];
        String roomName = args[0];
        String source = args[0];

        FormAuthenticator authenticator = new FormAuthenticator(email, password);
        Idobata idobata = new IdobataBuilder().setRequestInterceptor(authenticator)
                                              .build();

        List<Room> rooms = idobata.getRooms(organizationSlug, roomName);
        Room room = rooms.get(0);

        idobata.postMessage(room.getId(), source);
    }
}
