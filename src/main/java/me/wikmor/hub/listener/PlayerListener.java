package me.wikmor.hub.listener;

import me.wikmor.hub.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.model.Variables;

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
}
