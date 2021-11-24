package me.justindevb.VulcanReplay;

import java.awt.Color;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import me.frep.vulcan.api.event.VulcanFlagEvent;
import me.frep.vulcan.api.event.VulcanPunishEvent;
import me.jumper251.replay.api.ReplayAPI;

public class VulcanListener implements Listener {

	private final VulcanReplay vulcanReplay;
	private static LinkedList<String> alertList = new LinkedList<>();
	private static LinkedList<String> punishList = new LinkedList<>();
	private List<String> disabledRecordings = new ArrayList<>();

	private boolean WEBHOOK_ENABLED = false;
	private String WEBHOOK_URL = "";
	private String WEBHOOK_AVATAR = "";
	private String WEBHOOK_USERNAME = "";
	private String SERVER_NAME = "";
	private double PLAYER_RANGE = 0D;
	private long delay = 2;

	final ReplayAPI replay = ReplayAPI.getInstance();

	public VulcanListener(VulcanReplay vulcanReplay) {
		this.vulcanReplay = vulcanReplay;

		initConfigFields();

	}

	@EventHandler
	public void onFlagEvent(VulcanFlagEvent event) {
		if (disabledRecordings.contains(event.getCheck().getName().toLowerCase()))
			return;

		final Player p = event.getPlayer();

		if (alertList.contains(p.getName()))
			return;

		alertList.add(p.getName());

		final String replayName = p.getName() + "-" + event.getCheck().getName() + "-" + getTimeStamp();

		startRecording(p, replayName);

		runLogic(p, replayName);

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPunish(VulcanPunishEvent event) {
		final Player p = event.getPlayer();

		Bukkit.getScheduler().runTask(vulcanReplay, () -> {
		});

		if (!punishList.contains(p.getName()))
			punishList.add(p.getName());

	}

	/**
	 * Begin recording the selected player
	 * 
	 * @param Player to record
	 * @param Name   of the recording when saving
	 */
	private void startRecording(Player p, String replayName) {
		vulcanReplay.log("Starting recording of player: " + p.getName(), false);
		Bukkit.getScheduler().runTask(vulcanReplay, () -> {

			replay.recordReplay(replayName, Bukkit.getConsoleSender(), getNearbyPlayers(p));
		});

	}

	/**
	 * Determine if we will save a player recording or not
	 * 
	 * @param Player to record
	 * @param Name   of recording if saving
	 */
	private void runLogic(Player p, String replayName) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (punishList.contains(p.getName())) {
					replay.stopReplay(replayName, true);
					vulcanReplay.log("Saving recording of attempted hack...", false);
					vulcanReplay.log("Saved as: " + replayName, false);
					sendDiscordWebhook(replayName, p);
					punishList.remove(p.getName());

					if (alertList.contains(p.getName()))
						alertList.remove(p.getName());

				} else {
					replay.stopReplay(replayName, false);
					if (alertList.contains(p.getName()))
						alertList.remove(p.getName());
					vulcanReplay.log("Not saving recording...", false);
				}

			}
		}.runTaskLater(vulcanReplay, 20L * 60L * delay);
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

		final Player[] players = new Player[i];
		players[0] = p;

		for (int j = 1; i > 1 && j < nearbyPlayers.size(); j++)
			players[j] = nearbyPlayers.get(j);

		return players;

	}

	/**
	 * Send a recording notification to Discord
	 * 
	 * @param Name   of the Recording
	 * @param Player that was recorded
	 */
	private void sendDiscordWebhook(String recording, Player player) {
		if (!WEBHOOK_ENABLED)
			return;

		Bukkit.getScheduler().runTaskAsynchronously(vulcanReplay, () -> {
			final DiscordWebhook webhook = new DiscordWebhook(WEBHOOK_URL);
			webhook.setAvatarUrl(WEBHOOK_AVATAR);
			webhook.setUsername(WEBHOOK_USERNAME);
			webhook.addEmbed(
					new DiscordWebhook.EmbedObject().setTitle("Instant Replay").setDescription("Recording created")
							.setThumbnail("http://cravatar.eu/avatar/" + player.getName() + "/64.png")
							.setColor(new Color(0, 255, 0)).addField("Server: ", SERVER_NAME, true)
							.addField("Recording saved as", recording, true)
							.addField("View with: ", "/replay play " + recording, true));
			vulcanReplay.log("Sending WebHook request...", false);
			try {
				webhook.execute();
				vulcanReplay.log(ChatColor.DARK_GREEN + "Webhook sent!", false);
			} catch (final IOException e) {
				e.printStackTrace();
				vulcanReplay.log(ChatColor.DARK_RED + "There was an error trying to send the request!", false);
			}
		});
	}

	private String getTimeStamp() {
		Date date = new Date();
		Timestamp ts = new Timestamp(date.getTime());
		LocalDateTime ldt = ts.toLocalDateTime();
		String time = ldt.toString();

		String[] part = time.split("T");

		return part[0];
	}

	private void initConfigFields() {
		FileConfiguration config = vulcanReplay.getConfig();
		this.WEBHOOK_URL = config.getString("Discord.Webhook");
		this.WEBHOOK_AVATAR = config.getString("Discord.Avatar");
		this.WEBHOOK_USERNAME = config.getString("Discord.Username");
		this.SERVER_NAME = config.getString("Discord.Server-Name");
		this.WEBHOOK_ENABLED = config.getBoolean("Discord.Enabled");
		this.PLAYER_RANGE = config.getDouble("General.Nearby-Range");

		this.disabledRecordings = config.getStringList("General.Disabled-Recordings");
		this.delay = config.getLong("General.Recording-Length");
	}

}
