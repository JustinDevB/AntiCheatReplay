package me.justindevb.anticheatreplay.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.alessiodp.oreannouncer.api.events.bukkit.common.BukkitOreAnnouncerAdvancedAlertEvent;
import com.alessiodp.oreannouncer.api.events.bukkit.common.BukkitOreAnnouncerAlertEvent;
import com.alessiodp.oreannouncer.api.events.bukkit.common.BukkitOreAnnouncerBlockDestroyEvent;

import me.justindevb.anticheatreplay.ListenerBase;
import me.justindevb.anticheatreplay.AntiCheatReplay;

public class OreAnnouncerListener extends ListenerBase implements Listener {

	private final AntiCheatReplay acReplay;

	public OreAnnouncerListener(AntiCheatReplay acReplay) {
		super(acReplay);
		this.acReplay = acReplay;
		acReplay.log("Enabled Listener", true);
	}

	@EventHandler
	public void onPlayerBreak(BukkitOreAnnouncerAlertEvent event) {
		acReplay.log("OreAnnounce", true);
		Player p = Bukkit.getPlayer(event.getPlayer().getPlayerUUID());

		if (alertList.contains(p.getUniqueId()))
			return;
		acReplay.log("Added to alert List", true);
		alertList.add(p.getUniqueId());

		startRecording(p, getReplayName(p, event.getPlayer().getName()));
		
		if (!punishList.contains(p.getUniqueId())) {
			punishList.add(p.getUniqueId());
			acReplay.log("Added to punish list", true);
		}
	}
	
	@EventHandler
	public void onBreak(BukkitOreAnnouncerBlockDestroyEvent event) {
		acReplay.log("OreBreakEvent", true);
	}
	
	@EventHandler
	public void onAdvanced(BukkitOreAnnouncerAdvancedAlertEvent event) {
		acReplay.log("Advanced Event", true);
	}

}
