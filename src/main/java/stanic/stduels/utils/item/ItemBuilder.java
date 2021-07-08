package stanic.stduels.utils.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ItemBuilder {

    ItemStack item;

    public ItemBuilder(Material item) {
        this.item = new ItemStack(item);
    }
    public ItemBuilder(ItemStack item) { this.item = item; }

    public ItemBuilder setDurability(int durability) {
        item.setDurability((short) durability);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder setLore(ArrayList<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder setAmount(Integer amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder addEnchantment(String enchantmentName, Integer level) {
        Enchantment enchantment = Enchantment.getByName(enchantmentName);
        if (enchantment != null) item.addEnchantment(enchantment, level);
        return this;
    }

    public ItemStack build() { return item; }

}