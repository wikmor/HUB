package me.wikmor.hub.listener;

import me.wikmor.hub.settings.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.mineacademy.fo.annotation.AutoRegister;

@AutoRegister
public final class MobListener implements Listener {

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (Settings.MOB_SPAWN)
			return;

		boolean isSpawnedByPlugins = event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM;
		if (isSpawnedByPlugins)
			return;
		
		event.setCancelled(true);
	}
}
