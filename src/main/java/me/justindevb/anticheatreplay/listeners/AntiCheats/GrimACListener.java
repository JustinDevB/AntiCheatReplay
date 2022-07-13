package me.justindevb.anticheatreplay.listeners.AntiCheats;

import ac.grim.grimac.events.CommandExecuteEvent;
import ac.grim.grimac.events.FlagEvent;
import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class GrimACListener extends ListenerBase implements Listener {
    private final AntiCheatReplay acReplay;
    private List<String> punishCommands = new ArrayList<>();

    public GrimACListener(AntiCheatReplay acReplay) {
        super(acReplay);
        this.acReplay = acReplay;
        Bukkit.getPluginManager().registerEvents(this, AntiCheatReplay.getInstance());

        setupGrim();
    }

    private void setupGrim() {
        initGrimSpecificConfig();
    }


    @EventHandler
    public void onFlag(FlagEvent event) {
        Player p = Bukkit.getPlayer(event.getPlayer().getUniqueId());

        if (alertList.contains(p.getUniqueId()))
            return;

        alertList.add(p.getUniqueId());

        startRecording(p, getReplayName(p, event.getCheck().getCheckName()));

    }

    @EventHandler
    public void onCommand(CommandExecuteEvent event) {
        if (!parseCommand(event.getCommand()))
            return;

        final Player p = Bukkit.getPlayer(event.getPlayer().getUniqueId());

        if (!punishList.contains(p.getUniqueId()))
            punishList.add(p.getUniqueId());
    }

    /**
     * Check if CommandExecute returns a command that should save a recording
     * @param command
     * @return
     */
    private boolean parseCommand(String command) {
        command = command.toLowerCase();
        String[] parts = command.split(" ");
        for (String commands : punishCommands) {
            if (parts[0].contentEquals(commands.toLowerCase()))
                return true;
        }
        return false;
    }

    private void initGrimSpecificConfig() {
        this.punishCommands = acReplay.getConfig().getStringList("Grim.Punish-Commands");
    }

}

