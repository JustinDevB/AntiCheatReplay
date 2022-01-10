package me.justindevb.AntiCheatReplay.Listeners;

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
import org.bukkit.event.Listener;

import me.frep.vulcan.api.event.VulcanFlagEvent;
import me.frep.vulcan.api.event.VulcanPunishEvent;

import me.justindevb.AntiCheatReplay.ListenerBase;
import me.justindevb.AntiCheatReplay.AntiCheatReplay;

public class VulcanListener extends ListenerBase implements Listener {

	private final AntiCheatReplay AntiCheatReplay;
	private List<String> disabledRecordings = new ArrayList<>();

	public VulcanListener(AntiCheatReplay AntiCheatReplay) {
		super(AntiCheatReplay);
		this.AntiCheatReplay = AntiCheatReplay;
		Bukkit.getPluginManager().registerEvents(this, AntiCheatReplay.getInstance());

		setupVulcan();

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

		if (alertList.contains(p.getUniqueId()))
			return;

		alertList.add(p.getUniqueId());

		startRecording(p, getReplayName(p, event.getCheck().getName()));

	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPunish(VulcanPunishEvent event) {
		final Player p = event.getPlayer();

		if (!punishList.contains(p.getUniqueId()))
			punishList.add(p.getUniqueId());

	}

	/**
	 * Check to see if the Vulcan API is enabled in Vulcan's config.yml
	 */
	private void checkVulcanApi() {
		AntiCheatReplay.log("Checking if Vulcan API is enabled", false);
		File file = new File(AntiCheatReplay.getDataFolder().getParentFile(),
				"Vulcan" + System.getProperty("file.separator") + "config.yml");
		if (!file.exists()) {
			AntiCheatReplay.log("Vulcan is not installed!", true);
			return;
		}

		FileConfiguration vulcan = YamlConfiguration.loadConfiguration(file);

		if (vulcan.getBoolean("settings.enable-api")) {
			AntiCheatReplay.log("Vulcan API is enabled", false);
			return;
		}

		AntiCheatReplay.log("Vulcan API is disabled in Vulcan's config.yml. This must be true for this plugin to work!",
				true);
		AntiCheatReplay.log(
				"We went ahead and changed it to true, but you need to reboot your server for it to take effect!",
				true);
		vulcan.set("settings.enable-api", true);
		try {
			vulcan.save(file);
		} catch (IOException e) {
			AntiCheatReplay.log("Error editing Vulcan config. You will have to manually do it", true);
			e.printStackTrace();
		}

		Bukkit.getScheduler().runTask(AntiCheatReplay, () -> {
			Bukkit.getPluginManager().disablePlugin(AntiCheatReplay);
		});
	}

	public void initVulcanSpecificConfig() {
		this.disabledRecordings = AntiCheatReplay.getConfig().getStringList("Vulcan.Disabled-Recordings");
	}

}
