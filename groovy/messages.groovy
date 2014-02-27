#!/usr/bin/env groovy

import com.uphyca.idobata.*

/**
 * Sample groovy script to post message.
 */
@Grab(group='com.uphyca.idobata', module='idobata4j-core', version='+')
def post(user, password, organizationSlug, roomName, source) {
    def auth = new FormAuthenticator(user, password)
    def api = new IdobataBuilder().setRequestInterceptor(auth).build()
    api.getRooms(organizationSlug, roomName).each {
        api.postMessage(it.id, source)
    }
}

env = System.getenv()

user = "$env.IDOBATA_USER"
password = "$env.IDOBATA_PASSWORD"

organizationSlug = args[0]
roomName = args[1]
source = args[2]

post(user, password, organizationSlug, roomName, source)

