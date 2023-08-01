package me.wikmor.hub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.model.Variable;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.util.function.Function;

public final class HUB extends SimplePlugin {

	@Override
	protected void onPluginStart() {
		Variables.addVariable("player", new Function<CommandSender, String>() {
			@Override
			public String apply(CommandSender commandSender) {
				if (commandSender instanceof Player)
					return ((Player) commandSender).getDisplayName();

				return "";
			}
		});

		Variables.addVariable("username", new Function<CommandSender, String>() {
			@Override
			public String apply(CommandSender commandSender) {
				if (commandSender instanceof Player)
					return commandSender.getName();

				return "";
			}
		});

		Variables.addVariable("prefix", new Function<CommandSender, String>() {
			@Override
			public String apply(CommandSender commandSender) {
				if (commandSender instanceof Player)
					return HookManager.getPlayerPrefix((Player) commandSender);

				return "";
			}
		});

		Variables.addVariable("suffix", new Function<CommandSender, String>() {
			@Override
			public String apply(CommandSender commandSender) {
				if (commandSender instanceof Player)
					return HookManager.getPlayerSuffix((Player) commandSender);

				return "";
			}
		});
	}

	@Override
	protected void onReloadablesStart() {
		Variable.loadVariables();
	}

	@Override
	public int getMetricsPluginId() {
		return 19339;
	}

	public static HUB getInstance() {
		return (HUB) SimplePlugin.getInstance();
	}
}
