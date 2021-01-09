package space.devport.wertik.domains.system;

import space.devport.wertik.domains.DomainTrackerPlugin;
import space.devport.wertik.domains.configuration.Config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DomainManager {

    private final DomainTrackerPlugin plugin;

    private final Map<String, Integer> domains = new HashMap<>();

    private final AtomicInteger totalJoined = new AtomicInteger();

    private final Config storage;
    private final Config data;

    public DomainManager(DomainTrackerPlugin plugin) {
        this.plugin = plugin;
        this.storage = new Config(plugin, "domains");
        this.data = new Config(plugin, "data");
    }

    public void load() {
        storage.load();

        Map<String, Integer> loaded = new HashMap<>();

        for (String key : storage.getConfiguration().getKeys()) {
            int count = storage.getConfiguration().getInt(key);
            String hostname = key.replace("_", ".");
            loaded.put(hostname, count);
        }

        domains.clear();
        domains.putAll(loaded);

        plugin.getLogger().info(String.format("Loaded %d domain(s)...", loaded.size()));

        data.load();
        totalJoined.set(data.getConfiguration().getInt("total-joined", 0));

        plugin.getLogger().info(String.format("Loaded %d unique player count...", totalJoined.get()));
    }

    public void save() {
        data.getConfiguration().set("total-joined", totalJoined.get());
        data.save();
        plugin.getLogger().info(String.format("Saved %d unique player count...", totalJoined.get()));

        for (Map.Entry<String, Integer> entry : domains.entrySet()) {
            String key = entry.getKey().replace(".", "_");
            storage.getConfiguration().set(key, entry.getValue());
        }
        storage.save();
        plugin.getLogger().info(String.format("Saved %d domain(s)...", domains.size()));
    }

    public Map<String, Integer> getDomains() {
        return Collections.unmodifiableMap(domains);
    }

    public int getCount(String hostname) {
        return domains.getOrDefault(hostname, 0);
    }

    public int updateCount(String hostname) {
        int count = getCount(hostname) + 1;
        domains.put(hostname, count);
        return count;
    }

    public int incrementTotal() {
        return totalJoined.incrementAndGet();
    }

    public int getCount() {
        return totalJoined.get();
    }
}
