package me.justindevb.anticheatreplay.listeners;

import me.justindevb.anticheatreplay.api.events.PlayerReportEvent;
import me.justindevb.anticheatreplay.utils.DiscordWebhook;
import me.justindevb.anticheatreplay.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.justindevb.anticheatreplay.PlayerCache;
import me.justindevb.anticheatreplay.AntiCheatReplay;

import java.awt.*;
import java.io.IOException;

public class PlayerListener implements Listener {

	protected AntiCheatReplay acReplay;
	protected boolean saveOnDisconnect = false;

	private String WEBHOOK_URL;
	private String WEBHOOK_AVATAR;
	private String WEBHOOK_USERNAME;
	private String SERVER_NAME;
	private boolean WEBHOOK_ENABLED;
	private int RED = 0;
	private int GREEN = 255;
	private int BLUE = 0;
	private long delay;

	public PlayerListener(AntiCheatReplay acReplay) {
		this.acReplay = acReplay;
		this.saveOnDisconnect = acReplay.getConfig().getBoolean("General.Save-Recording-On-Disconnect");

		initConfigValues();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		PlayerCache cachedPlayer = new PlayerCache(p, acReplay);
		acReplay.putCachedPlayer(p.getUniqueId(), cachedPlayer);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(acReplay, () -> {
			acReplay.removeCachedPlayer(p.getUniqueId());
		}, 10L);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onReport(PlayerReportEvent event) {
		Player reporter = event.getReporter();
		Player target = event.getTarget();
		String reason = event.getReason();

		sendDiscordWebhook(target.getName() + "-report", reason, reporter, target);


	}

	//TODO: Refactor out so we don't have duplicate methods
	private void sendDiscordWebhook(String recordingName, String reason, Player reporter, Player target) {
		if (!WEBHOOK_ENABLED)
			return;

		Bukkit.getScheduler().runTaskLaterAsynchronously(acReplay, () -> {
			final DiscordWebhook webhook = new DiscordWebhook(WEBHOOK_URL);
			webhook.setAvatarUrl(WEBHOOK_AVATAR);
			webhook.setUsername(WEBHOOK_USERNAME);
			webhook.addEmbed(
					new DiscordWebhook.EmbedObject().setTitle(Messages.REPORT_TITLE).setDescription(Messages.REPORT_DESCRIPTION)
							.setThumbnail("https://crafthead.net/avatar/" + target.getName())
							.setColor(new Color(this.RED, this.GREEN, this.BLUE)).addField(Messages.SERVER, SERVER_NAME, true)
							.addField("Reporter:", reporter.getName(), true)
							.addField("Player Reported:", target.getName(), true)
							.addField("Reason:", reason, true)
							.addField(Messages.RECORDING_NAME, "`" + recordingName + "`", true)
							.addField(Messages.COMMAND + " ", "`/replay play " + recordingName + "`", true));
			acReplay.log("Sending WebHook request...", false);
			try {
				webhook.execute();
				acReplay.log(ChatColor.DARK_GREEN + "Webhook sent!", false);
			} catch (final IOException e) {
				acReplay.log(ChatColor.DARK_RED + "There was an error trying to send the request!", true);
				acReplay.log(ChatColor.DARK_RED + "Webhook URL is most likely incorrect", false);
				e.printStackTrace();

			}
		}, 20L * 60L * delay);
	}

	private void initConfigValues() {
		FileConfiguration config = acReplay.getConfig();
		this.WEBHOOK_URL = config.getString("Discord.Webhook");
		this.WEBHOOK_AVATAR = config.getString("Discord.Avatar");
		this.WEBHOOK_USERNAME = config.getString("Discord.Username");
		this.SERVER_NAME = config.getString("Discord.Server-Name");
		this.WEBHOOK_ENABLED = config.getBoolean("Discord.Enabled");
		this.RED = config.getInt("Discord.Red");
		this.GREEN = config.getInt("Discord.Green");
		this.BLUE = config.getInt("Discord.Blue");
		this.delay = config.getLong("General.Recording-Length");
	}

}
