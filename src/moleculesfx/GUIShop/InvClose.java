package moleculesfx.GUIShop;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InvClose implements Listener {
	private Main pl;
	public InvClose(Main pl){
		this.pl = pl;
	}
	@EventHandler
	public void ItemPurchase(InventoryClickEvent event){
		FileConfiguration conf = this.pl.getConfig();
		String GUITitle = Utils.ColorMsg(conf.getString("GUITitle"));
		int slot = event.getSlot();
		if (!event.getInventory().getName().equalsIgnoreCase(GUITitle))
			return;
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		if (event.getCurrentItem()==null || event.getCurrentItem().getType()==Material.AIR || !event.getCurrentItem().hasItemMeta()){
			player.closeInventory();
			return;
		}
	    Set<String> GetSlots = conf.getConfigurationSection("DispItems").getKeys(false);
		for (String gs : GetSlots){
			int slotnum = conf.getInt("DispItems." + gs + ".Slot")-1;
			if (slot == slotnum){
				player.closeInventory();
			}
		}
	}
}
