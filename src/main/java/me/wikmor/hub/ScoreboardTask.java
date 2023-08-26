package me.wikmor.hub;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.wikmor.hub.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.remain.Remain;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ScoreboardTask implements Runnable {

	private static final ScoreboardTask instance = new ScoreboardTask();
	private static final String DUMMY_CRITERIA = "dummy";
	private static final int MAX_LINES_SHOWN_AT_ONCE = 15;

	@Override
	public void run() {
		for (Player player : Remain.getOnlinePlayers()) {
			Objective objective = player.getScoreboard().getObjective(HUB.getInstance().getName());
			if (objective != null)
				updateScoreboard(player, objective);
			else
				createNewScoreboard(player);
		}
	}

	private void createNewScoreboard(Player player) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		render(scoreboard, player);

		for (int slot = 1; slot <= MAX_LINES_SHOWN_AT_ONCE; slot++)
			register(slot, scoreboard);

		player.setScoreboard(scoreboard);
	}

	private void render(Scoreboard scoreboard, Player player) {
		Objective objective = scoreboard.registerNewObjective(HUB.getInstance().getName(), DUMMY_CRITERIA);
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(Variables.replace(Settings.Scoreboard.TITLE, player));
	}

	private void register(int slot, Scoreboard scoreboard) {
		Team team = scoreboard.registerNewTeam("SLOT_" + slot);
		String entry = generateEntry(slot);
		team.addEntry(entry);
	}

	private void updateScoreboard(Player player, Objective objective) {
		int linesCount = Settings.Scoreboard.LINES.size();
		for (String line : Settings.Scoreboard.LINES) {
			setSlot(linesCount, line, player, objective);
			linesCount--;
		}
	}

	private void setSlot(int slot, String text, Player player, Objective objective) {
		Team team = player.getScoreboard().getTeam("SLOT_" + slot);
		String entry = generateEntry(slot);
		if (!player.getScoreboard().getEntries().contains(entry))
			objective.getScore(entry).setScore(slot);

		text = Variables.replace(text, player);
		String prefix = getFirstSplit(text);
		String suffix = getFirstSplit(ChatColor.getLastColors(prefix) + getSecondSplit(text));
		team.setPrefix(prefix);
		team.setSuffix(suffix);
	}

	private String generateEntry(int slot) {
		return ChatColor.values()[slot].toString();
	}

	private String getFirstSplit(String s) {
		return s.length() > 16 ? s.substring(0, 16) : s;
	}

	private String getSecondSplit(String s) {
		if (s.length() > 32)
			s = s.substring(0, 32);

		return s.length() > 16 ? s.substring(16) : "";
	}

	public static ScoreboardTask getInstance() {
		return instance;
	}
}
