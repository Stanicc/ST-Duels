package stanic.stduels.duel.match.task;

import org.bukkit.scheduler.BukkitTask;
import stanic.stduels.duel.match.Match;
import stanic.stduels.duel.match.state.MatchState;

import java.util.concurrent.atomic.AtomicInteger;

public class MatchRunnable implements Runnable {

    Match match;
    BukkitTask task;

    AtomicInteger delay = new AtomicInteger(10);

    public MatchRunnable(Match match) {
        this.match = match;
    }

    @Override
    public void run() {
        if (match.getState() == MatchState.STARTING) {
            if (delay.get() == 10) //send message
            if (delay.decrementAndGet() <= 3) {
                //send starting message
            }

            if (delay.get() == 0) match.setState(MatchState.RUNNING);
            return;
        }

        //run tie
    }

    public BukkitTask getTask() {
        return task;
    }
    public void setTask(BukkitTask task) {
        this.task = task;
    }

}
