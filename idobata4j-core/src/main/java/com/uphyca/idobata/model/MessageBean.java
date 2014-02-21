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

package com.uphyca.idobata.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class MessageBean implements Message, Serializable {

    private long id;
    private String body;
    private String bodyPlain;
    private List<String> imageUrls;
    private boolean multiline;
    private List<Long> mentions;
    private String createdAt;
    private long roomId;
    private String senderType;
    private long senderId;
    private String senderName;
    private String senderIconUrl;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getBody() {
        return body;
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String getBodyPlain() {
        return bodyPlain;
    }

    @Override
    public void setBodyPlain(String bodyPlain) {
        this.bodyPlain = bodyPlain;
    }

    @Override
    public List<String> getImageUrls() {
        return imageUrls;
    }

    @Override
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public boolean isMultiline() {
        return multiline;
    }

    @Override
    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
    }

    @Override
    public List<Long> getMentions() {
        return mentions;
    }

    @Override
    public void setMentions(List<Long> mentions) {
        this.mentions = mentions;
    }

    @Override
    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public long getRoomId() {
        return roomId;
    }

    @Override
    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    @Override
    public String getSenderType() {
        return senderType;
    }

    @Override
    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    @Override
    public long getSenderId() {
        return senderId;
    }

    @Override
    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    @Override
    public String getSenderName() {
        return senderName;
    }

    @Override
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public String getSenderIconUrl() {
        return senderIconUrl;
    }

    @Override
    public void setSenderIconUrl(String senderIconUrl) {
        this.senderIconUrl = senderIconUrl;
    }
}
