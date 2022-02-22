package me.justindevb.anticheatreplay.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerReportEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;

    private Player reporter;
    private Player target;
    private String reason;

    public PlayerReportEvent(Player reporter, Player target, String reason) {
        this.reporter = reporter;
        this.target = target;
        this.reason = reason;
        this.isCancelled = false;
    }

    /**
     * Player that submitted a report
     *
     * @return reporter
     */
    public Player getReporter() {
        return this.reporter;
    }

    /**
     * Player that was reported
     *
     * @return target
     */
    public Player getTarget() {
        return this.target;
    }

    /**
     * Reason for the report.
     * Returns "No Reason Supplied" if no reason was given in the report.
     *
     * @return reason
     */
    public String getReason() {
        return this.reason;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


}
