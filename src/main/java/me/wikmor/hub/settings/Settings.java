package me.wikmor.hub.settings;

import org.mineacademy.fo.settings.SimpleSettings;

@SuppressWarnings("unused")
public final class Settings extends SimpleSettings {

	@Override
	protected int getConfigVersion() {
		return 2;
	}

	public static Boolean CAN_PLAYER_BREAK_BLOCKS;

	public static class JoinQuitEvents {

		public static String JOIN_MESSAGE;
		public static String QUIT_MESSAGE;

		private static void init() {
			setPathPrefix("Join_Quit_Events");

			JOIN_MESSAGE = getString("Join_Message");
			QUIT_MESSAGE = getString("Quit_Message");
		}
	}

	private static void init() {
		setPathPrefix(null);

		CAN_PLAYER_BREAK_BLOCKS = getBoolean("Can_Player_Break_Blocks");
	}
}