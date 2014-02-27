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
import com.pusher.client.channel.PresenceChannel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.uphyca.idobata.event.*;
import com.uphyca.idobata.http.TypedInput;
import com.uphyca.idobata.pusher.PresenceChannelEventListenerAdapter;
import com.uphyca.idobata.transform.ConversionException;
import com.uphyca.idobata.transform.Converter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sosuke Masui (masui@uphyca.com)
 */
class IdobataStreamImpl extends PresenceChannelEventListenerAdapter implements IdobataStream, ConnectionEventListener {

    private static final String MESSAGE_CREATED = "message_created";
    private static final String MEMBER_STATUS_CHANGED = "member_status_changed";
    private static final String ROOM_TOUCHED = "room_touched";

    private static final Map<String, Type> TYPE_MAP;
    static {
        TYPE_MAP = new HashMap<String, Type>();
        TYPE_MAP.put(MESSAGE_CREATED, MessageCreatedEvent.class);
        TYPE_MAP.put(MEMBER_STATUS_CHANGED, MemberStatusChangedEvent.class);
        TYPE_MAP.put(ROOM_TOUCHED, RoomTouchedEvent.class);
    }

    private final Idobata idobata;
    private final String channelName;
    private final Converter converter;
    private final Pusher pusher;
    private final Map<String, Set<Listener<?>>> listenerMap = new ConcurrentHashMap<String, Set<Listener<?>>>();
    private final PresenceChannel presenceChannel;

    private ErrorListener errorListener = EMPTY_ERROR_LISTENER;
    private ConnectionListener connectionListener = EMPTY_CONNECTION_LISTENER;

    IdobataStreamImpl(Idobata idobata, Pusher pusher, String channelName, Converter converter) {
        this.idobata = idobata;
        this.pusher = pusher;
        this.channelName = channelName;
        this.converter = converter;
        presenceChannel = pusher.subscribePresence(channelName);
    }

    @Override
    public IdobataStream subscribeMessageCreated(Listener<MessageCreatedEvent> listener) {
        addListener(MESSAGE_CREATED, listener);
        subscribePresence(MESSAGE_CREATED);
        return this;
    }

    @Override
    public IdobataStream subscribeMemberStatusChanged(Listener<MemberStatusChangedEvent> listener) {
        addListener(MEMBER_STATUS_CHANGED, listener);
        subscribePresence(MEMBER_STATUS_CHANGED);
        return this;
    }

    @Override
    public IdobataStream subscribeRoomTouched(Listener<RoomTouchedEvent> listener) {
        addListener(ROOM_TOUCHED, listener);
        subscribePresence(ROOM_TOUCHED);
        return this;
    }

    @Override
    public IdobataStream setErrorListener(ErrorListener listener) {
        this.errorListener = listener != null ? listener : EMPTY_ERROR_LISTENER;
        return this;
    }

    @Override
    public IdobataStream setConnectionListener(ConnectionListener listener) {
        this.connectionListener = listener != null ? listener : EMPTY_CONNECTION_LISTENER;
        return this;
    }

    @Override
    public void close() throws IOException {
        pusher.disconnect();
    }

    @Override
    public void open() {
        pusher.connect();
    }

    private void subscribePresence(String eventName) {
        presenceChannel.bind(eventName, this);
        pusher.connect(this, ConnectionState.ALL);
    }

    private void addListener(String event, Listener listener) {
        Set<Listener<?>> listeners = listenerMap.get(event);
        if (listeners == null) {
            listenerMap.put(event, listeners = new LinkedHashSet<Listener<?>>());
        }
        listeners.add(listener);
    }

    private void publishError(Exception e) {
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
            each.onEvent(event);
        }
    }

    @Override
    public void onEvent(String channelName, String eventName, String data) {
        try {
            Type type = TYPE_MAP.get(eventName);
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

    @Override
    public void onConnectionStateChange(ConnectionStateChange change) {
        switch (change.getCurrentState()) {
            case CONNECTED:
                connectionListener.opened(new ConnectionEventValue(ConnectionEvent.OPENED));
                break;
            case DISCONNECTED:
                connectionListener.closed(new ConnectionEventValue(ConnectionEvent.CLOSED));
                break;
        }
    }

    @Override
    public void onError(String message, String code, Exception e) {
        publishError(e);
    }

    private static final ErrorListener EMPTY_ERROR_LISTENER = new ErrorListener() {
        @Override
        public void onError(IdobataError error) {
            //no-op
        }
    };

    private static final ConnectionListener EMPTY_CONNECTION_LISTENER = new ConnectionListener() {
        @Override
        public void closed(ConnectionEvent event) {
            //no-op
        }

        @Override
        public void opened(ConnectionEvent event) {
            //no-op
        }
    };

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
