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
 * Represents an user entity.
 * 
 * @author Sosuke Masui (masui@uphyca.com)
 */
public interface User {

    long getId();

    void setId(long id);

    String getName();

    void setName(String name);

    String getGravatarId();

    void setGravatarId(String gravatarId);

    String getStatus();

    void setStatus(String status);

    String getEmail();

    void setEmail(String email);

    String getChannelName();

    void setChannelName(String channelName);

    String getGithubToken();

    void setGithubToken(String githubToken);

    List<Long> getOrganizationIds();

    void setOrganizationIds(List<Long> organizationIds);

    List<Long> getRoomIds();

    void setRoomIds(List<Long> roomIds);
}
