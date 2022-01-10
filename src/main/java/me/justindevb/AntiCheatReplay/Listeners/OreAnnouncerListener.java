package me.justindevb.AntiCheatReplay.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.alessiodp.oreannouncer.api.events.bukkit.common.BukkitOreAnnouncerAdvancedAlertEvent;
import com.alessiodp.oreannouncer.api.events.bukkit.common.BukkitOreAnnouncerAlertEvent;
import com.alessiodp.oreannouncer.api.events.bukkit.common.BukkitOreAnnouncerBlockDestroyEvent;

import me.justindevb.AntiCheatReplay.ListenerBase;
import me.justindevb.AntiCheatReplay.AntiCheatReplay;

public class OreAnnouncerListener extends ListenerBase implements Listener {

	private final AntiCheatReplay AntiCheatReplay;

	public OreAnnouncerListener(AntiCheatReplay AntiCheatReplay) {
		super(AntiCheatReplay);
		this.AntiCheatReplay = AntiCheatReplay;
		AntiCheatReplay.log("Enabled Listener", true);
	}

	@EventHandler
	public void onPlayerBreak(BukkitOreAnnouncerAlertEvent event) {
		AntiCheatReplay.log("OreAnnounce", true);
		Player p = Bukkit.getPlayer(event.getPlayer().getPlayerUUID());

		if (alertList.contains(p.getUniqueId()))
			return;
		AntiCheatReplay.log("Added to alert List", true);
		alertList.add(p.getUniqueId());

		startRecording(p, getReplayName(p, event.getPlayer().getName()));
		
		if (!punishList.contains(p.getUniqueId())) {
			punishList.add(p.getUniqueId());
			AntiCheatReplay.log("Added to punish list", true);
		}
	}
	
	@EventHandler
	public void onBreak(BukkitOreAnnouncerBlockDestroyEvent event) {
		AntiCheatReplay.log("OreBreakEvent", true);
	}
	
	@EventHandler
	public void onAdvanced(BukkitOreAnnouncerAdvancedAlertEvent event) {
		AntiCheatReplay.log("Advanced Event", true);
	}

}
