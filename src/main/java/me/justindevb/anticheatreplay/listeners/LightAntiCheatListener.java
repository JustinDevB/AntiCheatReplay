package me.justindevb.anticheatreplay.listeners;

import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import vekster.lightanticheat.api.LacFlagEvent;
import vekster.lightanticheat.api.LacPunishmentEvent;

public class LightAntiCheatListener extends ListenerBase implements Listener {
    public LightAntiCheatListener(AntiCheatReplay acReplay) {
        super(acReplay);
        Bukkit.getPluginManager().registerEvents(this, acReplay);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFlag(LacFlagEvent event) {
        Player p = event.getPlayer();

        if (alertList.contains(p.getUniqueId()))
            return;

        alertList.add(p.getUniqueId());

        startRecording(p, getReplayName(p, event.getCheckType()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPunishment(LacPunishmentEvent event) {
        final Player p = event.getPlayer();

        if (!punishList.contains(p.getUniqueId()))
            punishList.add(p.getUniqueId());
    }
}
