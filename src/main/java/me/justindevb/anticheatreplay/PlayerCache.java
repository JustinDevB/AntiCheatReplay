package me.justindevb.anticheatreplay;

import org.bukkit.entity.Player;

public class PlayerCache {
	private Player player;

	private long loginTimeStamp;
	private boolean isReportCooldown = false;

	public PlayerCache(Player player, AntiCheatReplay acReplay) {
		this.player = player;

		this.loginTimeStamp = System.currentTimeMillis();
	}

	/**
	 * Returns if player is on a cooldown
	 * @return isReportCooldown
	 */
	public boolean isReportCooldown() {
		return isReportCooldown;
	}

	/**
	 * Set whether a player is on a report cooldown.
	 * @param b
	 */
	public void setReportCooldown(boolean b) {
		this.isReportCooldown = b;
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
