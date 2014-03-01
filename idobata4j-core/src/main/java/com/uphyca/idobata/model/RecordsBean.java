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
public class RecordsBean implements Records, Serializable {

    private List<Organization> organizations;
    private List<Room> rooms;
    private User user;
    private Bot bot;

    @Override
    public List<Organization> getOrganizations() {
        return organizations;
    }

    @Override
    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    @Override
    public List<Room> getRooms() {
        return rooms;
    }

    @Override
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Bot getBot() {
        return bot;
    }

    @Override
    public void setBot(Bot bot) {
        this.bot = bot;
    }
}
