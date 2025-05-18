package io.github.kdroidfilter.database.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class NetworkMode {
    FULL_OPEN,      // No restrictions
    BLACKLIST,      // Block specific hosts
    WHITELIST,      // Allow only specific hosts
    LOCAL_ONLY,      // Allow only local network access
    OFFLINE         // Disable network access
}

// 2. Optional details depending on mode
@Serializable
sealed interface ModeSpec {
    @Serializable @SerialName("None")
    object None : ModeSpec                        // For FULL_OPEN and LOCAL_ONLY
    @Serializable @SerialName("HostList")
    data class HostList(val hosts: Set<String>) : ModeSpec  // For BLACKLIST and WHITELIST
}

@Serializable
data class NetworkPolicy(
    val mode: NetworkMode,
    val spec: ModeSpec = ModeSpec.None
)
