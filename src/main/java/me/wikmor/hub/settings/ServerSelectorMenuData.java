package me.wikmor.hub.settings;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.MathUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.ConfigSerializable;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.ConfigItems;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.*;

@Getter
public class ServerSelectorMenuData extends YamlConfig {

	private static final ConfigItems<ServerSelectorMenuData> serverSelectorMenuData = ConfigItems
			.fromFile("", "server-selector.yml", ServerSelectorMenuData.class);

	private final String name;

	private String title;

	private List<String> info;
	private int size;
	private List<ButtonData> buttons;

	private ServerSelectorMenuData(String name) {
		this.name = name;

		this.setPathPrefix(name);
		this.loadConfiguration(NO_DEFAULT, "server-selector.yml");
	}

	@Override
	protected void onLoad() {
		this.title = this.getString("Title");
		this.info = this.getStringList("Info");
		this.size = (int) MathUtil.calculate(this.getString("Size"));
		this.buttons = this.loadButtons();
	}

	private List<ButtonData> loadButtons() {
		List<ButtonData> buttons = new ArrayList<>();

		for (Map.Entry<String, Object> entry : this.getMap("Buttons", String.class, Object.class).entrySet()) {
			String buttonName = entry.getKey();
			SerializedMap buttonSettings = SerializedMap.of(entry.getValue());

			buttons.add(ButtonData.deserialize(buttonSettings, buttonName));
		}

		return buttons;
	}

	public void displayTo(Player player) {
		this.toMenu().displayTo(player);
	}

	public Menu toMenu() {
		return this.toMenu(null);
	}

	public Menu toMenu(Menu parent) {

		Map<Integer, Button> buttons = this.getButtons();

		return new Menu() {
			{ // constructor for a anonymous class
				this.setTitle(ServerSelectorMenuData.this.title);
				this.setSize(ServerSelectorMenuData.this.size);
			}

			@Override
			protected List<Button> getButtonsToAutoRegister() {
				return new ArrayList<>(buttons.values());
			}

			@Override
			public ItemStack getItemAt(int slot) {

				if (buttons.containsKey(slot))
					return buttons.get(slot).getItem();

				return NO_ITEM;
			}

			@Override
			protected String[] getInfo() {
				return Valid.isNullOrEmpty(info) ? null : Common.toArray(info);
			}
		};
	}

	public Map<Integer, Button> getButtons() {
		Map<Integer, Button> buttons = new HashMap<>();

		for (ButtonData data : this.buttons) {
			buttons.put(data.getSlot(), new Button() {
				@Override
				public void onClickedInMenu(Player player, Menu menu, ClickType click) {
					if (data.getPlayerCommand() != null)
						player.chat(data.getPlayerCommand());
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(data.getMaterial(), data.getTitle(), data.getLore()).make();
				}
			});
		}

		return buttons;
	}

	@Getter
	@ToString
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	private static class ButtonData implements ConfigSerializable {

		private final String name;

		private int slot;
		private CompMaterial material;
		private String title;
		private List<String> lore;

		private String playerCommand;

		@Override
		public SerializedMap serialize() {
			return null;
		}

		public static ButtonData deserialize(SerializedMap map, String buttonName) {
			ButtonData button = new ButtonData(buttonName);

			button.slot = map.containsKey("Slot") ? Integer.parseInt(map.getString("Slot")) : -1;
			Valid.checkBoolean(button.slot != -1, "Missing 'Slot' key from button: " + map);

			button.material = map.getMaterial("Material");
			Valid.checkNotNull(button.material, "Missing 'Material' key from button: " + map);

			button.title = map.getString("Title");
			Valid.checkNotNull(button.title, "Missing 'Title' key from button: " + map);

			button.lore = map.getStringList("Lore");
			Valid.checkNotNull(button.lore, "Missing 'Lore' key from button: " + map);

			SerializedMap click = map.getMap("Click");

			button.playerCommand = click.getString("Player_Command");

			return button;
		}
	}

	public static ServerSelectorMenuData findMenu(String name) {
		return serverSelectorMenuData.findItem(name);
	}

	public static void loadMenus() {
		serverSelectorMenuData.loadItems();
	}

	public static Set<String> getMenuNames() {
		return serverSelectorMenuData.getItemNames();
	}
}
