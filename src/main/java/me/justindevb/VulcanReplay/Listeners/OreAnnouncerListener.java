package me.justindevb.VulcanReplay.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.alessiodp.oreannouncer.api.events.bukkit.common.BukkitOreAnnouncerAdvancedAlertEvent;
import com.alessiodp.oreannouncer.api.events.bukkit.common.BukkitOreAnnouncerAlertEvent;
import com.alessiodp.oreannouncer.api.events.bukkit.common.BukkitOreAnnouncerBlockDestroyEvent;

import me.justindevb.VulcanReplay.ListenerBase;
import me.justindevb.VulcanReplay.VulcanReplay;

public class OreAnnouncerListener extends ListenerBase implements Listener {

	private final VulcanReplay vulcanReplay;

	public OreAnnouncerListener(VulcanReplay vulcanReplay) {
		super(vulcanReplay);
		this.vulcanReplay = vulcanReplay;
		vulcanReplay.log("Enabled Listener", true);
	}

	@EventHandler
	public void onPlayerBreak(BukkitOreAnnouncerAlertEvent event) {
		vulcanReplay.log("OreAnnounce", true);
		Player p = Bukkit.getPlayer(event.getPlayer().getPlayerUUID());

		if (alertList.contains(p.getUniqueId()))
			return;
		vulcanReplay.log("Added to alert List", true);
		alertList.add(p.getUniqueId());

		startRecording(p, getReplayName(p, event.getPlayer().getName()));
		
		if (!punishList.contains(p.getUniqueId())) {
			punishList.add(p.getUniqueId());
			vulcanReplay.log("Added to punish list", true);
		}
	}
	
	@EventHandler
	public void onBreak(BukkitOreAnnouncerBlockDestroyEvent event) {
		vulcanReplay.log("OreBreakEvent", true);
	}
	
	@EventHandler
	public void onAdvanced(BukkitOreAnnouncerAdvancedAlertEvent event) {
		vulcanReplay.log("Advanced Event", true);
	}

}
