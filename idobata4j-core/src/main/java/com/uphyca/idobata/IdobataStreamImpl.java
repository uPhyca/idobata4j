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

import com.pusher.client.Pusher;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.uphyca.idobata.event.MemberStatusChangedEvent;
import com.uphyca.idobata.event.MessageCreatedEvent;
import com.uphyca.idobata.http.TypedInput;
import com.uphyca.idobata.pusher.PresenceChannelEventListenerAdapter;
import com.uphyca.idobata.transform.ConversionException;
import com.uphyca.idobata.transform.Converter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sosuke Masui (masui@uphyca.com)
 */
class IdobataStreamImpl extends PresenceChannelEventListenerAdapter implements IdobataStream {

    private static final String MESSAGE_CREATED = "message_created";
    private static final String MEMBER_STATUS_CHANGED = "member_status_changed";

    private static final String[] events = {
            MESSAGE_CREATED, MEMBER_STATUS_CHANGED
    };

    private final Idobata idobata;
    private final String channelName;
    private final Converter converter;
    private final Pusher pusher;
    private final Map<String, Set<Listener<?>>> listenerMap = new ConcurrentHashMap<String, Set<Listener<?>>>();

    private ErrorListener errorListener;

    IdobataStreamImpl(Idobata idobata, Pusher pusher, String channelName, Converter converter) {
        this.idobata = idobata;
        this.pusher = pusher;
        this.channelName = channelName;
        this.converter = converter;
        subscribePresence();
    }

    @Override
    public IdobataStream subscribeMessageCreated(Listener<MessageCreatedEvent> listener) {
        addListener(MESSAGE_CREATED, listener);
        return this;
    }

    @Override
    public IdobataStream subscribeMemberStatusChanged(Listener<MemberStatusChangedEvent> listener) {
        addListener(MEMBER_STATUS_CHANGED, listener);
        return this;
    }

    @Override
    public void setErrorListener(ErrorListener listener) {
        errorListener = listener;
    }

    @Override
    public void close() throws IOException {
        pusher.disconnect();
    }

    private void subscribePresence() {
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                pusher.subscribePresence(channelName, IdobataStreamImpl.this, events);
            }

            @Override
            public void onError(String message, String code, Exception e) {
                publishError(e);
            }
        }, ConnectionState.CONNECTED);
    }

    private void addListener(String event, Listener listener) {
        Set<Listener<?>> listeners = listenerMap.get(event);
        if (listeners == null) {
            listenerMap.put(event, listeners = new LinkedHashSet<Listener<?>>());
        }
        listeners.add(listener);
    }

    private void publishError(Exception e) {
        if (errorListener == null) {
            return;
        }
        IdobataError idobataError = new IdobataError(e);
        idobataError.fillInStackTrace();
        errorListener.onError(idobataError);
    }

    private void publishEvent(String eventName, Object event) {
        Set<Listener<?>> listeners = listenerMap.get(eventName);
        if (listeners == null) {
            return;
        }
        for (Listener each : listeners) {
            each.onResponse(event);
        }
    }

    @Override
    public void onEvent(String channelName, String eventName, String data) {
        try {
            Type type = null;
            if (eventName.equals(MESSAGE_CREATED)) {
                type = MessageCreatedEvent.class;
            } else if (eventName.equals(MEMBER_STATUS_CHANGED)) {
                type = MemberStatusChangedEvent.class;
            }
            if (type == null) {
                throw new IllegalStateException();
            }
            publishEvent(eventName, converter.convert(new JsonTypedInput(data), type));
        } catch (ConversionException e) {
            publishError(e);
        } catch (IOException e) {
            publishError(e);
        }
    }

    private static final class JsonTypedInput implements TypedInput {

        private final ByteArrayInputStream body;

        private JsonTypedInput(String string) {
            try {
                body = new ByteArrayInputStream(string.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String mimeType() {
            return "application/json; charset=UTF-8";
        }

        @Override
        public long length() {
            return body.available();
        }

        @Override
        public InputStream in() throws IOException {
            return body;
        }
    }
}
