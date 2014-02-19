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
public class OrganizationBean implements Organization {
    private long id;
    private String name;
    private String slug;
    private long userId;
    private List<Long> regularMemberIds;
    private List<Long> guestMemberIds;
    private List<Long> botIds;
    private List<Long> roomIds;
    private List<Long> invitationIds;

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
    public String getSlug() {
        return slug;
    }

    @Override
    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public List<Long> getRegularMemberIds() {
        return regularMemberIds;
    }

    @Override
    public void setRegularMemberIds(List<Long> regularMemberIds) {
        this.regularMemberIds = regularMemberIds;
    }

    @Override
    public List<Long> getGuestMemberIds() {
        return guestMemberIds;
    }

    @Override
    public void setGuestMemberIds(List<Long> guestMemberIds) {
        this.guestMemberIds = guestMemberIds;
    }

    @Override
    public List<Long> getBotIds() {
        return botIds;
    }

    @Override
    public void setBotIds(List<Long> botIds) {
        this.botIds = botIds;
    }

    @Override
    public List<Long> getRoomIds() {
        return roomIds;
    }

    @Override
    public void setRoomIds(List<Long> roomIds) {
        this.roomIds = roomIds;
    }

    @Override
    public List<Long> getInvitationIds() {
        return invitationIds;
    }

    @Override
    public void setInvitationIds(List<Long> invitationIds) {
        this.invitationIds = invitationIds;
    }
}
