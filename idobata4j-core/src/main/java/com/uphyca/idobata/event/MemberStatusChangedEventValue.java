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

/**
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class MemberStatusChangedEventValue implements MemberStatusChangedEvent, Serializable {

    private final long id;
    private final String status;
    private final String type;

    public MemberStatusChangedEventValue(long id, String status, String type) {
        this.id = id;
        this.status = status;
        this.type = type;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getType() {
        return type;
    }
}
