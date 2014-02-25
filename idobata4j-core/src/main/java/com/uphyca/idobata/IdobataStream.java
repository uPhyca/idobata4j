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

import com.uphyca.idobata.event.MemberStatusChangedEvent;
import com.uphyca.idobata.event.MessageCreatedEvent;

import java.io.Closeable;

/**
 * Represents <a href="https://idobata.io/">Idobata</a> WebSocket API.
 * 
 * @author Sosuke Masui (masui@uphyca.com)
 */
public interface IdobataStream extends Closeable {

    public interface Listener<T> {
        void onEvent(T event);
    }

    /**
     * event: message_created
     */
    IdobataStream subscribeMessageCreated(Listener<MessageCreatedEvent> listener);

    /**
     * event: member_status_changed
     */
    IdobataStream subscribeMemberStatusChanged(Listener<MemberStatusChangedEvent> listener);

    void setErrorListener(ErrorListener listener);

}
