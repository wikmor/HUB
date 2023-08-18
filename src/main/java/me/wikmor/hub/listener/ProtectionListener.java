package me.wikmor.hub.listener;

import me.wikmor.hub.HUB;
import me.wikmor.hub.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
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
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.Lang;

import java.util.Arrays;
import java.util.List;

@AutoRegister
public final class ProtectionListener implements Listener {

	private List<CompMaterial> interactables = Arrays.asList(
			CompMaterial.ACACIA_FENCE_GATE,
			CompMaterial.ACACIA_TRAPDOOR,
			CompMaterial.BIRCH_FENCE_GATE,
			CompMaterial.BIRCH_TRAPDOOR,
			CompMaterial.CHEST,
			CompMaterial.DARK_OAK_FENCE_GATE,
			CompMaterial.DARK_OAK_TRAPDOOR,
			CompMaterial.ENDER_CHEST,
			CompMaterial.FURNACE,
			CompMaterial.JUNGLE_FENCE_GATE,
			CompMaterial.JUNGLE_TRAPDOOR,
			CompMaterial.OAK_FENCE_GATE,
			CompMaterial.OAK_TRAPDOOR,
			CompMaterial.LEVER,
			CompMaterial.SPRUCE_FENCE_GATE,
			CompMaterial.SPRUCE_TRAPDOOR
	);

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

	@EventHandler // TODO: To be continued
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (Settings.BLOCK_INTERACT || player.hasPermission("hub.event.interact"))
			return;

		Block clickedBlock = event.getClickedBlock();
		if (clickedBlock == null)
			return;

		Action action = event.getAction();
		boolean isRightClickBlock = action != Action.LEFT_CLICK_BLOCK &&
				action != Action.LEFT_CLICK_AIR &&
				action != Action.RIGHT_CLICK_AIR;
		if (!isRightClickBlock)
			return;

		preventFirstInteraction(player, clickedBlock, event);
	}

	private void preventFirstInteraction(Player player, Block clickedBlock, PlayerInteractEvent event) {
		for (CompMaterial interactable : interactables) {
//			System.out.println("Block = " + clickedBlock);
//			System.out.println(interactable.getMaterial());
			if (interactable.getMaterial() == clickedBlock.getType()) {
				Common.tellTimed(3, player, Lang.of("Events.Player.Cannot_Interact"));
				event.setCancelled(true);
				break;
			}
		}
	}

	@EventHandler
	public void onItemFrameRotate(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();

		if (Settings.BLOCK_INTERACT || player.hasPermission("hub.event.interact"))
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
			if (player.hasPermission("hub.event.interact"))
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
