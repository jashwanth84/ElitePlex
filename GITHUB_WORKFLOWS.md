# ElitePlex GitHub Actions Workflows Guide

This repository includes automated GitHub Actions workflows for continuous integration and automated APK releases.

---

## 1. Workflows Overview

| Workflow File | Trigger | Purpose | Output Artifacts |
| :--- | :--- | :--- | :--- |
| **`.github/workflows/release-apk.yml`** | Tag push (`v*`) or manual `workflow_dispatch` | Builds, tests, signs, packages, and publishes APKs to GitHub Releases | `ElitePlex-v<ver>-release.apk`<br>`ElitePlex-v<ver>-debug.apk`<br>SHA-256 Checksums |
| **`.github/workflows/android-ci.yml`** | Push or PR to `main` / `master` | Runs automated Robolectric & unit tests and verifies APK compilation | `ElitePlex-Debug-APK` (7-day retention) |

---

## 2. How to Release an APK

### Method A: Git Release Tag (Recommended)
Push any version tag starting with `v`:
```bash
git tag v1.0.0
git push origin v1.0.0
```
The workflow will automatically:
1. Run all Robolectric and unit tests.
2. Build both Release and Debug APKs.
3. Generate SHA-256 checksum verification hashes.
4. Create a formal **GitHub Release** tagged `v1.0.0` with the APK files attached.

### Method B: Manual Workflow Dispatch (GitHub Web UI)
1. Navigate to your repository on GitHub.
2. Click on the **Actions** tab.
3. Select **Release APK** from the left sidebar.
4. Click **Run workflow**.
5. Customize:
   - **Version name**: e.g. `1.0.0`
   - **Release title**: e.g. `ElitePlex v1.0.0 Release`
   - **Release notes**: Details of changes
   - Draft / Pre-release toggles
6. Click **Run workflow** to initiate the build.

---

## 3. Keystore Signing Configuration

The workflow is designed to be **fail-safe and work out-of-the-box**:

### Zero-Configuration Default (Built-in Fallback)
If no repository secrets are provided, the workflow automatically generates a release signing key on-the-fly (`release-upload.jks`) using standard RSA 2048-bit encryption, allowing `assembleRelease` to produce an installable APK immediately.

### Production Keystore (Optional)
To sign with your custom production upload keystore:
1. Encode your `.jks` file to base64:
   ```bash
   base64 -w 0 my-upload-key.jks > keystore_base64.txt
   ```
2. In your GitHub repository, go to **Settings > Secrets and variables > Actions > New repository secret**.
3. Add the following secrets:
   - `KEYSTORE_BASE64`: Paste the base64 string from step 1.
   - `STORE_PASSWORD`: Keystore store password.
   - `KEY_PASSWORD`: Key alias password.
