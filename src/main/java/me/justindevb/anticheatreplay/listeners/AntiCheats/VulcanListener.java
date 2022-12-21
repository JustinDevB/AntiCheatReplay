package me.justindevb.anticheatreplay.listeners.AntiCheats;

import me.frep.vulcan.api.event.VulcanFlagEvent;
import me.frep.vulcan.api.event.VulcanPunishEvent;
import me.justindevb.anticheatreplay.AntiCheatReplay;
import me.justindevb.anticheatreplay.ListenerBase;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class VulcanListener extends ListenerBase implements Listener {

    private List<String> disabledRecordings = new ArrayList<>();

    public VulcanListener(AntiCheatReplay acReplay) {
        super(acReplay);
        Bukkit.getPluginManager().registerEvents(this, acReplay);

        setupVulcan();

    }

    private void setupVulcan() {
        checkVulcanApi();
        initVulcanSpecificConfig();

    }

    @EventHandler
    public void onFlagEvent(VulcanFlagEvent event) {
        String fullName = event.getCheck().getName() + event.getCheck().getType();
        if (disabledRecordings.contains(event.getCheck().getName().toLowerCase())
                || disabledRecordings.contains(fullName.toLowerCase()))
            return;

        final Player p = event.getPlayer();

        if (alertList.contains(p.getUniqueId()))
            return;

        alertList.add(p.getUniqueId());

        startRecording(p, getReplayName(p, event.getCheck().getName()));

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPunish(VulcanPunishEvent event) {
        final Player p = event.getPlayer();

        if (!punishList.contains(p.getUniqueId()))
            punishList.add(p.getUniqueId());

    }

    /**
     * Check to see if the Vulcan API is enabled in Vulcan's config.yml
     */
    private void checkVulcanApi() {
        Bukkit.getScheduler().runTaskAsynchronously(acReplay, () -> {
            acReplay.log("Checking if Vulcan API is enabled", false);
            File file = new File(acReplay.getDataFolder().getParentFile(),
                    "Vulcan" + System.getProperty("file.separator") + "config.yml");
            if (!file.exists()) {
                acReplay.log("Vulcan is not installed!", true);
                return;
            }

            FileConfiguration vulcan = YamlConfiguration.loadConfiguration(file);

            if (vulcan.getBoolean("settings.enable-api")) {
                acReplay.log("Vulcan API is enabled", false);
                return;
            }

            acReplay.log("Vulcan API is disabled in Vulcan's config.yml. This must be true for this plugin to work!",
                    true);
            acReplay.log(
                    "We went ahead and changed it to true, but you need to reboot your server for it to take effect!",
                    true);
            vulcan.set("settings.enable-api", true);
            try {
                vulcan.save(file);
            } catch (IOException e) {
                acReplay.log("Error editing Vulcan config. You will have to manually do it", true);
                e.printStackTrace();
            }

            Bukkit.getScheduler().runTask(acReplay, () -> {
                Bukkit.getPluginManager().disablePlugin(acReplay);
            });
        });
    }

    public void initVulcanSpecificConfig() {
        this.disabledRecordings = acReplay.getConfig().getStringList("Vulcan.Disabled-Recordings");

        ListIterator<String> iterator = disabledRecordings.listIterator();
        while (iterator.hasNext())
            iterator.set(iterator.next().toLowerCase());

    }

}
