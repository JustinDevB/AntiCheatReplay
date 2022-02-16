package me.justindevb.anticheatreplay.listeners;

import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;
import me.tecnio.antihaxerman.api.impl.AHMFlagEvent;
import me.tecnio.antihaxerman.api.impl.AHMPunishEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AntiHaxermanListener extends ListenerBase implements Listener {
    public AntiHaxermanListener(AntiCheatReplay acReplay) {
        super(acReplay);
        Bukkit.getPluginManager().registerEvents(this, AntiCheatReplay.getInstance());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFlag(AHMFlagEvent event) {
        Player p = event.getPlayer();

        if (alertList.contains(p.getUniqueId()))
            return;

        alertList.add(p.getUniqueId());

        startRecording(p, getReplayName(p, event.getCheck()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPunish(AHMPunishEvent event) {
        Player p = event.getPlayer();

        if (!punishList.contains(p.getUniqueId()))
            punishList.add(p.getUniqueId());
    }
}
