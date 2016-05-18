package moleculesfx.GUIShop;

import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.milkbowl.vault.economy.Economy;

public class InvSellItems implements Listener {

	
	public static Economy econ = null;
	private Main pl;
	
	public InvSellItems(Economy money, Main pl){
		this.pl = pl;
		econ = money;
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void ItemPurchase(InventoryClickEvent event){
		FileConfiguration conf = this.pl.getConfig();
		String GUITitle = Utils.ColorMsg(conf.getString("GUITitle"));
		String Prefix = Utils.ColorMsg(conf.getString("Prefix"));
		String noItems = Utils.ColorMsg(conf.getString("Messages.No-Items"));
		String Success = Utils.ColorMsg(conf.getString("Messages.Sell-Success"));
		int slot = event.getSlot();
		if (!event.getInventory().getName().equalsIgnoreCase(GUITitle)){
			return;
		}
		event.setCancelled(true);
		Player player = (Player) event.getWhoClicked();
		if (event.getCurrentItem()==null || event.getCurrentItem().getType()==Material.AIR || !event.getCurrentItem().hasItemMeta() ){
			player.closeInventory();
			return;
		}
	    Set<String> GetSlots = conf.getConfigurationSection("SellItems").getKeys(false);
		for (String gs : GetSlots){
			int slotnum = conf.getInt("SellItems." + gs + ".Slot")-1;
			if (slot == slotnum){
				ItemStack itemStack = event.getInventory().getItem(slotnum);
		        ItemMeta meta = itemStack.getItemMeta();
		        List<String> lore = meta.getLore();
				for(String line : lore) {
					if (line.contains("§4You can't sell this kind of item.")){
						player.sendMessage(Prefix + "§4You can't sell this kind of item.");
						event.setCancelled(true);
					}
					if (ChatColor.stripColor(line).startsWith("Price:")){
						String value = ChatColor.stripColor(line).replace("Price:", "").replace(" ", "").replace("$", "").replace(",", "");
						double value1 = Double.parseDouble(value);
		            		if (PSellItems.RemoveItem(itemStack, player)){
		            			player.sendMessage(Prefix + Success);
		            			econ.depositPlayer(player.getName(), value1);
		            		}
		            		else{
		            			player.sendMessage(Prefix + noItems);
		            			event.setCancelled(true);
		            		}
					}
				}
			}
			else{
				event.setCancelled(true);
			}
		}
	}
}
