package stanic.stduels.utils.item;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import stanic.stduels.Main;

import java.util.HashMap;
import java.util.List;

public class ItemUtils {

    FileConfiguration settings = Main.getInstance().getSettings();
    String kitPath = "Config.kit";

    public HashMap<Integer, ItemStack> getKit() {
        HashMap<Integer, ItemStack> items = getArmors();

        ConfigurationSection others = settings.getConfigurationSection(kitPath + ".others");
        for (String path : others.getKeys(false)) {
            ItemStack selected = buildItem(kitPath + ".others." + path);
            if (selected != null) items.put(settings.getInt(kitPath + ".others." + path + ".slot"), selected);
        }
        return items;
    }

    public HashMap<Integer, ItemStack> getArmors() {
        HashMap<Integer, ItemStack> items = new HashMap<>();

        ItemStack helmet = buildItem(kitPath + ".helmet");
        ItemStack chestplate = buildItem(kitPath + ".chestplate");
        ItemStack leggings = buildItem(kitPath + ".leggings");
        ItemStack boots = buildItem(kitPath + ".boots");

        if (helmet != null) items.put(103, helmet);
        if (chestplate != null) items.put(102, chestplate);
        if (leggings != null) items.put(101, leggings);
        if (boots != null) items.put(100, boots);

        return items;
    }

    public ItemStack buildItem(String path) {
        String id = settings.getString(path + ".id");
        if (id == null) return null;

        int data = settings.getInt(path + ".data");
        List<String> enchantments = settings.getStringList(path + ".enchantments");

        ItemBuilder item = new ItemBuilder(Material.getMaterial(id)).setDurability(data);
        for (String enchantment : enchantments) item.addEnchantment(enchantment.split(":")[0], Integer.parseInt(enchantment.split(":")[1]));

        return item.build();
    }

}