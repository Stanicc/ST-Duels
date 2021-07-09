package stanic.stduels;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import stanic.stduels.commands.DuelCommand;
import stanic.stduels.database.Database;
import stanic.stduels.database.impl.IDatabase;
import stanic.stduels.duel.DuelManager;
import stanic.stduels.events.DuelListeners;
import stanic.stduels.factory.DuelFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Main extends JavaPlugin {

    private IDatabase database;
    private DuelManager duelManager;

    private File settingsFile;
    private FileConfiguration settings;

    @Override
    public void onEnable() {
        loadSettings();
        duelManager = new DuelManager();

        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                new Database().start();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        });

        getCommand("duel").setExecutor(new DuelCommand());
        getServer().getPluginManager().registerEvents(new DuelListeners(this), this);
    }

    @Override
    public void onDisable() {
        try {
            new DuelFactory().save();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void loadSettings() {
        settingsFile = new File(getDataFolder(), "settings.yml");
        if (!settingsFile.exists()) {
            settingsFile.getParentFile().mkdirs();
            saveResource("settings.yml", false);
        }
        settings = new YamlConfiguration();
        try {
            settings.load(settingsFile);
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
    }

    public DuelManager getDuelManager() {
        return duelManager;
    }

    public File getSettingsFile() {
        return settingsFile;
    }

    public FileConfiguration getSettings() {
        return settings;
    }

    public IDatabase getDb() {
        return database;
    }

    public void setDatabase(IDatabase database) {
        this.database = database;
    }

    public static Main getInstance() {
        return getPlugin(Main.class);
    }

}
