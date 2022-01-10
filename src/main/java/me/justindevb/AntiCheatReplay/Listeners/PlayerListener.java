package me.justindevb.AntiCheatReplay.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.justindevb.AntiCheatReplay.PlayerCache;
import me.justindevb.AntiCheatReplay.AntiCheatReplay;

public class PlayerListener implements Listener {

	private AntiCheatReplay AntiCheatReplay;

	public PlayerListener(AntiCheatReplay AntiCheatReplay) {
		this.AntiCheatReplay = AntiCheatReplay;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		PlayerCache cachedPlayer = new PlayerCache(p, AntiCheatReplay);
		AntiCheatReplay.putCachedPlayer(p.getUniqueId(), cachedPlayer);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(AntiCheatReplay, () -> {
			AntiCheatReplay.removeCachedPlayer(p.getUniqueId());
		}, 10L);
	}

}
