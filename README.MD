# 📄 JSON Policy Contribution Guide

✨ **We accept pull requests *only* containing JSON policy files.** No code changes or Kotlin sources.

---

```text
AppPolicy
├── FixedPolicy
│   ├── "type": "Fixed"
│   ├── networkPolicy
│   │   ├── mode: FULL_OPEN | BLACKLIST | WHITELIST | LOCAL_ONLY
│   │   └── spec: None | HostList{hosts…}
│   └── detectionRules […]
│
├── ModeBasedPolicy
│   ├── "type": "ModeBased"
│   ├── modePolicies
│   │   ├── OFFLINE → NetworkPolicy{…}
│   │   ├── GPS_ONLY → NetworkPolicy{…}
│   │   ├── GPS_AND_MAIL → NetworkPolicy{…}
│   │   ├── REDUCED_RISK → NetworkPolicy{…}
│   │   └── MOST_OPEN → NetworkPolicy{…}
│   └── detectionRules […]
│
└── MultiModePolicy
    ├── "type": "MultiMode"
    ├── modeVariants
    │   ├── userMode: GPS_ONLY
    │   │   ├── variants
    │   │   │   ├── id: "strict",   policy: {mode: LOCAL_ONLY}
    │   │   │   └── id: "balanced", policy: {mode: WHITELIST, spec: HostList[…]}
    │   │   └── defaultVariantId: "balanced"
    │   └── userMode: MOST_OPEN
    │       ├── variants
    │       │   └── id: "open", policy: {mode: FULL_OPEN}
    │       └── defaultVariantId: "open"
    └── detectionRules […]
```

## 🗂️ Directory Structure

Place your JSON file under:

```
app-policies/<category>/<packageName>.json
```

Example:

```
app-policies/navigation/com.example.app.json
```

Each category folder (e.g., `communication`, `navigation`, `video`) helps avoid merge conflicts and keeps things tidy. 🚀

---

## 📐 Supported Policy Types & Templates

Below are the three policy types. Copy the template that matches your use case and fill in your data.

### 1️⃣ FixedPolicy

Use when the same network rules apply to all modes.

```json
{
  "type": "Fixed",
  "packageName": "org.signal",
  "category": "COMMUNICATION",
  "minimumVersionCode": 0,
  "networkPolicy": {
    "mode": "WHITELIST",
    "spec": {
      "type": "HostList",
      "hosts": ["signal.org"]
    }
  }
}
```

### 2️⃣ ModeBasedPolicy

Use when you need different rules per user mode.

```json
{
  "type": "ModeBased",
  "packageName": "com.whatsapp",
  "category": "COMMUNICATION",
  "minimumVersionCode": 100,
  "modePolicies": {
    "OFFLINE":  { "mode": "LOCAL_ONLY" },
    "GPS_ONLY": {
      "mode": "WHITELIST",
      "spec": { "type": "HostList", "hosts": ["whatsapp.com"] }
    },
    "MOST_OPEN": { "mode": "FULL_OPEN" }
  }
}
```

### 3️⃣ MultiModePolicy

Use when each user mode has multiple variants, each with its own rules and optional activity/node detections.

```json
{
  "type": "MultiMode",
  "packageName": "com.whatsapp",
  "category": "COMMUNICATION",
  "minimumVersionCode": 0,
  "modeVariants": [
    {
      "userMode": "MOST_OPEN",
      "variants": [
        {
          "id": "open",
          "label": "Fully open",
          "policy": {
            "mode": "FULL_OPEN"
          }
        },
        {
          "id": "restricted",
          "label": "Only messages, no photos, videos and calling",
          "policy": {
            "mode": "WHITELIST",
            "spec": {
              "type": "HostList",
              "hosts": [
                "v.whatsapp.net",
                "static.whatsapp.net",
                "firebaseinstallations.googleapis.com",
                "time.android.com",
                "dit.whatsapp.net",
                "e1.whatsapp.net",
                "mmg.whatsapp.net",
                "g.whatsapp.net"
              ]
            }
          }
        },
        {
          "id": "block_groups",
          "label": "Block groups",
          "policy": {
            "mode": "FULL_OPEN"
          },
          "detectionRules": [
            {
              "type": "NODE",
              "targets": [
                "TODO"
              ],
              "condition": "ONLY_IF",
              "action": "KILL_APP"
            }
          ],
          "overrideDefaultRules" : false,
          "configurationRequired" : true,
          "configurationKey" : "whatsapp_groups_prefs"
        }
      ],
      "defaultVariantId": "open"
    }
  ],
  "detectionRules": [
    {
      "type": "NODE",
      "targets": [
        "com.whatsapp:id/newsletter_quick_forwarding_pill_container_key"
      ],
      "condition": "ONLY_IF",
      "action": "KILL_APP",
      "desc": "Kill app when entering on WhatsApp Update channel"
    }
  ]
}
```

* `detectionRules` at the root apply across all variants.
* Within each variant:

    * `detectionRules`: rules specific to that variant.
    * `overrideDefaultRules` (boolean):

        * `true` (default): only variant rules apply.
        * `false`: merge root rules + variant rules (set explicitly).

---

🧩 About configurationRequired and configurationKey
Some MultiModePolicy variants may require user configuration (for example, specifying allowed or blocked WhatsApp groups). To support this, a variant can define:

configurationRequired: true → Signals the app that this variant needs extra user input (e.g., group names).

configurationKey: "..." → A unique key used to link user input to the policy variant.

The app is expected to query this configuration during enforcement. The backend does not define values—only the key.



## ✅ Checklist Before Submitting

1. 🔎 **Validate JSON** with a linter (e.g., [jsonlint.com](https://jsonlint.com/)).
2. 📂 **Place** your file under the correct category folder.
3. 📜 **Commit only** the JSON file—no code or docs changes.
4. 📝 **PR title** should clearly state the app package and policy type.

CI will reject invalid JSON or misplaced files. Good luck! 🙌
