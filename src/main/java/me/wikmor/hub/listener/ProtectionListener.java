package me.wikmor.hub.listener;

import me.wikmor.hub.HUB;
import me.wikmor.hub.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.settings.Lang;

@AutoRegister
public final class ProtectionListener implements Listener {

	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) {
		if (Settings.BLOCK_BURN)
			return;

		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();

		if (Settings.BLOCK_BREAK || player.hasPermission("hub.event.blockbreak"))
			return;

		Common.tellTimed(3, player, Lang.of("Events.Player.Cannot_Break_Blocks"));
		event.setCancelled(true);
	}

	@EventHandler
	public void onHangingEntityBreak(HangingBreakByEntityEvent event) {
		if (Settings.BLOCK_BREAK)
			return;

		Entity remover = event.getRemover();
		if (!(remover instanceof Player))
			return;

		Player player = (Player) remover;
		Entity entity = event.getEntity();
		if (!(entity instanceof ItemFrame || entity instanceof Painting))
			return;

		Common.tellTimed(3, player, Lang.of("Events.Player.Cannot_Break_Blocks"));
		event.setCancelled(true);
	}

	@EventHandler
	public void onBlockInteract(PlayerInteractEvent event) {
		// TODO: disable player interaction with doors, levers, etc.
	}

	@EventHandler
	public void onItemFrameRotate(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();

		if (Settings.BLOCK_INTERACT || player.hasPermission("hub.event.blockinteract"))
			return;

		Entity clickedEntity = event.getRightClicked();
		if (!(clickedEntity instanceof ItemFrame))
			return;

		Common.tellTimed(3, player, Lang.of("Events.Player.Cannot_Interact"));
		event.setCancelled(true);
	}

	@EventHandler
	public void onItemStealFromItemFrame(EntityDamageByEntityEvent event) {
		if (Settings.BLOCK_INTERACT)
			return;

		Entity entity = event.getEntity();
		Entity damager = event.getDamager();

		if (entity instanceof ItemFrame && damager instanceof Player) {
			Player player = (Player) damager;
			if (player.hasPermission("hub.event.blockinteract"))
				return;

			Common.tellTimed(3, player, Lang.of("Events.Player.Cannot_Interact"));
			event.setCancelled(true);
		}
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
	public void onPlayerDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;

		Player player = (Player) event.getEntity();
		EntityDamageEvent.DamageCause damageCause = event.getCause();

		switch (damageCause) {
			case DROWNING:
				if (Settings.DROWNING_DAMAGE || player.hasPermission("hub.event.drowningdamage"))
					return;

				event.setCancelled(true);
				break;

			case FALL:
				if (Settings.FALL_DAMAGE || player.hasPermission("hub.event.falldamage"))
					return;

				event.setCancelled(true);
				break;

			case FIRE:
			case FIRE_TICK:
			case LAVA:
				if (Settings.FIRE_DAMAGE || player.hasPermission("hub.event.firedamage"))
					return;

				event.setCancelled(true);
				break;

			case VOID:
				player.setFallDistance(0F);
				Location spawnLocation = player.getWorld().getSpawnLocation();
				Bukkit.getScheduler().scheduleSyncDelayedTask(HUB.getInstance(), new Runnable() {
					@Override
					public void run() {
						player.teleport(spawnLocation);
					}
				}, 3L);
				event.setCancelled(true);
				break;
		}
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
}
