package stanic.stduels;

import org.bukkit.plugin.java.JavaPlugin;
import stanic.stduels.duel.DuelManager;

public class Main extends JavaPlugin {

    private DuelManager duelManager;

    @Override
    public void onEnable() {
        duelManager = new DuelManager();
    }

    public static Main getInstance() {
        return getPlugin(Main.class);
    }

}
