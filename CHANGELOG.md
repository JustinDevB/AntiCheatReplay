# Changelog

All notable changes to AntiCheatReplay are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Fixed
- Fix Spartan API `NoSuchMethodError` for `getHackType()` (branch: fix/spartan-api-compat)
- Fix recordings failing after first anticheat trigger (branch: fix/recording-state-bugs)

### Changed
- Bump version to 3.0.4 (branch: fix/recording-state-bugs)

---

## [3.0.3] - 2026-04-11

### Added
- Re-added support for GrimAC

### Fixed
- Fix recording length being in seconds instead of minutes
- Fix recording length then being in hours instead of minutes
- Properly handle saving recording lengths with BetterReplay v1.3

### Changed
- Update upload-artifact GitHub Action to version 4

---

## [3.0.0] - 2026-03-14

### Added
- Folia support
- Switched to BetterReplay instead of AdvancedReplay

### Changed
- Major version bump to 3.0.0 due to new replay plugin and Folia support

---

## [2.8.x] - 2025-07-20

### Added
- Added support for Vacan anticheat
- Added support for Gladiator AntiCheat

### Removed
- Dropped support for Grim due to repeated API breakage

### Fixed
- Fixed Grim breaking their API (multiple times, 2025-04 and 2025-06)

### Changed
- Bump version to 1.21.6 (latest AdvancedReplay-supported version)
- Version bump (2025-03-20)

---

## [2.7.x] - 2024-06-18

### Added
- Fix to correctly name Matrix violations
- Updated to latest LightAntiCheat and fixed compatibility
- Fix KarhuAC support (PR #31)

### Fixed
- Reverts #35 due to recordings no longer working
- Reverted AdvancedReplay API to system path instead of Jitpack (Jitpack caused NPE at runtime)

### Changed
- Updated to Minecraft 1.21
- Updated AdvancedReplay dependency to latest version
- Dropped Negativity v2 support
- Version bump (2024-06-18)

---

## [2.6.x] - 2023-07-07

### Added
- Negativity v1 and v2 support (PR #22, PR #23)
- Config option to disable report command
- CI/CD: Created build.yml and maven-publish.yml workflows

### Fixed
- Fix `/report` not properly naming replays
- Fix Sparky not found
- Fix building issues
- Fix typo (PR #28)
- Remove duplicate Sparky link

### Changed
- Updated to support AdvancedReplay 1.19.3
- AdvancedReplay version bump
- Update Spartan and AdvancedReplay dependencies to use repositories instead of local files
- Internal version bump
- Update readme formatting
- Remove unused imports

---

## [2.6.6] - 2022-02-16

### Added
- Support for GrimAC
- New config options
- Report cooldown feature
- LightAntiCheat support
- Intave support
- Sparky AntiCheat support (PR #10)
- Artemis support
- AntiCheatReloaded support
- More AntiCheats supported

### Fixed
- Fixed Grim compatibility (multiple fixes)
- Fixed recording notification being white
- Fixed FlappyAC enum
- Updated VulcanListener
- ListenerBase updates (PR #17)

### Changed
- Rewamped plugin architecture (PR #9)
- Plugin renamed (name change refactoring)
- Config auto-update feature
- Quality of Life update
- Changed update notification from error to info
- Small refactor and bug fixes
- 1.19 version bump
- Dropped then re-added Soaroma support
- API Update
- README rewrite

---

## [v2.5] - 2022-01-07

### Added
- API implementation (events: `PlayerReportEvent`, `RecordingSaveEvent`, `RecordingStartEvent`, `WebhookSendEvent`)
- FlappyAC support
- SoaromaSAC support

### Fixed
- Online timestamp correction
- Replay name length fix

### Changed
- Refactoring and cleanup
- Added .gitignore
- Removed build artifacts from repository

---

## [v2.1] - 2021-12-30

### Added
- Karhu support
- Kauri support
- Themis support
- BStats chart integration
- Reload command (`/acreplay reload`)

### Fixed
- Fixed activeListener bug
- Fixed Kauri support
- Bug fixes
- Fixed rare issue where VulcanReplay would refuse to enable

### Changed
- Complete recode of the plugin

---

## [v1.4] - 2021-11-24

### Added
- Update checker

### Changed
- General updates

---

## [v1.3] - 2021-11-24

### Fixed
- Check if API is enabled before use

---

## [v1.2] - 2021-11-24

### Added
- Version support

---

## [v1.0] - 2021-11-23

### Added
- Initial release
- Vulcan anticheat support
- Discord webhook notifications
- AdvancedReplay integration for recording suspicious players
- License (GPL-3.0)

### Fixed
- Fixed webhook
- Bug fix
- Removed useless Runnable
