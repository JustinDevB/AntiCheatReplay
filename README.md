This plugin records suspicious players so you can review the incident later in BetterReplay. Recordings can start from supported AntiCheat alerts and punishments, and staff can also trigger a recording manually with `/report`.

See the [Changelog](CHANGELOG.md) for a full history of changes across all versions.

## AntiCheats:

**Premium AntiCheats**:
 - [Gladiator](https://www.spigotmc.org/resources/gladiator-anticheat.122383/)
 - [Vulcan](https://www.spigotmc.org/resources/83626/)
 - [Spartan/Vacan](https://www.spigotmc.org/resources/25638/)
 - [Matrix](https://matrix.rip/)
 - [GodsEye](https://www.spigotmc.org/resources/69595/)
 - [Kauri](https://www.spigotmc.org/resources/53721/)
 - [Karhu](https://karhu.cc/)
 - [Verus](https://verus.ac/)
 - [Intave](https://intave.de/)
 - [Sparky](https://sparky.ac/)
 - [Negativity V2](https://www.spigotmc.org/resources/86874/)
 - [AstroAC](https://astroac.cc/)

**Free AntiCheats**:
 - [Themis](https://www.spigotmc.org/resources/90766/)
 - [FlappyAC](https://www.spigotmc.org/resources/92180/)
 - [AntiCheatReloaded](https://www.spigotmc.org/resources/23799/)
 - [LightAntiCheat](https://www.spigotmc.org/resources/96341/)
 - [AntiHaxerman](https://www.spigotmc.org/resources/83198/)
 - [Negativity V1](https://www.spigotmc.org/resources/48399/)
 - [GrimAC](https://modrinth.com/plugin/grimac)


Feel free to request other AntiCheats to be supported.​

Videos:

- [[AntiCheatReplayExample1]](https://youtu.be/P88KS4W8IGI)
- [[AntiCheatReplayExample2]](https://youtu.be/YDNmiOYlvq8)
- [[AntiCheatReplayExample3]](https://youtu.be/znMqh0mWuyI)


With this you can have video proof of someone hacking on your server when they appeal and claim to be "hitting grass"

Features Discord Integration. Optionally send a message to a Discord Channel whenever a recording is created.
![Discord Webhook Example](https://www.spigotmc.org/attachments/capture-png.665322/)

Requires a supported AntiCheat and BetterReplay to be installed and running on your server.

NOTE: AntiCheat-driven recordings still depend on the integrations your AntiCheat exposes. For most integrations, a flag starts the recording and a punish event causes it to be saved. The `/report` command can also start and save a recording without relying on a punish event.


### Setup:
- Vulcan: Set `enable-api` in Vulcan's config to "true"
- Soaroma: Set `enableAPI` in Soaroma's config to "true"
- Sparky: Set `API.Events` in Sparky's config to "true"

For best results, follow this configuration guide for BetterReplay:

Enable MySQL to store the files (This plugin can generate a lot of recordings if you have a lot of hackers. Depending on your recording length this can take up quite a bit of space.)
  In BetterReplay `config.yml` change the following values:
```
   cleanup_replays: 10 # This makes replays delete when they're 10 days old
   hide_players: true
   replaying:
   world:
   reset_changes: true
   recording:
   blocks:
   real_changes: false
   chat:
   enabled: true
```
### Default Configuration:
```YAML
General:
  Overwrite: false  # Should we overwrite a recording if a player did the same hack on the same date?
  Check-Update: true  # Be notified when this plugin updates
  Nearby-Range: 30  # How far to look for nearby players to include in the recording?
  Recording-Length: 2  # Length in minutes of a recording.
  Notify-Staff: true  # Notify staff with AntiCheatReplay.recording-notify when a replay is saved.
  Save-Recording-On-Disconnect: false
  Always-Save-Recording: false
  Report-Cooldown: 3  # Cooldown in minutes between reports per player.
  Report-Enabled: true
Vulcan:
  Disabled-Recordings: # Any checks you do not want to record. Must be lowercase.
    - timer
    - strafe
Themis:
  Disabled-Actions:   # Themis actions to disable, refer to the Themis config.yml.
    - notify
Discord: # Send a recording notification to a Discord channel.
  Enabled: true
  Webhook: Enter webhook here
  Avatar: https://i.imgur.com/JPG1Kwk.png  # Default Vulcan avatar, feel free to change this.
  Username: VulcanReplay
  Server-Name: Server
```
### Usage:
```
Most replay management commands are handled by BetterReplay. AntiCheatReplay adds a reload command and an optional player report command.

Basic usage:
/replay list                          # List recordings
/replay play <recording>              # Play a recording
/replay delete <recording>            # Delete a recording
/replayreload                         # Reload AntiCheatReplay
/report <player> [reason]             # Start or mark a recording to be saved for the reported player

Permissions:
AntiCheatReplay.reload                # Use /replayreload
AntiCheatReplay.report                # Use /report
AntiCheatReplay.report-notify         # Receive staff notifications when someone files a report
AntiCheatReplay.reportImmune          # Prevent a player from being reported
AntiCheatReplay.recording-notify      # Receive staff notifications when a replay is saved
```


### Discord

Please join my Discord to report any bugs [here](https://discord.gg/vK3wksVdpb).

**Disclaimer:**<br>
This plugin should be used in conjunction with the logs that are provided by your AntiCheat. This should not be your only proof that a player is hacking

## Adding an AntiCheat:

1. Create an Enum in `AntiCheat.java` with the format `ANTICHEAT(String name, String pluginName, Function checker, Function instantiator);` where ANTICHEAT is the Name of your AntiCheat, name should match ANTICHEAT, this is what gets reported to bStats.
- `pluginName` is what your plugin is referred to internally and shows up as when you do /pl on a server.
- `checker` can be null if you do not require any extra plugin for your AntiCheat to run. (Ignore ProtocolLib, we already require it).
- `instantiator` should just call your Listener class. Example: `ourAntiCheatListener::new`

2. Create a Listener. In the package `me.justindevb.anticheatreplay.listeners` create `YourAntiCheatListener.java`

3. This class must extends ListenerBase and implement Listener. View another Listener class to see how to build this out. <br>

4. Final step: Add your plugin(s) as a softdepend in the `plugin.yml`
