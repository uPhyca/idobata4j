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

package com.uphyca.idobata.transform;

import com.uphyca.idobata.event.*;
import com.uphyca.idobata.http.TypedInput;
import com.uphyca.idobata.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link com.uphyca.idobata.transform.Converter} which uses org.json for deserialization of entities.
 * 
 * @author Sosuke Masui (masui@uphyca.com)
 */
public class JSONConverter implements Converter {

    @Override
    public Object convert(TypedInput body, Type type) throws ConversionException, IOException {
        String src;
        InputStream in = null;
        try {
            in = body.in();
            src = drain(in);
        } finally {
            if (in != null) {
                in.close();
            }
        }

        try {
            JSONObject json = new JSONObject(src);

            if (type.equals(Seed.class)) {
                Seed seed = new SeedBean();
                drain(json, seed);
                return seed;
            }

            if (type.equals(Room[].class)) {
                return asRoomList(getJSONArray(json, "rooms"));
            }

            if (type.equals(Room.class)) {
                Room room = new RoomBean();
                drain(getJSONObject(json, "room"), room);
                return room;
            }

            if (type.equals(Message[].class)) {
                return asMessageList(getJSONArray(json, "messages"));
            }

            if (type.equals(Message.class)) {
                Message message = new MessageBean();
                drain(getJSONObject(json, "message"), message);
                return message;
            }

            if (type.equals(User[].class)) {
                return asUserList((getJSONArray(json, "users")));
            }

            if (type.equals(User.class)) {
                User user = new UserBean();
                drain(getJSONObject(json, "user"), user);
                return user;
            }

            if (type.equals(Bot[].class)) {
                return asBotList((getJSONArray(json, "bots")));
            }

            if (type.equals(Bot.class)) {
                Bot bot = new BotBean();
                drain(getJSONObject(json, "bot"), bot);
                return bot;
            }

            if (type.equals(MemberStatusChangedEvent.class)) {
                return asMemberStatusChangedEvent(json);
            }

            if (type.equals(MessageCreatedEvent.class)) {
                return asMessageCreatedEvent(getJSONObject(json, "message"));
            }

            if (type.equals(RoomTouchedEvent.class)) {
                return asRoomTouchedEvent(json);
            }

        } catch (JSONException e) {
            throw new ConversionException(e);
        }

        throw new IllegalArgumentException();
    }

    private static MemberStatusChangedEvent asMemberStatusChangedEvent(JSONObject json) throws JSONException {
        return new MemberStatusChangedEventValue(getLong(json, "id"), getString(json, "status"), getString(json, "type"));
    }

    private static MessageCreatedEvent asMessageCreatedEvent(JSONObject json) throws JSONException {
        return new MessageCreatedEventValue(getLong(json, "id"), getString(json, "body"), getString(json, "body_plain"), asStringList(getJSONArray(json, "image_urls")), getBoolean(json, "multiline"), asLongList(getJSONArray(json, "mentions")), getString(json, "created_at"), getLong(json, "room_id"), getString(json, "room_name"), getString(json, "organization_slug"), getString(json, "sender_type"), getLong(json, "sender_id"), getString(json, "sender_name"), getString(json, "sender_icon_url"));
    }

    private static RoomTouchedEvent asRoomTouchedEvent(JSONObject json) throws JSONException {
        return new RoomTouchedEventValue(getLong(json, "room_id"));
    }

    private static String drain(InputStream in) throws IOException {
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int readBytes; (readBytes = in.read(buffer)) > -1;) {
            out.write(buffer, 0, readBytes);
        }
        return new String(out.toByteArray(), "UTF-8");
    }

    private static void drain(JSONObject json, Seed seed) throws JSONException {
        if (json == null) {
            return;
        }
        seed.setVersion(getLong(json, "version"));
        RecordsBean records = new RecordsBean();
        drain(getJSONObject(json, "records"), records);
        seed.setRecords(records);
    }

    private static void drain(JSONObject json, Records records) throws JSONException {
        if (json == null) {
            return;
        }
        records.setOrganizations(asOrganizationList(getJSONArray(json, "organizations")));
        records.setRooms(asRoomList(getJSONArray(json, "rooms")));
        UserBean user = new UserBean();
        drain(getJSONObject(json, "user"), user);
        drain(getJSONObject(json, "bot"), user);
        records.setUser(user);
    }

    private static void drain(JSONObject json, Organization organization) throws JSONException {
        if (json == null) {
            return;
        }
        organization.setId(getLong(json, "id"));
        organization.setName(getString(json, "name"));
        organization.setSlug(getString(json, "slug"));
        organization.setUserId(getLong(json, "user_id"));
        organization.setRegularMemberIds(asLongList(getJSONArray(json, "regular_member_ids")));
        organization.setGuestMemberIds(asLongList(getJSONArray(json, "guest_member_ids")));
        organization.setBotIds(asLongList(getJSONArray(json, "bot_ids")));
        organization.setRoomIds(asLongList(getJSONArray(json, "room_ids")));
        organization.setInvitationIds(asLongList(getJSONArray(json, "invitation_ids")));
    }

    private static void drain(JSONObject json, Room room) throws JSONException {
        if (json == null) {
            return;
        }
        room.setId(getLong(json, "id"));
        room.setName(getString(json, "name"));
        room.setOrganizationId(getLong(json, "organization_id"));
        room.setMessageIds(asStringList(getJSONArray(json, "message_ids")));
        room.setUnreadMessageIds(asStringList(getJSONArray(json, "unread_message_ids")));
        room.setUnreadMentionIds(asStringList(getJSONArray(json, "unread_mention_ids")));
        room.setInvitationUrl(getString(json, "invitation_url"));
        room.setMemberIds(asLongList(getJSONArray(json, "member_ids")));
        room.setEarlyMemberIds(asLongList(getJSONArray(json, "early_member_ids")));
        room.setHookIds(asLongList(getJSONArray(json, "hook_ids")));
        room.setBotIds(asLongList(getJSONArray(json, "bot_ids")));
    }

    private static void drain(JSONObject json, User user) throws JSONException {
        if (json == null) {
            return;
        }
        user.setId(getLong(json, "id"));
        user.setName(getString(json, "name"));
        user.setGravatarId(getString(json, "gravatar_id"));
        user.setStatus(getString(json, "status"));
        user.setEmail(getString(json, "email"));
        user.setChannelName(getString(json, "channel_name"));
        user.setGithubToken(getString(json, "github_token"));
        user.setOrganizationIds(asLongList(getJSONArray(json, "organization_ids")));
        user.setRoomIds(asLongList(getJSONArray(json, "room_ids")));
    }

    private static void drain(JSONObject json, Bot bot) throws JSONException {
        if (json == null) {
            return;
        }
        bot.setId(getLong(json, "id"));
        bot.setName(getString(json, "name"));
        bot.setIconUrl(getString(json, "icon_url"));
        bot.setApiToken(getString(json, "api_token"));
        bot.setStatus(getString(json, "status"));
        bot.setChannelName(getString(json, "channel_name"));
    }

    private static void drain(JSONObject json, Message message) throws JSONException {
        if (json == null) {
            return;
        }
        message.setId(getLong(json, "id"));
        message.setBody(getString(json, "body"));
        message.setBodyPlain(getString(json, "body_plain"));
        message.setImageUrls(asStringList(getJSONArray(json, "image_urls")));
        message.setMultiline(getBoolean(json, "multiline"));
        message.setMentions(asLongList(getJSONArray(json, "mentions")));
        message.setCreatedAt(getString(json, "created_at"));
        message.setRoomId(getLong(json, "room_id"));
        message.setSenderType(getString(json, "sender_type"));
        message.setSenderId(getLong(json, "sender_id"));
        message.setSenderName(getString(json, "sender_name"));
        message.setSenderIconUrl(getString(json, "sender_icon_url"));
    }

    private static List<Organization> asOrganizationList(JSONArray array) throws JSONException {
        if (array == null) {
            return null;
        }
        List<Organization> list = new ArrayList<Organization>(array.length());
        for (int i = 0, size = array.length(); i < size; ++i) {
            OrganizationBean organization = new OrganizationBean();
            drain(array.getJSONObject(i), organization);
            list.add(organization);
        }
        return list;
    }

    private static List<Room> asRoomList(JSONArray array) throws JSONException {
        if (array == null) {
            return null;
        }
        List<Room> list = new ArrayList<Room>(array.length());
        for (int i = 0, size = array.length(); i < size; ++i) {
            RoomBean room = new RoomBean();
            drain(array.getJSONObject(i), room);
            list.add(room);
        }
        return list;
    }

    private static List<User> asUserList(JSONArray array) throws JSONException {
        if (array == null) {
            return null;
        }
        List<User> list = new ArrayList<User>(array.length());
        for (int i = 0, size = array.length(); i < size; ++i) {
            UserBean user = new UserBean();
            drain(array.getJSONObject(i), user);
            list.add(user);
        }
        return list;
    }

    private static List<Bot> asBotList(JSONArray array) throws JSONException {
        if (array == null) {
            return null;
        }
        List<Bot> list = new ArrayList<Bot>(array.length());
        for (int i = 0, size = array.length(); i < size; ++i) {
            BotBean bot = new BotBean();
            drain(array.getJSONObject(i), bot);
            list.add(bot);
        }
        return list;
    }

    private static List<Message> asMessageList(JSONArray array) throws JSONException {
        if (array == null) {
            return null;
        }
        List<Message> list = new ArrayList<Message>(array.length());
        for (int i = 0, size = array.length(); i < size; ++i) {
            MessageBean message = new MessageBean();
            drain(array.getJSONObject(i), message);
            list.add(message);
        }
        return list;
    }

    private static List<String> asStringList(JSONArray array) throws JSONException {
        if (array == null) {
            return null;
        }
        List<String> list = new ArrayList<String>(array.length());
        for (int i = 0, size = array.length(); i < size; ++i) {
            list.add(array.getString(i));
        }
        return list;
    }

    private static List<Long> asLongList(JSONArray array) throws JSONException {
        if (array == null) {
            return null;
        }
        List<Long> list = new ArrayList<Long>(array.length());
        for (int i = 0, size = array.length(); i < size; ++i) {
            list.add(array.getLong(i));
        }
        return list;
    }

    private static JSONObject getJSONObject(JSONObject json, String name) throws JSONException {
        return json.has(name) && !json.isNull(name) ? json.getJSONObject(name) : null;
    }

    private static JSONArray getJSONArray(JSONObject json, String name) throws JSONException {
        return json.has(name) && !json.isNull(name) ? json.getJSONArray(name) : null;
    }

    private static String getString(JSONObject json, String name) throws JSONException {
        return json.has(name) && !json.isNull(name) ? json.getString(name) : null;
    }

    private static long getLong(JSONObject json, String name) throws JSONException {
        return json.has(name) && !json.isNull(name) ? json.getLong(name) : 0L;
    }

    private static boolean getBoolean(JSONObject json, String name) throws JSONException {
        return json.has(name) && !json.isNull(name) ? json.getBoolean(name) : false;
    }
}
