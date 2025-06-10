package me.justindevb.anticheatreplay;

import java.awt.Color;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import me.justindevb.anticheatreplay.api.events.WebhookSendEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.jumper251.replay.api.ReplayAPI;
import me.justindevb.anticheatreplay.api.events.RecordingSaveEvent;
import me.justindevb.anticheatreplay.api.events.RecordingStartEvent;
import me.justindevb.anticheatreplay.utils.DiscordWebhook;
import me.justindevb.anticheatreplay.utils.Messages;

public abstract class ListenerBase {
	
	protected AntiCheatReplay acReplay;
	protected ReplayAPI replay;
	private boolean saveRecording = false;

	private boolean notifyStaff = false;

	private boolean WEBHOOK_ENABLED = false;
	private String WEBHOOK_URL = "";
	private String WEBHOOK_AVATAR = "";
	private String WEBHOOK_USERNAME = "";
	private String SERVER_NAME = "";
	private double PLAYER_RANGE = 0D;
	private long delay = 2;
	private boolean OVERWRITE = false;
	private boolean SAVE_ON_DISCONNECT = false;
	private boolean ALWAYS_SAVE_RECORDING = false;

	private int RED = 0;
	private int GREEN = 255;
	private int BLUE = 0;


	protected LinkedList<UUID> alertList = new LinkedList<>();
	protected LinkedList<UUID> punishList = new LinkedList<>();

	public ListenerBase(AntiCheatReplay acReplay) {
		this.acReplay = acReplay;
		this.replay = ReplayAPI.getInstance();

		initConfigFields();
	}

	public void disinit() {
		// Do nothing
	}

	/**
	 * Begin recording the selected player
	 * 
	 * @param p Player to record
	 * @param replayName  Name of the recording when saving
	 */
	protected void startRecording(Player p, String replayName) {
		RecordingStartEvent startEvent = new RecordingStartEvent(p, replayName);

		Bukkit.getScheduler().runTask(acReplay, () -> {
			Bukkit.getPluginManager().callEvent(startEvent);
		});

		if (startEvent.isCancelled())
			return;

		acReplay.log("Starting recording of player: " + p.getName(), false);
		Bukkit.getScheduler().runTask(acReplay, () -> {

			replay.recordReplay(replayName, Bukkit.getConsoleSender(), getNearbyPlayers(p));
		});


		runLogic(p, replayName);
	}

	/**
	 * Handle a player that has been reported
	 * @param reporter
	 * @param target
	 * @param reason
	 */
	protected void reportPlayer(Player reporter, Player target, String reason) {
		if (alertList.contains(target.getUniqueId()))
			return;
		alertList.add(target.getUniqueId());
		String replayName = getReplayName(target, "report");
		acReplay.log("Starting recording of player: " + target.getName(), false);
		Bukkit.getScheduler().runTask(acReplay, () -> {
			replay.recordReplay(replayName, Bukkit.getConsoleSender(), getNearbyPlayers(target));
		});

		Bukkit.getScheduler().runTaskLaterAsynchronously(acReplay, () -> {
			replay.stopReplay(replayName, true);
			acReplay.log("Saved a player report:", false);
			acReplay.log(reporter.getName() + " reported " + target.getName() + " for " + reason, false);


			if (alertList.contains(target.getUniqueId()))
				alertList.remove(target.getUniqueId());
		}, 20L * 60L * delay);
	}

	protected void saveRecording() {
		this.saveRecording = true;
	}

	/**
	 * Determine if we will save a player recording or not
	 * 
	 * @param p Player to record
	 * @param replayName name of recording if saving
	 */
	private void runLogic(Player p, String replayName) {
		PlayerCache cachedPlayer = acReplay.getCachedPlayer(p.getUniqueId());
		long loginTime = cachedPlayer.getLoginTimeStamp();
		new BukkitRunnable() {
			@Override
			public void run() {
				if (ALWAYS_SAVE_RECORDING)
					punishList.add(p.getUniqueId());
				if (!p.isOnline() || p == null) {
					if (SAVE_ON_DISCONNECT)
						punishList.add(p.getUniqueId());
				}
				if (punishList.contains(p.getUniqueId())) {
					RecordingSaveEvent saveEvent = new RecordingSaveEvent(p, replayName);

					Bukkit.getScheduler().runTask(acReplay, () -> {
						Bukkit.getPluginManager().callEvent(saveEvent);
					});


					if (saveEvent.isCancelled())
						return;

					replay.stopReplay(replayName, true);
					acReplay.log("Saving recording of attempted hack...", false);
					acReplay.log("Saved as: " + replayName, false);
					sendDiscordWebhook(replayName, p, getOnlineTime(loginTime, System.currentTimeMillis()));
					punishList.remove(p.getUniqueId());

					if (notifyStaff) {
						String notification = Messages.NOTIFY_RECORDING;
						notification = notification.replace("%r", replayName);
						for (Player p : Bukkit.getOnlinePlayers()) {
							if (p.hasPermission("AntiCheatReplay.recording-notify"))
								p.sendMessage(ChatColor.GOLD + notification);
						}
					}

					if (alertList.contains(p.getUniqueId()))
						alertList.remove(p.getUniqueId());

				} else {
					replay.stopReplay(replayName, false);
					if (alertList.contains(p.getUniqueId()))
						alertList.remove(p.getUniqueId());
					acReplay.log("Not saving recording...", false);
				}

			}
		}.runTaskLaterAsynchronously(acReplay, 20L * 60L * delay);
	}


	/**
	 * Get all players within X distance from the target player
	 * 
	 * @param p
	 * @return
	 */
	private Player[] getNearbyPlayers(Player p) {
		final List<Player> nearbyPlayers = new ArrayList<>();
		int i = 0;
		final Collection<Entity> entites = p.getWorld().getNearbyEntities(p.getLocation(), PLAYER_RANGE, PLAYER_RANGE,
				PLAYER_RANGE);

		for (final Entity e : entites)
			if (e instanceof Player) {
				nearbyPlayers.add((Player) e);
				i++;
			}

		if (i == 0) {
			Player[] player = new Player[1];
			player[0] = p;
			return player;
		}

		final Player[] players = new Player[i];
		players[0] = p;

		for (int j = 1; i > 1 && j < nearbyPlayers.size(); j++)
			players[j] = nearbyPlayers.get(j);

		return players;

	}

	/**
	 * Return the Player's online timestamp. Calculated by abs((current time - the
	 * time the player logged in) - recording length delay)
	 * 
	 * @param loginTime
	 * @param currentTime
	 * @return total time in minutes player was online
	 */
	private long getOnlineTime(long loginTime, long currentTime) {

		return (TimeUnit.MILLISECONDS.toMinutes(Math.abs((currentTime - loginTime) - this.delay)));
	}

	/**
	 * Random number between 1 and 999
	 * 
	 * @return
	 */
	private int getRandomSalt() {
		Random rand = new Random();
		int n = 999 - 1 + 1;
		int val = rand.nextInt() % n;
		return Math.abs(val);
	}

	/**
	 * Send a recording notification to Discord
	 * 
	 * @param recording Name of the Recording
	 * @param player that was recorded
	 */
	private void sendDiscordWebhook(String recording, Player player, long minutesOnline) {
		if (!WEBHOOK_ENABLED)
			return;

		WebhookSendEvent webhookSendEvent = new WebhookSendEvent();
		Bukkit.getScheduler().scheduleSyncDelayedTask(acReplay, () -> {
			Bukkit.getPluginManager().callEvent(webhookSendEvent);
		});

		if (webhookSendEvent.isCancelled())
			return;

		Bukkit.getScheduler().runTaskAsynchronously(acReplay, () -> {
			final DiscordWebhook webhook = new DiscordWebhook(WEBHOOK_URL);
			webhook.setAvatarUrl(WEBHOOK_AVATAR);
			webhook.setUsername(WEBHOOK_USERNAME);
			webhook.addEmbed(
					new DiscordWebhook.EmbedObject().setTitle(Messages.TITLE).setDescription(Messages.DESCRIPTION)
							.setThumbnail("https://crafthead.net/avatar/" + player.getName())
							.setColor(new Color(this.RED, this.GREEN, this.BLUE)).addField(Messages.SERVER, SERVER_NAME, true)
							.addField(Messages.ONLINE_FOR, minutesOnline + " " + Messages.ONLINE_FOR_MINUTES, true)
							.addField(Messages.RECORDING_NAME, "`" + recording + "`", true)
							.addField(Messages.COMMAND + " ", "`/replay play " + recording + "`", true));
			acReplay.log("Sending WebHook request...", false);
			try {
				webhook.execute();
				acReplay.log(ChatColor.DARK_GREEN + "Webhook sent!", false);
			} catch (final IOException e) {
				acReplay.log(ChatColor.DARK_RED + "There was an error trying to send the request!", true);
				acReplay.log(ChatColor.DARK_RED + "Webhook URL is most likely incorrect", false);
				e.printStackTrace();

			}
		});
	}

	protected String getTimeStamp() {
		Date date = new Date();
		Timestamp ts = new Timestamp(date.getTime());
		LocalDateTime ldt = ts.toLocalDateTime();
		String time = ldt.toString();

		String[] part = time.split("T");

		if (this.OVERWRITE)
			return part[0];

		return part[0] + "." + getRandomSalt();

	}

	/**
	 * Return the name of the recording. Will be trimmed to never be longer than 40
	 * characters
	 * 
	 * @param p
	 * @param violation
	 * @return Recording Name
	 */
	protected String getReplayName(Player p, String violation) {
		String timeStamp = getTimeStamp();

		if (violation.length() <= 8) // Max character space we have for a violation is 8 characters
			return p.getName() + "-" + violation + "-" + timeStamp;

		return p.getName() + "-" + violation.substring(0, 8) + "-" + timeStamp;
	}

	/**
	 * Initialize our config values
	 */
	private void initConfigFields() {
		FileConfiguration config = acReplay.getConfig();
		this.WEBHOOK_URL = config.getString("Discord.Webhook");
		this.WEBHOOK_AVATAR = config.getString("Discord.Avatar");
		this.WEBHOOK_USERNAME = config.getString("Discord.Username");
		this.SERVER_NAME = config.getString("Discord.Server-Name");
		this.WEBHOOK_ENABLED = config.getBoolean("Discord.Enabled");
		this.PLAYER_RANGE = config.getDouble("General.Nearby-Range");
		this.delay = config.getLong("General.Recording-Length");
		this.OVERWRITE = config.getBoolean("General.Overwrite");
		this.RED = config.getInt("Discord.Red");
		this.GREEN = config.getInt("Discord.Green");
		this.BLUE = config.getInt("Discord.Blue");
		this.notifyStaff = config.getBoolean("General.Notify-Staff");
		this.SAVE_ON_DISCONNECT = config.getBoolean("General.Save-Recording-On-Disconnect");
		this.ALWAYS_SAVE_RECORDING = config.getBoolean("General.Always-Save-Recording");
	}

	public boolean isSaveRecording() {
		return saveRecording;
	}
}
