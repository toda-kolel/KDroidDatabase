package io.github.kdroidfilter.database.core.policies

import io.github.kdroidfilter.database.core.AppCategory
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppPolicy {
    val packageName: String
    val category: AppCategory
    val minimumVersionCode: Int
}


