package me.justindevb.VulcanReplay.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.justindevb.VulcanReplay.PlayerCache;
import me.justindevb.VulcanReplay.VulcanReplay;

public class PlayerListener implements Listener {

	private VulcanReplay vulcanReplay;

	public PlayerListener(VulcanReplay vulcanReplay) {
		this.vulcanReplay = vulcanReplay;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		PlayerCache cachedPlayer = new PlayerCache(p, vulcanReplay);
		vulcanReplay.putCachedPlayer(p.getUniqueId(), cachedPlayer);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(vulcanReplay, () -> {
			vulcanReplay.removeCachedPlayer(p.getUniqueId());
		}, 10L);
	}

}
