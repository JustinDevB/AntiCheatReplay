package me.justindevb.VulcanReplay.Listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.gmail.olexorus.themis.api.ActionEvent;
import com.gmail.olexorus.themis.api.ViolationEvent;

import me.justindevb.VulcanReplay.ListenerBase;
import me.justindevb.VulcanReplay.VulcanReplay;

public class ThemisListener extends ListenerBase implements Listener {
	private final VulcanReplay vulcanReplay;
	private List<String> disabledActions = new ArrayList<>();

	public ThemisListener(VulcanReplay vulcanReplay) {
		super(vulcanReplay);
		Bukkit.getPluginManager().registerEvents(this, VulcanReplay.getInstance());
		this.vulcanReplay = vulcanReplay;
		
		setupThemis();
	}
	
	private void setupThemis() {
		initThemisSpecificConfig();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onViolationEvent(ViolationEvent event) {
		Player p = event.getPlayer();

		if (alertList.contains(p.getName()))
			return;

		alertList.add(p.getName());

		startRecording(p, getReplayName(p, event.getType().getCheckName()));
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAction(ActionEvent event) {
		final Player p = event.getPlayer();
		
		if (disabledActions.contains(event.getActionName().toLowerCase())) {
			vulcanReplay.log("Cancelled Action", true);
			return;
		}
		
		if (!punishList.contains(p.getName()))
			punishList.add(p.getName());
	}
	
	private void initThemisSpecificConfig() {
		this.disabledActions = vulcanReplay.getConfig().getStringList("Themis.Disabled-Actions");
	}

}
