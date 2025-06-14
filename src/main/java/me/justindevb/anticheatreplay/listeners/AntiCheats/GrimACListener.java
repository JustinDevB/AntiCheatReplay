package me.justindevb.anticheatreplay.listeners.AntiCheats;


import ac.grim.grimac.api.GrimAbstractAPI;
import ac.grim.grimac.api.event.GrimEvent;
import ac.grim.grimac.api.event.GrimEventListener;
import ac.grim.grimac.api.event.events.CommandExecuteEvent;
import ac.grim.grimac.api.event.events.FlagEvent;
import ac.grim.grimac.api.plugin.GrimPlugin;
import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class GrimACListener extends ListenerBase implements GrimEventListener {
	
    private List<String> punishCommands = new ArrayList<>();

    public GrimACListener(AntiCheatReplay acReplay) {
        super(acReplay);


        setupGrim();
    }

    private void setupGrim() {

        RegisteredServiceProvider<GrimAbstractAPI> provider = Bukkit.getServicesManager().getRegistration(GrimAbstractAPI.class);
        GrimAbstractAPI api = provider.getProvider();

        initGrimSpecificConfig();
    }


    public void onFlag(FlagEvent event) {
        Player p = Bukkit.getPlayer(event.getPlayer().getUniqueId());

        if (alertList.contains(p.getUniqueId()))
            return;

        alertList.add(p.getUniqueId());

        startRecording(p, getReplayName(p, event.getCheck().getCheckName()));

    }

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

    @Override
    public void handle(GrimEvent grimEvent) throws Exception {

    }
}

