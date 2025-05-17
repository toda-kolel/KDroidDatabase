# 📄 Application Policy JSON Contribution Guide

💡 **We accept pull requests *only* containing JSON policy files.** No Kotlin or source‑code changes should be included.

---

## 📂 JSON Policy Schemas Overview

Below is the hierarchical representation of the three supported policy types:

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

---

## 1️⃣ Templates for JSON Files

Choose the template that matches your use-case and copy it:

### 🏷 FixedPolicy

```jsonc
{
  "type": "Fixed",                 // required
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

### 🔄 ModeBasedPolicy

```jsonc
{
  "type": "ModeBased",
  "packageName": "com.whatsapp",
  "category": "COMMUNICATION",
  "minimumVersionCode": 0,
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

### 🧩 MultiModePolicy

```jsonc
{
  "type": "MultiMode",
  "packageName": "com.youtube.tv",
  "category": "VIDEO",
  "minimumVersionCode": 123,
  "modeVariants": [
    {
      "userMode": "GPS_ONLY",
      "variants": [
        {
          "id": "strict",
          "label": "Strict (local only)",
          "policy": { "mode": "LOCAL_ONLY" }
        },
        {
          "id": "balanced",
          "label": "Balanced (whitelist)",
          "policy": {
            "mode": "WHITELIST",
            "spec": { "type": "HostList", "hosts": ["googlevideo.com"] }
          }
        }
      ],
      "defaultVariantId": "balanced"
    },
    {
      "userMode": "MOST_OPEN",
      "variants": [
        {
          "id": "open",
          "label": "Fully open",
          "policy": { "mode": "FULL_OPEN" }
        }
      ]
    }
  ]
}
```

---

## 2️⃣ Contribution Checklist

1. **Fork** the repository and create a new branch.
2. **Select** the appropriate template above.
3. **Edit** the placeholders with your real data.
4. **Validate** your JSON with a linter (e.g., [jsonlint.com](https://jsonlint.com/)).
5. **Place** the file under `app-policies/<category>/<packageName>.json`.
6. **Commit** *only* the JSON file and open a PR with a clear title & description.

🚦 CI will fail if the JSON is invalid or located in the wrong folder.

---

## 3️⃣ Directory Layout

```text
app-policies/
  communication/
    org.signal.json
  video/
    com.youtube.tv.json
  …
```

Each category has its own folder for easy navigation and to avoid merge conflicts.

---

## 💬 Need Help?

Open an issue or mention **@your-team** in the PR—responses within 24 h. 🙌
