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

import java.util.List;

/**
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class RoomBean implements Room {

    private long id;
    private String name;
    private long organizationId;
    private List<String> messageIds;
    private List<String> unreadMessageIds;
    private List<String> unreadMentionIds;
    private String invitationUrl;
    private List<Long> memberIds;
    private List<Long> earlyMemberIds;
    private List<Long> hookIds;
    private List<Long> botIds;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public long getOrganizationId() {
        return organizationId;
    }

    @Override
    public void setOrganizationId(long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public List<String> getMessageIds() {
        return messageIds;
    }

    @Override
    public void setMessageIds(List<String> messageIds) {
        this.messageIds = messageIds;
    }

    @Override
    public List<String> getUnreadMessageIds() {
        return unreadMessageIds;
    }

    @Override
    public void setUnreadMessageIds(List<String> unreadMessageIds) {
        this.unreadMessageIds = unreadMessageIds;
    }

    @Override
    public List<String> getUnreadMentionIds() {
        return unreadMentionIds;
    }

    @Override
    public void setUnreadMentionIds(List<String> unreadMentionIds) {
        this.unreadMentionIds = unreadMentionIds;
    }

    @Override
    public String getInvitationUrl() {
        return invitationUrl;
    }

    @Override
    public void setInvitationUrl(String invitationUrl) {
        this.invitationUrl = invitationUrl;
    }

    @Override
    public List<Long> getMemberIds() {
        return memberIds;
    }

    @Override
    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }

    @Override
    public List<Long> getEarlyMemberIds() {
        return earlyMemberIds;
    }

    @Override
    public void setEarlyMemberIds(List<Long> earlyMemberIds) {
        this.earlyMemberIds = earlyMemberIds;
    }

    @Override
    public List<Long> getHookIds() {
        return hookIds;
    }

    @Override
    public void setHookIds(List<Long> hookIds) {
        this.hookIds = hookIds;
    }

    @Override
    public List<Long> getBotIds() {
        return botIds;
    }

    @Override
    public void setBotIds(List<Long> botIds) {
        this.botIds = botIds;
    }
}
