This plugin allows for you to record a player that Vulcan has punished from your server and watch the hack at a later time. The hacker could be the only person online and you would still have video evidence of them hacking.

Videos:


​

With this you can have video proof of someone hacking on your server when they appeal and claim to be "hitting grass"

Features Discord Integration. Optionally send a message to a Discord Channel whenever a recording is created.

[​IMG]

Requires Vulcan and AdvancedReplay to be installed and running on your server.

NOTE: Vulcan has to be configured to punish someone in order to record. If players never get punished you will not get a recording.


Setup:
Set "enable-api" in Vulcan's config to "true"
​
For best results, follow this configuration guide for Advanced Replay:

1. Enable MySQL to store the files (This plugin can generate a lot of recordings if you have a lot of hackers. Depending on your recording length this can take up quite a bit of space.)
In AdvancedReplay config.yml change the following values:
Code (YAML):
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
 
Default Configuration:​
Code (YAML):
General:
  Nearby-Range: 30     #How far to look for nearby players to include in the recording? NOTE: The formula is 1/2 of what you put here in each. So it will be 15 blocks in each +x and -x for a total of 30 blocks, etc.
  Recording-Length: 2 #Length in minutes of a recording. Recording will not be created until this time has passed from the start of a recording.
Genral:
  Disabled-Recordings: #Any checks you do not want to record. These are examples, replace/add as many as you want NOTE: Must be lowercase
  - timer
  - strafe
Discord: # Send a recording notification to a Discord Channel
  Enabled: true
  Webhook: Enter webhook here
  Avatar: Enter link to a discord avatar
  Username: VulcanReplay
  Server-Name: Server

Usage:​
This plugin provides no commands or permissions, this is all handled by AdvancedReplay

Basic usage:
/replay list Will print a list of recordings
/replay play <recording> Play a recording
/replay delete <recording> Deletes a recording


Known Issues:
If a player in a recording drops an item on the ground the item will still be there after the recording ends. It is possible to duplicate items with this.
There is nothing I can do about this, I have opened an issue with the AdvancedReplay developer.​

Recording does not show any mobs/entities other than Player's. You may see a player take damage in a recording from what appears to be nothing, but it could be a mob. Again, this is an issue that the AdvancedReplay developer has to fix.

AdvancedReplay does not show a swimming animation. This can make it look like someone is walking on water when they are actually swimming at the surface.

Discord:
Please join my Discord to report any bugs.

This plugin was created by the MinecraftPlanetEarth server. The oldest Earth server since 2013. Come play with us at
mc.minecraftplanetearth.com
[​IMG]


Disclaimer:
This plugin should be used in conjunction with the logs that are provided by Vulcan. This should not be your only proof that a player is hacking​
