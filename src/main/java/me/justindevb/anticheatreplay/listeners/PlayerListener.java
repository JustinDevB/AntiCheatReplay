package me.justindevb.anticheatreplay.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.justindevb.anticheatreplay.PlayerCache;
import me.justindevb.anticheatreplay.AntiCheatReplay;

public class PlayerListener implements Listener {

	private AntiCheatReplay acReplay;

	public PlayerListener(AntiCheatReplay acReplay) {
		this.acReplay = acReplay;
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

}
