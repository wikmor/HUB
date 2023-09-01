package me.wikmor.hub.settings;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.tool.Tool;
import org.mineacademy.fo.remain.Remain;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ServerSelectorItem extends Tool {

	@Getter
	private static final ServerSelectorItem instance = new ServerSelectorItem();

	@Override
	protected void onBlockClick(PlayerInteractEvent event) {
		if (!Remain.isInteractEventPrimaryHand(event))
			return;

		ServerSelectorMenuData serverSelectorMenuData = ServerSelectorMenuData.findMenu("Server_Selector");
		serverSelectorMenuData.displayTo(event.getPlayer());
	}

	@Override
	public ItemStack getItem() {
		return ItemCreator.of(
				Settings.ServerSelector.MATERIAL,
				Settings.ServerSelector.NAME,
				Settings.ServerSelector.LORE
		).make();
	}

	@Override
	protected boolean ignoreCancelled() {
		return false;
	}
}
