package me.wikmor.hub.listener;

import me.wikmor.hub.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.settings.Lang;

@AutoRegister
public final class PlayerListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		if (!player.hasPermission("hub.event.joinmessage")) {
			event.setJoinMessage("");
			return;
		}

		event.setJoinMessage(Variables.replace(Settings.JoinQuitEvents.JOIN_MESSAGE, player));
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		if (!player.hasPermission("hub.event.quitmessage")) {
			event.setQuitMessage("");
			return;
		}

		event.setQuitMessage(Variables.replace(Settings.JoinQuitEvents.QUIT_MESSAGE, player));
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (Settings.MOB_SPAWN)
			return;

		boolean isSpawnedByPlugins = event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM;
		if (isSpawnedByPlugins)
			return;

		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();

		if (Settings.BLOCK_PLACE || player.hasPermission("hub.event.blockplace"))
			return;

		Common.tellTimed(3, player, Lang.of("Events.Player.Cannot_Place_Blocks"));
		event.setCancelled(true);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		// TODO: disable PvP
	}


	@EventHandler
	public void onFireSpread(BlockIgniteEvent event) {
		if (Settings.FIRE_SPREAD)
			return;

		boolean isCauseSpread = event.getCause() == BlockIgniteEvent.IgniteCause.SPREAD;
		if (!isCauseSpread)
			return;

		event.setCancelled(true);
	}

	@EventHandler
	public void onLeafDecay(LeavesDecayEvent event) {
		if (Settings.LEAVES_DECAY)
			return;

		event.setCancelled(true);
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();

		if (Settings.ITEM_DROP || player.hasPermission("hub.events.itemdrop"))
			return;

		Common.tellTimed(3, player, Lang.of("Events.Player.Cannot_Drop_Items"));
		event.setCancelled(true);
	}

	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();

		if (Settings.ITEM_PICKUP || player.hasPermission("hub.events.itempickup"))
			return;

		Common.tellTimed(3, player, Lang.of("Events.Player.Cannot_Pickup_Items"));
		event.setCancelled(true);
	}

	@EventHandler
	public void onHungerLoss(FoodLevelChangeEvent event) {
		if (Settings.HUNGER_LOSS)
			return;

		if (!(event.getEntity() instanceof Player))
			return;

		event.setCancelled(true);
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		if (Settings.WEATHER_CHANGE)
			return;

		event.setCancelled(true);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (Settings.DEATH_MESSAGES)
			return;

		event.setDeathMessage("");
	}
}
