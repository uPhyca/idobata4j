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
 * Represents an message entity.
 * 
 * @author Sosuke Masui (masui@uphyca.com)
 */
public interface Message extends Serializable {
    long getId();

    void setId(long id);

    String getBody();

    void setBody(String body);

    String getBodyPlain();

    void setBodyPlain(String bodyPlain);

    List<String> getImageUrls();

    void setImageUrls(List<String> imageUrls);

    boolean isMultiline();

    void setMultiline(boolean multiline);

    List<Long> getMentions();

    void setMentions(List<Long> mentions);

    String getCreatedAt();

    void setCreatedAt(String createdAt);

    long getRoomId();

    void setRoomId(long roomId);

    String getSenderType();

    void setSenderType(String senderType);

    long getSenderId();

    void setSenderId(long senderId);

    String getSenderName();

    void setSenderName(String senderName);

    String getSenderIconUrl();

    void setSenderIconUrl(String senderIconUrl);
}
