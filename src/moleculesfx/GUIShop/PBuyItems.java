package moleculesfx.GUIShop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PBuyItems{

	@SuppressWarnings("deprecation")
	public static boolean AddItem(ItemStack itemStack, Player player) {
	    Material Item = itemStack.getType();
	    short Damge = itemStack.getDurability();
	    int Quantity = itemStack.getAmount();
	    Inventory inventory = player.getInventory();
	    if (inventory.firstEmpty() != -1){
	      inventory.addItem(new ItemStack[] { new ItemStack(Item, Quantity, Damge) });
	      return true;
	    }
	    boolean AddMoney = false;
	    int Size = inventory.getSize();
	    for (int i = 0; i <= Size; i++){
	      ItemStack GetItem = inventory.getItem(i);
	      if ((GetItem != null) && 
	        (GetItem.getTypeId() == Item.getId()) && 
	        (GetItem.getDurability() == Damge) && 
	        (GetItem.getAmount() < Item.getMaxStackSize()) && 
	        (GetItem.getAmount() + Quantity <= Item.getMaxStackSize())){
	        AddMoney = true;
	        break;
	      }
	    }
	    if (AddMoney){
	      inventory.addItem(new ItemStack[] { new ItemStack(Item, Quantity, Damge) });
	      player.updateInventory();
	      return true;
	    }
		return false;
	}
}
