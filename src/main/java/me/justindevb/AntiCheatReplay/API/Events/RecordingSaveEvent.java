package me.justindevb.AntiCheatReplay.API.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RecordingSaveEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	private final Player player;
	private final String replayName;
	private boolean isCancelled;

	public RecordingSaveEvent(Player player, String replayName) {
		this.player = player;
		this.replayName = replayName;
		this.isCancelled = false;
	}

	/**
	 * Get the player that had a recording saved of them
	 * 
	 * @return player
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Get the name of the saved recording
	 * 
	 * @return replayName
	 */
	public String getReplayName() {
		return this.replayName;
	}

	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.isCancelled = cancel;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

}
