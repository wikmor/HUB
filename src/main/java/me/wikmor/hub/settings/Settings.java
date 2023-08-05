package me.wikmor.hub.settings;

import org.mineacademy.fo.settings.SimpleSettings;

@SuppressWarnings("unused")
public final class Settings extends SimpleSettings {

	@Override
	protected int getConfigVersion() {
		return 3;
	}

	public static Boolean BLOCK_BREAK;
	public static Boolean BLOCK_PLACE;
	public static Boolean ITEM_DROP;
	public static Boolean ITEM_PICKUP;
	public static Boolean HUNGER_LOSS;
	public static Boolean WEATHER_CHANGE;
	public static Boolean DEATH_MESSAGE;

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

		BLOCK_BREAK = getBoolean("Block_Break");
		BLOCK_PLACE = getBoolean("Block_Place");
		ITEM_DROP = getBoolean("Item_Drop");
		ITEM_PICKUP = getBoolean("Item_Pickup");
		HUNGER_LOSS = getBoolean("Hunger_Loss");
		WEATHER_CHANGE = getBoolean("Weather_Change");
		DEATH_MESSAGE = getBoolean("Death_Message");
	}
}
