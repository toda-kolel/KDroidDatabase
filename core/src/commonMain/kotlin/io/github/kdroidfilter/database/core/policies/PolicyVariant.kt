package io.github.kdroidfilter.database.core.policies

import io.github.kdroidfilter.database.core.AppCategory
import io.github.kdroidfilter.database.core.NetworkPolicy
import io.github.kdroidfilter.database.core.UserMode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Free identifier for the variant; used as a display key in the UI. */
@Serializable
data class PolicyVariant(
    val id: String,            // e.g. "strict", "balanced", "relaxed"
    val label: String,         // e.g. "Strict (no external hosts)"
    val policy: NetworkPolicy  // associated filtering rule
)

/** Group of variants available for a given UserMode. */
@Serializable
data class ModeVariants(
    val userMode: UserMode,
    val variants: List<PolicyVariant>,
    val defaultVariantId: String = variants.first().id
)

/** Complete policy: several user modes, each with multiple variants. */
@Serializable @SerialName("MultiMode")
data class MultiModePolicy(
    override val packageName: String,
    override val category: AppCategory,
    val modeVariants: List<ModeVariants>,
    override val minimumVersionCode: Int,
) : AppPolicy {

    /** Returns the effective NetworkPolicy for the specified mode and variant. */
    fun resolve(mode: UserMode, chosenId: String? = null): NetworkPolicy {
        val mv = modeVariants.first { it.userMode == mode }
        val id = chosenId ?: mv.defaultVariantId
        return mv.variants.first { it.id == id }.policy
    }
}
