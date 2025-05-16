package io.github.kdroidfilter.database.core.policies

import io.github.kdroidfilter.database.core.AppCategory
import io.github.kdroidfilter.database.core.NetworkPolicy
import io.github.kdroidfilter.database.core.UserMode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Advanced case: different rules depending on the user mode
@Serializable
@SerialName("ModeBased")
data class ModeBasedPolicy(
    override val packageName: String,
    override val category: AppCategory,
    val modePolicies: Map<UserMode, NetworkPolicy>,
    override val minimumVersionCode: Int,
) : AppPolicy