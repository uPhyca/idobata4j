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
import com.uphyca.idobata.ResponseListener;
import com.uphyca.idobata.TokenAuthenticator;
import com.uphyca.idobata.event.MemberStatusChangedEvent;
import com.uphyca.idobata.event.MessageCreatedEvent;

/**
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class IdobataBotStreamApp {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: apiToken");
            System.err.println("Ex: VpVuHc3FjizoY6kszGnrKttBLDJqAd78");
            System.exit(-1);
        }

        String apiToken = args[0];

        TokenAuthenticator authenticator = new TokenAuthenticator(apiToken);
        Idobata idobata = new IdobataBuilder().setRequestInterceptor(authenticator)
                                              .build();

        idobata.openStream()
               .subscribeMessageCreated(new ResponseListener<MessageCreatedEvent>() {
                   @Override
                   public void onResponse(MessageCreatedEvent event) {
                       System.out.println(event.getSenderName() + ":" + event.getBody());
                   }
               })
               .subscribeMemberStatusChanged(new ResponseListener<MemberStatusChangedEvent>() {
                   @Override
                   public void onResponse(MemberStatusChangedEvent event) {
                       System.out.println(event.getId() + ":" + event.getStatus());
                   }
               });

        final Object lock = new Object();
        synchronized (lock) {
            lock.wait();
        }
    }
}
