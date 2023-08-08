package me.wikmor.hub.settings;

import org.mineacademy.fo.settings.SimpleSettings;

@SuppressWarnings("unused")
public final class Settings extends SimpleSettings {

	@Override
	protected int getConfigVersion() {
		return 4;
	}

	public static Boolean BLOCK_BURN;
	public static Boolean BLOCK_BREAK;
	public static Boolean BLOCK_INTERACT;
	public static Boolean BLOCK_PLACE;
	public static Boolean DEATH_MESSAGES;
	public static Boolean FIRE_SPREAD;
	public static Boolean HUNGER_LOSS;
	public static Boolean ITEM_DROP;
	public static Boolean ITEM_PICKUP;
	public static Boolean LEAVES_DECAY;
	public static Boolean WEATHER_CHANGE;

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

		BLOCK_BURN = getBoolean("Block_Burn");
		BLOCK_BREAK = getBoolean("Block_Break");
		BLOCK_INTERACT = getBoolean("Block_Interact");
		BLOCK_PLACE = getBoolean("Block_Place");
		DEATH_MESSAGES = getBoolean("Death_Messages");
		FIRE_SPREAD = getBoolean("Fire_Spread");
		HUNGER_LOSS = getBoolean("Hunger_Loss");
		ITEM_DROP = getBoolean("Item_Drop");
		ITEM_PICKUP = getBoolean("Item_Pickup");
		LEAVES_DECAY = getBoolean("Leaves_Decay");
		WEATHER_CHANGE = getBoolean("Weather_Change");

	}
}
