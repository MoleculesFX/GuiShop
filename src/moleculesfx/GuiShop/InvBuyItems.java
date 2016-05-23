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

public class InvBuyItems implements Listener {
	
	public static Economy econ = null;
	private Main pl;
	
	public InvBuyItems(Economy money, Main pl){
		this.pl = pl;
		econ = money;
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void ItemPurchase(InventoryClickEvent event){
		FileConfiguration conf = this.pl.getConfig();
		String GUITitle = Utils.ColorMsg(conf.getString("GUITitle"));
		String Prefix = Utils.ColorMsg(conf.getString("Prefix"));
		String InvFull = Utils.ColorMsg(conf.getString("Messages.Inventory-Full"));
		String noMoney = Utils.ColorMsg(conf.getString("Messages.No-Money"));
		String Success = Utils.ColorMsg(conf.getString("Messages.Buy-Success"));
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
	    Set<String> GetSlots = conf.getConfigurationSection("BuyItems").getKeys(false);
		for (String gs : GetSlots){
			int slotnum = conf.getInt("BuyItems." + gs + ".Slot")-1;
			if (slot == slotnum){
				ItemStack itemStack = event.getInventory().getItem(slotnum);
		        ItemMeta meta = itemStack.getItemMeta();
		        List<String> lore = meta.getLore();
				for(String line : lore) {
					if (line.contains("§4You can't buy this kind of item.")){
						player.sendMessage(Prefix + "§4You cant buy this kind of item.");
						event.setCancelled(true);
					}
					if (ChatColor.stripColor(line).startsWith("Price:")){
						String value = ChatColor.stripColor(line).replace("Price:", "").replace(" ", "").replace("$", "").replace(",", "");
						if (value != "0" || value != "-1"){
							double value1 = Double.parseDouble(value);
							if (econ.has(player.getName(), value1)) {
			            		if (PBuyItems.AddItem(itemStack, player)){
			            			player.sendMessage(Prefix + Success);
			            			econ.withdrawPlayer(player.getName(), value1);
			            		}
			            		else{
			            			player.sendMessage(Prefix + InvFull);
			            			event.setCancelled(true);
			            		}
			            	}
			            	else{
			            		player.sendMessage(Prefix + noMoney);
			            		event.setCancelled(true);
			            	}
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
