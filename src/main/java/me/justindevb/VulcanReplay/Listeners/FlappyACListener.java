package me.justindevb.VulcanReplay.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.justdoom.flappyanticheat.customevents.FlagEvent;
import com.justdoom.flappyanticheat.customevents.PunishEvent;

import me.justindevb.VulcanReplay.ListenerBase;
import me.justindevb.VulcanReplay.VulcanReplay;

public class FlappyACListener extends ListenerBase implements Listener {

	public FlappyACListener(VulcanReplay vulcanReplay) {
		super(vulcanReplay);
		Bukkit.getPluginManager().registerEvents(this, VulcanReplay.getInstance());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onFlagEvent(FlagEvent event) {
		Player p = event.getFlaggedPlayer();

		if (alertList.contains(p.getUniqueId()))
			return;

		alertList.add(p.getUniqueId());

		startRecording(p, getReplayName(p, event.getCheck().toString()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPunish(PunishEvent event) {
		final Player p = event.getPunishedPlayer();

		if (!punishList.contains(p.getUniqueId()))
			punishList.add(p.getUniqueId());
	}
	
	

}
