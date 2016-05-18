package moleculesfx.GUIShop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class PSellItems {

	public static boolean RemoveItem(ItemStack itemStack, Player player) {
	    Material item = Material.getMaterial(itemStack.getTypeId());
	    short Damge = itemStack.getDurability();
	    int Quantity = itemStack.getAmount();
	    ItemStack items = new ItemStack(item, Quantity, Damge);
	    int NumItem = 0;
	    Inventory inventory = player.getInventory();
	    int size = inventory.getSize();
	    for (int i = 0; i <= size; i++)
	    {
	      ItemStack ItemFor = inventory.getItem(i);
	      if ((ItemFor != null) && 
	        (ItemFor.getType().equals(item)) && (ItemFor.getDurability() == Damge && !ItemFor.hasItemMeta()))
	      {
	        NumItem += ItemFor.getAmount();
	        if (NumItem >= Quantity) {
	          break;
	        }
	      }
	    }
	    if (NumItem >= Quantity)
	    {
	      inventory.remove(items);
	      return true;
	    }
		return false;
	}
}
