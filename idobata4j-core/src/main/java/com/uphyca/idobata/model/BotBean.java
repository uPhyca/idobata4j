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

/**
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class BotBean implements Bot {

    private long id;
    private String name;
    private String iconUrl;
    private String apiToken;
    private String status;
    private String channelName;

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
    public String getIconUrl() {
        return iconUrl;
    }

    @Override
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @Override
    public String getApiToken() {
        return apiToken;
    }

    @Override
    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getChannelName() {
        return channelName;
    }

    @Override
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
