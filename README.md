This plugin allows for you to record a player that your AntiCheat has punished from your server and watch the hack at a later time. The hacker could be the only person online and you would still have video evidence of them hacking.

 AntiCheats:
<details>
  <summary>Premium AntiCheats</summary>
  [Vulcan](https://www.spigotmc.org/resources/fundraiser-vulcan-anti-cheat-advanced-cheat-detection-1-7-1-17-1.83626/)
  [Spartan](https://www.spigotmc.org/resources/spartan-anti-cheat-advanced-cheat-detection-hack-blocker-1-7-2-1-18.25638/)
  [Matrix](https://matrix.rip/)
  [GodsEye](https://www.spigotmc.org/resources/godseye-anticheat-advanced-detection.69595/)
  [Kauri](https://www.spigotmc.org/resources/kauri-anticheat-best-in-class-performance-patches-log4j-removes-closet-cheaters-1-7-10-1-18-1.53721/)
  [Karhu](https://karhu.cc/)
  [Verus](https://verus.ac/)
  [Intave](https://intave.de/)
</details>

<details>
  <summary>Free AntiCheats</summary>
  [Themis](https://www.spigotmc.org/resources/themis-anti-cheat-1-17-1-18-bedrock-support-paper-compatibility-free-optimized.90766/)
  [Soaroma](https://www.spigotmc.org/resources/soaromasac-lightweight-cheat-detection-system.87702/)
  [FlappyAC](https://www.spigotmc.org/resources/flappy-anticheat-1-13-1-17.92180/)
  [AntiCheatReloaded](https://www.spigotmc.org/resources/anticheatreloaded.23799/)
</details>

Feel free to request other AntiCheats to be supported.​

Videos:
<details>
<summary>Videos of AntiCheat Replay recording hackers</summary>
[![Video 1]](https://youtu.be/P88KS4W8IGI)
[![Video 2]](https://youtu.be/YDNmiOYlvq8)
[![Video 3]](https://youtu.be/znMqh0mWuyI)
</details>

With this you can have video proof of someone hacking on your server when they appeal and claim to be "hitting grass"

Features Discord Integration. Optionally send a message to a Discord Channel whenever a recording is created.
Capture.PNG​

Requires a supported AntiCheat and AdvancedReplay to be installed and running on your server.

NOTE: Your AntiCheat has to be configured to punish someone in order to record. If players never get punished you will not get a recording.


Setup:
Vulcan: Set "enable-api" in Vulcan's config to "true"
Soaroma: Set "enableAPI" in Soaroma's config to "true"

For best results, follow this configuration guide for Advanced Replay:

1. Enable MySQL to store the files (This plugin can generate a lot of recordings if you have a lot of hackers. Depending on your recording length this can take up quite a bit of space.)
   In AdvancedReplay config.yml change the following values:
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
Default Configuration:
```YAML
General:
Overwrite: false  #Should we overwrite a recording if a player did the same hack on the same date?
Check-Update: true  #Be notified when this plugin updates
Nearby-Range: 30     #How far to look for nearby players to include in the recording? NOTE: The formula is 1/2 of what you put here in each. So it will be 15 blocks in each +x and -x for a total of 30 blocks, etc.
Recording-Length: 2 #Length in minutes of a recording. Recording will not be created until this time has passed from the start of a recording.
Vulcan:
Disabled-Recordings: #Any checks you do not want to record. These are examples, replace/add as many as you want NOTE: Must be lowercase
- timer
- strafe
  Themis:
  Disabled-Actions:   #Themis Actions to disable, refer to Themis config.yml
- notify
  Discord: # Send a recording notification to a Discord Channel
  Enabled: true
  Webhook: Enter webhook here
  Avatar: https://i.imgur.com/JPG1Kwk.png     #Default Vulcan avatar, feel free to change this
  Username: VulcanReplay
  Server-Name: Server
```
Usage:
```
Most commands and permissions are handled by AdvancedReplay The only command AntiCheatReplay adds is a reload command

Basic usage:
/replay list Will print a list of recordings
/replay play <recording> Play a recording
/replay delete <recording> Deletes a recording
/replayreload vulcanreplay.reload Reload AntiCheatReplay
```
Known Issues:

Recording does not show any mobs/entities other than Players. You may see a player take damage in a recording from what appears to be nothing, but it could be a mob. Again, this is an issue that the AdvancedReplay developer has to fix.


Discord:
Please join my Discord to report any bugs.
https://discord.gg/vK3wksVdpb


Disclaimer:
This plugin should be used in conjunction with the logs that are provided by your AntiCheat. This should not be your only proof that a player is hacking