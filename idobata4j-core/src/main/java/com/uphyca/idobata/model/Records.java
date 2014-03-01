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
 * Represents an records entity.
 * 
 * @author Sosuke Masui (masui@uphyca.com)
 */
public interface Records extends Serializable {
    List<Organization> getOrganizations();

    void setOrganizations(List<Organization> organizations);

    List<Room> getRooms();

    void setRooms(List<Room> rooms);

    User getUser();

    void setUser(User user);

    Bot getBot();

    void setBot(Bot bot);
}
