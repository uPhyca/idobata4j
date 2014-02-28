#!/usr/bin/env groovy

import com.uphyca.idobata.*

/**
 * Sample groovy script to get seed.
 */
@Grab(group='com.uphyca.idobata', module='idobata4j-core', version='+')
def seed(api) {
    def seed = api.seed
    def records = seed.records
    def organizations = records.organizations
    def rooms = records.rooms
    def user = records.user

    println "records: "
    println " organizations: "
    organizations.eachWithIndex{ organization, organizationIndex ->
    println "  " + organizationIndex + ": "
    println "   id: $organization.id"
    println "   name: $organization.name"
    println "   slug: $organization.slug"
    }
    println " rooms: "
    rooms.eachWithIndex{ room, roomIndex ->
    println "  " + roomIndex + ": "
    println "   unread_message_ids: "
    room.unreadMessageIds.eachWithIndex{ unreadMessageId, unreadMessageIdIndex ->
    println "    " + unreadMessageIdIndex + ": " + unreadMessageId
    }
    }
    println " user: "
    println "   channel_name: " + user.channelName
    println "   email: " + user.email
    println "   github_token: " + user.githubToken
    println "   gravatar_id: " + user.gravatarId
    println "   id: " + user.id
    println "   name: " + user.name

    println "version: " + seed.version
}

env = System.getenv()

user = "$env.IDOBATA_USER"
password = "$env.IDOBATA_PASSWORD"

def auth = new FormAuthenticator(user, password)
def api = new IdobataBuilder().setRequestInterceptor(auth).build()

seed(api)


