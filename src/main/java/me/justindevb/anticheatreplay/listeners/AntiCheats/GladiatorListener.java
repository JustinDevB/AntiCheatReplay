package me.justindevb.anticheatreplay.listeners.AntiCheats;

import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;

import me.justindevb.gladiator.events.PunishmentEvent;
import me.justindevb.gladiator.events.ViolationEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class GladiatorListener extends ListenerBase implements Listener {

    private List<String> disabledRecordings = new ArrayList<>();
    private boolean recordExperimental;

    public GladiatorListener(AntiCheatReplay acReplay) {
        super(acReplay);
        Bukkit.getPluginManager().registerEvents(this, acReplay);

        initGladiatorSpecificConfig();
    }

    @EventHandler
    public void onFlag(ViolationEvent event) {
        String fullName = event.getCheckName() + event.getCheckType();
        if (disabledRecordings.contains(event.getCheckName().toLowerCase())
                || disabledRecordings.contains(fullName.toLowerCase()))
            return;

        if (event.isExperimental() && !recordExperimental)
            return;


        final Player p = event.getPlayer();

        if (alertList.contains(p.getUniqueId()))
            return;

        alertList.add(p.getUniqueId());

        startRecording(p, getReplayName(p, event.getCheckName()));
    }

    @EventHandler
    public void onPunish(PunishmentEvent event) {
        final Player p = event.getPlayer();

        if (!punishList.contains(p.getUniqueId()))
            punishList.add(p.getUniqueId());
    }

    public void initGladiatorSpecificConfig() {
        this.disabledRecordings = acReplay.getConfig().getStringList("Gladiator.Disabled-Recordings");
        ListIterator<String> iterator = disabledRecordings.listIterator();
        while (iterator.hasNext())
            iterator.set(iterator.next().toLowerCase());

        this.recordExperimental = acReplay.getConfig().getBoolean("Gladiator.Record-Experimental-Checks");

    }
}
