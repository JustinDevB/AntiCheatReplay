package me.justindevb.anticheatreplay.listeners.AntiCheats;

import ac.sparky.api.events.SparkyPunishEvent;
import ac.sparky.api.events.SparkyViolationEvent;
import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class SparkyListener extends ListenerBase implements Listener {

    public SparkyListener(AntiCheatReplay acReplay) {
        super(acReplay);
        Bukkit.getPluginManager().registerEvents(this, acReplay);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void violationEvent(SparkyViolationEvent event) {
        final Player p = event.getPlayer();

        if (alertList.contains(p.getUniqueId()))
            return;

        alertList.add(p.getUniqueId());

        startRecording(p, getReplayName(p, event.getCheckType()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPunish(SparkyPunishEvent event) {
        final Player p = event.getPlayer();

        if (!punishList.contains(p.getUniqueId()))
            punishList.add(p.getUniqueId());
    }
}
