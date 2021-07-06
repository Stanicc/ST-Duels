package stanic.stduels.duel.match.task;

import stanic.stduels.duel.match.Match;

public class MatchRunnable implements Runnable {

    Match match;

    public MatchRunnable(Match match) {
        this.match = match;
    }

    @Override
    public void run() {
    }

}
