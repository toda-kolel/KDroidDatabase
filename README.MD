# 📄 Application Policy **JSON** Contribution Guide

💡 **We accept pull requests *only* containing JSON policy files.** No Kotlin or source‑code changes are required for a typical policy contribution.

---

## 1️⃣ JSON Structures

Below are the three supported shapes. Choose the one that matches your use‑case and copy the template.

### 🏷 FixedPolicy

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

### 🔄 ModeBasedPolicy

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

### 🧩 MultiModePolicy (with variants)

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

## 2️⃣ Contribution Checklist 📋

1. **Fork** the repository and create a feature branch.
2. Pick the template above that matches your needs.
3. Replace the placeholder values with real data.
4. **Validate** your JSON with [https://jsonlint.com/](https://jsonlint.com/) or any linter of your choice.
5. Save the file under:

   ```text
   app-policies/<category>/<packageName>.json
   ```
6. Commit 💾 **only the JSON file** and open a PR. Provide a clear title & description.

🚦 *CI will fail if the file is not valid JSON or is placed in the wrong folder.*

---

## 3️⃣ Directory Layout 📂

```
app-policies/
  communication/
    org.signal.json
  video/
    com.youtube.tv.json
  …
```

Each category maps to its own folder for easier browsing and merge‑conflict avoidance.

---

## 4️⃣ Best Practices 💡

| ✅ Do                                       | 🚫 Don’t                                   |
| ------------------------------------------ | ------------------------------------------ |
| Use **2‑space indentation**                | Commit tabs or mixed spacing               |
| Keep keys in **lowerCamelCase**            | Add extra, undocumented keys               |
| Re‑use **stable `id` values** for variants | Force‑push breaking changes without notice |
| Group related apps in one PR               | Mix unrelated apps & categories            |

---

## 5️⃣ Reference Values 🧭

**NetworkMode**: `FULL_OPEN`, `BLACKLIST`, `WHITELIST`, `LOCAL_ONLY`
**ModeSpec.type**: `None`, `HostList`
**UserMode**: `OFFLINE`, `LOCAL_ONLY`, `GPS_ONLY`, `GPS_AND_MAIL`, `REDUCED_RISK`, `MOST_OPEN`
**AppCategory** (excerpt): `COMMUNICATION`, `PRODUCTIVITY`, `VIDEO`, …

---

### 💬 Need Help?

Open an issue or mention **@your‑team** in the PR—responses within 24 h. 🙌
