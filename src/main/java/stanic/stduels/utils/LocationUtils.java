package stanic.stduels.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import stanic.stduels.Main;

import java.io.IOException;

public class LocationUtils {

    private String serialize(Location location) {
        return location.getWorld().getName() + "/" + location.getX() + "/" + location.getY() + "/" + location.getZ() + "/" + location.getPitch() + "/" + location.getYaw();
    }
    private Location deserialize(String location) {
        String[] locationSplitted = location.split("/");
        return new Location(Bukkit.getWorld(locationSplitted[0]), Double.parseDouble(locationSplitted[1]), Double.parseDouble(locationSplitted[2]), Double.parseDouble(locationSplitted[3]), Float.parseFloat(locationSplitted[4]), Float.parseFloat(locationSplitted[5]));
    }

    public Location getLocation(String location) {
        return deserialize(Main.getInstance().getSettings().getString("Config.locations." + location));
    }

    public void saveLocation(Location location, String locationName) {
        FileConfiguration settings = Main.getInstance().getSettings();
        settings.set("Config.locations." + locationName, serialize(location));

        try {
            settings.save(Main.getInstance().getSettingsFile());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
