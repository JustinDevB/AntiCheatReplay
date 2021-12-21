package me.justindevb.VulcanReplay;

import org.bukkit.entity.Player;

public class PlayerCache {
	private Player player;

	private long loginTimeStamp;

	public PlayerCache(Player player, VulcanReplay vulcanReplay) {
		this.player = player;

		this.loginTimeStamp = System.currentTimeMillis();
	}

	/**
	 * Get the player that is associated with this PlayerCache
	 * 
	 * @return
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Get the timestamp of when a player first logged in.
	 * 
	 * @return
	 */
	public long getLoginTimeStamp() {
		return this.loginTimeStamp;
	}

}
