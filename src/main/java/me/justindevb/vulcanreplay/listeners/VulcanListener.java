package me.justindevb.vulcanreplay.listeners;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import me.frep.vulcan.api.event.VulcanFlagEvent;
import me.frep.vulcan.api.event.VulcanPunishEvent;

import me.justindevb.vulcanreplay.ListenerBase;
import me.justindevb.vulcanreplay.VulcanReplay;

public class VulcanListener extends ListenerBase implements Listener {

	private final VulcanReplay vulcanReplay;
	private List<String> disabledRecordings = new ArrayList<>();

	public VulcanListener(VulcanReplay vulcanReplay) {
		super(vulcanReplay);
		this.vulcanReplay = vulcanReplay;

		setupVulcan();

	}

	@Override
	public void unregister() {
		HandlerList.unregisterAll(this);
	}

	@Override
	public void register() {
		Bukkit.getPluginManager().registerEvents(this, VulcanReplay.getInstance());
	}

	private void setupVulcan() {
		checkVulcanApi();
		initVulcanSpecificConfig();

	}

	@EventHandler
	public void onFlagEvent(VulcanFlagEvent event) {
		if (disabledRecordings.contains(event.getCheck().getName().toLowerCase()))
			return;

		final Player p = event.getPlayer();

		if (alertList.contains(p.getName()))
			return;

		alertList.add(p.getName());

		final String replayName = p.getName() + "-" + event.getCheck().getName() + "-" + super.getTimeStamp();

		startRecording(p, replayName);

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPunish(VulcanPunishEvent event) {
		final Player p = event.getPlayer();

		if (!punishList.contains(p.getName()))
			punishList.add(p.getName());

	}

	/**
	 * Check to see if the Vulcan API is enabled in Vulcan's config.yml
	 */
	private void checkVulcanApi() {
		vulcanReplay.log("Checking if Vulcan API is enabled", false);
		File file = new File(vulcanReplay.getDataFolder().getParentFile(),
				"Vulcan" + System.getProperty("file.separator") + "config.yml");
		if (!file.exists()) {
			vulcanReplay.log("Vulcan is not installed!", true);
			return;
		}

		FileConfiguration vulcan = YamlConfiguration.loadConfiguration(file);

		if (vulcan.getBoolean("settings.enable-api")) {
			vulcanReplay.log("Vulcan API is enabled", false);
			return;
		}
		

		vulcanReplay.log("Vulcan API is disabled in Vulcan's config.yml. This must be true for this plugin to work!",
				true);
		vulcanReplay.log("We went ahead and changed it to true, but you need to reboot your server for it to take effect!", true);
		vulcan.set("settings.enable-api", true);
		try {
			vulcan.save(file);
		} catch (IOException e) {
			vulcanReplay.log("Error editing Vulcan config. You will have to manually do it", true);
			e.printStackTrace();
		}
		Bukkit.getScheduler().runTask(vulcanReplay, () -> {
			Bukkit.getPluginManager().disablePlugin(vulcanReplay);
		});
	}

	public void initVulcanSpecificConfig() {
		this.disabledRecordings = vulcanReplay.getConfig().getStringList("Vulcan.Disabled-Recordings");
	}

}
