package stanic.stduels.task;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import stanic.stduels.Main;
import stanic.stduels.factory.DuelFactory;

import java.sql.SQLException;

public class SaveTask {

    BukkitTask task;

    public void run() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    new DuelFactory().save();
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 20*60, 20*60*10);
    }

    public BukkitTask getTask() {
        return task;
    }

}