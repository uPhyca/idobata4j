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

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class IdobataUserImageUploadApp {

    public static void main(String[] args) throws Exception {

        String email = "esmasui@gmail.com";
        String password = "wrCyZBnf7jnDbDX2APRXxNgCigXbMA";
        String organizationSlug = "uphyca";
        String roomName = "playground";

        FormAuthenticator authenticator = new FormAuthenticator(email, password);
        Idobata idobata = new IdobataBuilder().setRequestInterceptor(authenticator)
                                              .build();

        List<Room> rooms = idobata.getRooms(organizationSlug, roomName);
        Room room = rooms.get(0);

        idobata.postMessage(room.getId(), "hoge.png", "image/png", new ByteArrayInputStream("a".getBytes()));
    }
}
