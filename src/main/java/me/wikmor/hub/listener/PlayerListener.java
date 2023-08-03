package me.wikmor.hub.listener;

import me.wikmor.hub.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();

		if (Settings.CAN_PLAYER_BREAK_BLOCKS || player.hasPermission("hub.event.blockbreak"))
			return;

		Common.tell(player, Lang.of("Events.Cannot_Break_Blocks"));
		event.setCancelled(true);
	}
}