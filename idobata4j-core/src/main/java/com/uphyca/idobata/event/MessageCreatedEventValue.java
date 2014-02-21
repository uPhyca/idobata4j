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

package com.uphyca.idobata.event;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class MessageCreatedEventValue implements MessageCreatedEvent, Serializable {

    private final long id;
    private final String body;
    private final String bodyPlain;
    private final List<String> imageUrls;
    private final boolean multiline;
    private final List<Long> mentions;
    private final String createdAt;
    private final long roomId;
    private final String roomName;
    private final String organizationSlug;
    private final String senderType;
    private final long senderId;
    private final String senderName;
    private final String senderIconUrl;

    public MessageCreatedEventValue(long id, String body, String bodyPlain, List<String> imageUrls, boolean multiline, List<Long> mentions, String createdAt, long roomId, String roomName, String organizationSlug, String senderType, long senderId, String senderName, String senderIconUrl) {
        this.id = id;
        this.body = body;
        this.bodyPlain = bodyPlain;
        this.imageUrls = imageUrls;
        this.multiline = multiline;
        this.mentions = mentions;
        this.createdAt = createdAt;
        this.roomId = roomId;
        this.roomName = roomName;
        this.organizationSlug = organizationSlug;
        this.senderType = senderType;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderIconUrl = senderIconUrl;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public String getBodyPlain() {
        return bodyPlain;
    }

    @Override
    public List<String> getImageUrls() {
        return imageUrls;
    }

    @Override
    public boolean isMultiline() {
        return multiline;
    }

    @Override
    public List<Long> getMentions() {
        return mentions;
    }

    @Override
    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public long getRoomId() {
        return roomId;
    }

    @Override
    public String getRoomName() {
        return roomName;
    }

    @Override
    public String getOrganizationSlug() {
        return organizationSlug;
    }

    @Override
    public String getSenderType() {
        return senderType;
    }

    @Override
    public long getSenderId() {
        return senderId;
    }

    @Override
    public String getSenderName() {
        return senderName;
    }

    @Override
    public String getSenderIconUrl() {
        return senderIconUrl;
    }
}
