package me.wikmor.hub;

import me.wikmor.hub.settings.Settings;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.model.Variable;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.Remain;

import java.util.function.Function;

public final class HUB extends SimplePlugin {

	private BukkitTask scoreboardTask;

	@Override
	protected void onPluginStart() {
		scoreboardTask = getServer()
				.getScheduler()
				.runTaskTimer(this, ScoreboardTask.getInstance(), 0, Settings.Scoreboard.REFRESH_RATE_TICKS);

		for (World world : getServer().getWorlds())
			lockTimeIn(world);

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

		Variables.addVariable("online", new Function<CommandSender, String>() {
			@Override
			public String apply(CommandSender commandSender) {
				return String.valueOf(Remain.getOnlinePlayers().size());
			}
		});
	}

	@Override
	protected void onReloadablesStart() {
		Variable.loadVariables();
	}

	@Override
	protected void onPluginStop() {
		if (scoreboardTask != null && !scoreboardTask.isCancelled())
			scoreboardTask.cancel();
	}

	private void lockTimeIn(World world) {
		world.setGameRuleValue("doDaylightCycle", "false");
		world.setTime(5000);
	}

	@Override
	public int getMetricsPluginId() {
		return 19339;
	}

	public static HUB getInstance() {
		return (HUB) SimplePlugin.getInstance();
	}
}
