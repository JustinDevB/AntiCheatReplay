package me.justindevb.anticheatreplay.listeners.AntiCheats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import me.justindevb.anticheatreplay.ListenerBase;
import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.vagdedes.spartan.api.PlayerViolationCommandEvent;
import me.vagdedes.spartan.api.PlayerViolationEvent;

public class SpartanListener extends ListenerBase implements Listener {

	public SpartanListener(AntiCheatReplay acReplay) {
		super(acReplay);
		Bukkit.getPluginManager().registerEvents(this, acReplay);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onFlagEvent(PlayerViolationEvent event) {
		Player p = event.getPlayer();

		if (alertList.contains(p.getUniqueId()))
			return;

		alertList.add(p.getUniqueId());

		startRecording(p, getReplayName(p, getHackTypeName(event)));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPunish(PlayerViolationCommandEvent event) {
		final Player p = event.getPlayer();

		if (!punishList.contains(p.getUniqueId()))
			punishList.add(p.getUniqueId());
	}

	/**
	 * Uses reflection to call getHackType() to avoid NoSuchMethodError when
	 * the Spartan API return type changes between versions
	 * (me.vagdedes.spartan.system.Enums$HackType vs ai.idealistic.spartan.abstraction.check.CheckEnums$HackType).
	 */
	private String getHackTypeName(PlayerViolationEvent event) {
		try {
			Object hackType = event.getClass().getMethod("getHackType").invoke(event);
			return hackType != null ? hackType.toString() : "unknown";
		} catch (Exception e) {
			return "unknown";
		}
	}

}
