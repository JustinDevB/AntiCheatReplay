package me.justindevb.AntiCheatReplay.Listeners;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.justindevb.AntiCheatReplay.ListenerBase;
import me.justindevb.AntiCheatReplay.AntiCheatReplay;
import me.korbsti.soaromaac.api.PunishmentEvent;
import me.korbsti.soaromaac.api.SoaromaFlagEvent;

public class SoaromaListener extends ListenerBase implements Listener {
	private final AntiCheatReplay AntiCheatReplay;

	public SoaromaListener(AntiCheatReplay AntiCheatReplay) {
		super(AntiCheatReplay);
		Bukkit.getPluginManager().registerEvents(this, AntiCheatReplay.getInstance());
		this.AntiCheatReplay = AntiCheatReplay;

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

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPunish(PunishmentEvent event) {
		final Player p = event.getPunishedPlayer();

		if (!punishList.contains(p.getUniqueId()))
			punishList.add(p.getUniqueId());

	}

	/**
	 * Check to see if Soaromoa's API is enabled
	 */
	private void checkSoaromaAPI() {
		AntiCheatReplay.log("Checking if Soaroma API is enabled", false);
		File file = new File(AntiCheatReplay.getDataFolder().getParentFile(),
				"SoaromaSAC" + System.getProperty("file.separator") + "main.yml");
		if (!file.exists()) {
			AntiCheatReplay.log("Soaroma is not installed!", true);
			return;
		}

		FileConfiguration soaroma = YamlConfiguration.loadConfiguration(file);

		if (soaroma.getBoolean("other.enableAPI")) {
			AntiCheatReplay.log("Soaroma API is enabled", false);
			return;
		}

		AntiCheatReplay.log("Soaroma API is disabled in Soaroma's config.yml. This must be true for this plugin to work!",
				true);
		AntiCheatReplay.log(
				"We went ahead and changed it to true, but you need to reboot your server for it to take effect!",
				true);
		soaroma.set("other.enableAPI", true);
		try {
			soaroma.save(file);
		} catch (IOException e) {
			AntiCheatReplay.log("Error editing Soaroma config. You will have to manually do it", true);
			e.printStackTrace();
		}

		Bukkit.getScheduler().runTask(AntiCheatReplay, () -> {
			Bukkit.getPluginManager().disablePlugin(AntiCheatReplay);
		});

	}

}
