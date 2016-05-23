package moleculesfx.GUIShop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PSellItems {

	public static boolean RemoveItem(ItemStack itemStack, Player player) {
	    Material item = itemStack.getType();
	    short Damge = itemStack.getDurability();
	    int Quantity = itemStack.getAmount();
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
	      inventory.removeItem(new ItemStack[] { new ItemStack(item, Quantity, Damge) });
	      return true;
	    }
		return false;
	}
}
