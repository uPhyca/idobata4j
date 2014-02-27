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

package com.uphyca.idobata;

import com.uphyca.idobata.model.*;

import java.io.InputStream;
import java.util.List;

/**
 * Represents <a href="https://idobata.io/">Idobata</a> API.
 * 
 * @author Sosuke Masui (masui@uphyca.com)
 */
public interface Idobata {

    /**
     * /api/seed
     */
    Seed getSeed() throws IdobataError;

    /**
     * /api/messages
     */
    List<Message> getMessages(List<String> ids) throws IdobataError;

    /**
     * /api/messages
     */
    List<Message> getMessages(long roomId, long olderThan) throws IdobataError;

    /**
     * /api/messages
     */
    Message postMessage(long roomId, String source) throws IdobataError;

    /**
     * /api/messages
     */
    Message postMessage(long roomId, String fileName, String contentType, InputStream content) throws IdobataError;

    /**
     * /api/messages/${id}
     */
    Message deleteMessage(long messageId) throws IdobataError;

    /**
     * /api/users
     */
    List<User> getUsers(List<Long> ids) throws IdobataError;

    /**
     * /api/users/${id}
     */
    User getUser(long guyId) throws IdobataError;

    /**
     * /api/rooms
     */
    List<Room> getRooms(List<Long> ids) throws IdobataError;

    /**
     * /api/rooms
     */
    Room getRoom(String organizationSlug, String roomName) throws IdobataError;

    /**
     * /api/rooms
     */
    @Deprecated
    List<Room> getRooms(String organizationSlug, String roomName) throws IdobataError;

    /**
     * /api/room/${id}
     */
    Room getRoom(long roomId) throws IdobataError;

    /**
     * /api/bots
     */
    List<Bot> getBots(List<Long> ids) throws IdobataError;

    /**
     * /api/user/rooms/${id}/touch
     */
    void postTouch(long roomId) throws IdobataError;

    /**
     * /pusher/auth
     */
    String postPusherAuth(String channelName, String socketId) throws IdobataError;

    /**
     * :wss://ws.pusherapp.com/app/${api_key}
     */
    IdobataStream openStream() throws IdobataError;
}
