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
import com.pusher.client.channel.PresenceChannelEventListener;
import com.uphyca.idobata.event.MemberStatusChangedEvent;
import com.uphyca.idobata.event.MessageCreatedEvent;
import com.uphyca.idobata.event.RoomTouchedEvent;
import com.uphyca.idobata.transform.Converter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.*;

/**
 * @author Sosuke Masui (masui@uphyca.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class IdobataStreamTest {

    IdobataStreamImpl underTest;

    @Mock
    Idobata idobata;

    @Mock
    Pusher pusher;

    @Mock
    PresenceChannel presenceChannel;

    String channelName;

    @Mock
    Converter converter;

    @Before
    public void setUp() throws Exception {
        channelName = "abc";
        given(pusher.subscribePresence(channelName)).willReturn(presenceChannel);

        underTest = new IdobataStreamImpl(idobata, pusher, channelName, converter);
    }

    @Test
    public void close() throws Exception {
        underTest.close();

        verify(pusher).disconnect();
    }

    @Test
    public void subscribeMessageCreated() throws Exception {
        final String eventName = "message_created";

        IdobataStream.Listener<MessageCreatedEvent> listener = mock(IdobataStream.Listener.class);
        underTest.subscribeMessageCreated(listener);

        underTest.onEvent(channelName, eventName, "{}");

        verify(presenceChannel).bind(eq(eventName), any(PresenceChannelEventListener.class));
        verify(listener).onEvent(any(MessageCreatedEvent.class));
    }

    @Test
    public void subscribeMemberStatusChanged() throws Exception {
        final String eventName = "member_status_changed";

        IdobataStream.Listener<MemberStatusChangedEvent> listener = mock(IdobataStream.Listener.class);
        underTest.subscribeMemberStatusChanged(listener);

        underTest.onEvent(channelName, eventName, "{}");

        verify(presenceChannel).bind(eq(eventName), any(PresenceChannelEventListener.class));
        verify(listener).onEvent(any(MemberStatusChangedEvent.class));
    }

    @Test
    public void subscribeRoomTouched() throws Exception {
        final String eventName = "room_touched";

        IdobataStream.Listener<RoomTouchedEvent> listener = mock(IdobataStream.Listener.class);
        underTest.subscribeRoomTouched(listener);

        underTest.onEvent(channelName, eventName, "{}");

        verify(presenceChannel).bind(eq(eventName), any(PresenceChannelEventListener.class));
        verify(listener).onEvent(any(RoomTouchedEvent.class));
    }

    @Test
    public void subscribeMultipleEvents() throws Exception {
        final String eventName1 = "member_status_changed";
        final String eventName2 = "message_created";

        IdobataStream.Listener<MemberStatusChangedEvent> listener1 = mock(IdobataStream.Listener.class);
        underTest.subscribeMemberStatusChanged(listener1);

        IdobataStream.Listener<MessageCreatedEvent> listener2 = mock(IdobataStream.Listener.class);
        underTest.subscribeMessageCreated(listener2);

        underTest.onEvent(channelName, eventName1, "{}");
        underTest.onEvent(channelName, eventName2, "{}");

        verify(presenceChannel).bind(eq(eventName1), any(PresenceChannelEventListener.class));
        verify(presenceChannel).bind(eq(eventName2), any(PresenceChannelEventListener.class));

        verify(listener1, times(1)).onEvent(any(MemberStatusChangedEvent.class));
        verify(listener2, times(1)).onEvent(any(MessageCreatedEvent.class));
    }

}
