package me.justindevb.anticheatreplay.listeners.AntiCheats;

import cc.astroac.api.event.impl.AstroFlagEvent;
import cc.astroac.api.event.impl.AstroPunishEvent;
import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AstroACListener extends ListenerBase implements Listener {

    public AstroACListener(AntiCheatReplay acReplay) {
        super(acReplay);
        Bukkit.getPluginManager().registerEvents(this, acReplay);

    }

    @EventHandler
    public void onFlag(AstroFlagEvent event) {
        final Player p = event.getPlayer();

        if (alertList.contains(p.getUniqueId()))
            return;

        alertList.add(p.getUniqueId());

        startRecording(p, getReplayName(p, event.getCheck()));
    }

    @EventHandler
    public void onPunish(AstroPunishEvent event) {
        final Player p = event.getPlayer();

        if (!punishList.contains(p.getUniqueId()))
            punishList.add(p.getUniqueId());
    }


}
