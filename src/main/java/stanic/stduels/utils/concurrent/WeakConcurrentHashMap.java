package stanic.stduels.utils.concurrent;

import org.bukkit.entity.Player;
import stanic.stduels.Main;
import stanic.stduels.duel.DuelManager;
import stanic.stduels.duel.match.Match;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class WeakConcurrentHashMap<K, V> extends ConcurrentHashMap<K, V> {

    private static final long serialVersionUID = 1L;

    private Map<K, Long> timeMap = new ConcurrentHashMap<>();
    private EntryCallback<K, V> callback;
    private long expiryInMillis;
    private boolean isAlive = true;

    public WeakConcurrentHashMap(Long expiryInMillis, EntryCallback<K, V> callback) {
        this.expiryInMillis = expiryInMillis;
        this.callback = callback;
        initialize();
    }

    void initialize() {
        new CleanerThread().start();
    }

    public Long getRemainingTime(K key) {
        if (!timeMap.containsKey(key)) return 0L;

        long currentTime = new Date().getTime();
        return (timeMap.get(key) + expiryInMillis) - currentTime;
    }

    @Override
    public V put(K key, V value) {
        if (!isAlive) {
            initialize();
        }
        Date date = new Date();
        timeMap.put(key, date.getTime());

        V returnVal = super.put(key, value);

        callback.onAdd(key, value);
        return returnVal;
    }

    @Override
    public boolean containsKey(Object key) {
        long currentTime = new Date().getTime();
        if (timeMap.containsKey(key)) {
            if (((timeMap.get(key) + expiryInMillis) - currentTime) < 0L) {
                timeMap.remove(key);
                remove(key);
            }
        }

        return super.containsKey(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (!isAlive) {
            initialize();
        }
        for (K key : m.keySet()) {
            put(key, m.get(key));
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {
        if (!isAlive) {
            initialize();
        }
        if (!containsKey(key)) {
            return put(key, value);
        } else {
            return get(key);
        }
    }

    public V removeFromMap(K key, Boolean decline) {
        if (!isAlive) {
            initialize();
        }
        
        if (containsKey(key)) {
            timeMap.remove(key);
            V value = remove(key);

            if (decline) {
                DuelManager duelManager = Main.getInstance().getDuelManager();
                Optional<Match> match = duelManager.getMatchByPlayer(((Player) key).getUniqueId());
                match.ifPresent(duelManager::removeMatch);
            }

            callback.onRemove(key, value);

            return value;
        }

        return null;
    }

    public void quitMap() {
        isAlive = false;
    }

    class CleanerThread extends Thread {

        @Override
        public void run() {
            while (isAlive) {
                cleanMap();
                try {
                    Thread.sleep(expiryInMillis / 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void cleanMap() {
            long currentTime = new Date().getTime();
            for (K key : timeMap.keySet()) {
                if (((timeMap.get(key) + expiryInMillis) - currentTime) < 0L) {
                    V value = removeFromMap(key, true);
                    timeMap.remove(key, value);
                }
            }
        }
    }

}