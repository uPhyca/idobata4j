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
public class UserBean extends GuyBean implements User, Serializable {

    private String gravatarId;
    private String email;
    private String githubToken;
    private List<Long> organizationIds;
    private List<Long> roomIds;

    @Override
    public String getGravatarId() {
        return gravatarId;
    }

    @Override
    public void setGravatarId(String gravatarId) {
        this.gravatarId = gravatarId;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getGithubToken() {
        return githubToken;
    }

    @Override
    public void setGithubToken(String githubToken) {
        this.githubToken = githubToken;
    }

    @Override
    public List<Long> getOrganizationIds() {
        return organizationIds;
    }

    @Override
    public void setOrganizationIds(List<Long> organizationIds) {
        this.organizationIds = organizationIds;
    }

    @Override
    public List<Long> getRoomIds() {
        return roomIds;
    }

    @Override
    public void setRoomIds(List<Long> roomIds) {
        this.roomIds = roomIds;
    }
}
