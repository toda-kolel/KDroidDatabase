package io.github.kdroidfilter.database.core

import kotlinx.serialization.Serializable

/**
 * Detection type to observe
 */
@Serializable
enum class DetectionType {
    ACTIVITY,
    NODE
}

/**
 * Condition for applying the rule
 */
@Serializable
enum class DetectionCondition {
    ONLY_IF,  // only apply if on target
    EXCEPT_IF  // apply for everything except the target
}

/**
 * Possible actions upon detection
 */
@Serializable
enum class DetectionAction {
    KILL_APP,
    BLOCK_INTERNET
}

/**
 * Detection rule for an activity or node
 */
@Serializable
data class DetectionRule(
    val type: DetectionType,
    val targets: List<String> = emptyList(),
    val condition: DetectionCondition = DetectionCondition.ONLY_IF,
    val action: DetectionAction,
)
