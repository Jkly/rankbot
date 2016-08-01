package jkly.rankbot

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "xodus")
open class XodusConfiguration() {
    var dataDir = "./data"
        get
        set
}