package io.github.kdroidfilter.database.core

import io.github.kdroidfilter.database.core.policies.AppPolicy
import io.github.kdroidfilter.database.core.policies.FixedPolicy
import io.github.kdroidfilter.database.core.policies.ModeBasedPolicy
import io.github.kdroidfilter.database.core.policies.MultiModePolicy

fun resolvePolicy(policy: AppPolicy, userMode: UserMode, variantId: String? = null): NetworkPolicy {
    return when (policy) {
        is FixedPolicy -> policy.networkPolicy
        is ModeBasedPolicy -> policy.modePolicies[userMode]
            ?: NetworkPolicy(NetworkMode.LOCAL_ONLY) // fallback if mode missing
        is MultiModePolicy -> policy.resolve(userMode, variantId)
    }
}
