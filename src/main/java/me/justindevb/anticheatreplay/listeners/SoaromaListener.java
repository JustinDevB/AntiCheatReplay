package me.justindevb.anticheatreplay.listeners;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.justindevb.anticheatreplay.ListenerBase;
import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.korbsti.soaromaac.api.PunishmentEvent;
import me.korbsti.soaromaac.api.SoaromaFlagEvent;

public class SoaromaListener extends ListenerBase implements Listener {
	private final AntiCheatReplay acReplay;

	public SoaromaListener(AntiCheatReplay acReplay) {
		super(acReplay);
		Bukkit.getPluginManager().registerEvents(this, AntiCheatReplay.getInstance());
		this.acReplay = acReplay;

		setupSoaroma();
	}

	private void setupSoaroma() {
		checkSoaromaAPI();
	}

	@EventHandler
	public void onFlagEvent(SoaromaFlagEvent event) {

		final Player p = event.getFlaggedPlayer();

		if (alertList.contains(p.getUniqueId()))
			return;

		alertList.add(p.getUniqueId());

		startRecording(p, getReplayName(p, event.getCheckFlagged()));

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPunish(PunishmentEvent event) {
		final Player p = event.getPunishedPlayer();

		if (!punishList.contains(p.getUniqueId()))
			punishList.add(p.getUniqueId());

	}

	/**
	 * Check to see if Soaromoa's API is enabled
	 */
	private void checkSoaromaAPI() {
		acReplay.log("Checking if Soaroma API is enabled", false);
		File file = new File(acReplay.getDataFolder().getParentFile(),
				"SoaromaSAC" + System.getProperty("file.separator") + "main.yml");
		if (!file.exists()) {
			acReplay.log("Soaroma is not installed!", true);
			return;
		}

		FileConfiguration soaroma = YamlConfiguration.loadConfiguration(file);

		if (soaroma.getBoolean("other.enableAPI")) {
			acReplay.log("Soaroma API is enabled", false);
			return;
		}

		acReplay.log("Soaroma API is disabled in Soaroma's config.yml. This must be true for this plugin to work!",
				true);
		acReplay.log(
				"We went ahead and changed it to true, but you need to reboot your server for it to take effect!",
				true);
		soaroma.set("other.enableAPI", true);
		try {
			soaroma.save(file);
		} catch (IOException e) {
			acReplay.log("Error editing Soaroma config. You will have to manually do it", true);
			e.printStackTrace();
		}

		Bukkit.getScheduler().runTask(acReplay, () -> {
			Bukkit.getPluginManager().disablePlugin(acReplay);
		});

	}

}
