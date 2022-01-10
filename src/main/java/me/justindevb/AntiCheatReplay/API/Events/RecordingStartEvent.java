package me.justindevb.AntiCheatReplay.API.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RecordingStartEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();

	private final Player recordedPlayer;
	private final String replayName;
	private boolean isCancelled;

	/**
	 * 
	 * @param player
	 * @param players
	 * @param replayName
	 */
	public RecordingStartEvent(Player recordedPlayer, String replayName) {
		this.replayName = replayName;
		this.isCancelled = false;
		this.recordedPlayer = recordedPlayer;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	/**
	 * Get the name of the Replay that is being recorded
	 * 
	 * @return replayName
	 */
	public String getReplayName() {
		return this.replayName;
	}

	/**
	 * Get the player that is the main character of our recording, the one that
	 * flagged the AntiCheat
	 * 
	 * @return recordedPlayer
	 */
	public Player getRecordedPlayer() {
		return this.recordedPlayer;
	}

	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

}
