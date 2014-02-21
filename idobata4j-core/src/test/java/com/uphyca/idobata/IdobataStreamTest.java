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
import com.uphyca.idobata.event.MemberStatusChangedEvent;
import com.uphyca.idobata.event.MessageCreatedEvent;
import com.uphyca.idobata.transform.Converter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

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

    String channelName;

    @Mock
    Converter converter;

    @Before
    public void setUp() throws Exception {
        channelName = "abc";
        underTest = new IdobataStreamImpl(idobata, pusher, channelName, converter);
    }

    @Test
    public void close() throws Exception {
        underTest.close();

        verify(pusher).disconnect();
    }

    @Test
    public void subscribeMessageCreated() throws Exception {

        IdobataStream.Listener<MessageCreatedEvent> listener = mock(IdobataStream.Listener.class);
        underTest.subscribeMessageCreated(listener);

        underTest.onEvent(channelName, "message_created", "{}");

        verify(listener).onResponse(any(MessageCreatedEvent.class));
    }

    @Test
    public void subscribeMemberStatusChanged() throws Exception {

        IdobataStream.Listener<MemberStatusChangedEvent> listener = mock(IdobataStream.Listener.class);
        underTest.subscribeMemberStatusChanged(listener);

        underTest.onEvent(channelName, "member_status_changed", "{}");

        verify(listener).onResponse(any(MemberStatusChangedEvent.class));
    }
}
