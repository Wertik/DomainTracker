package space.devport.wertik.tracker.system;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import space.devport.wertik.tracker.ClientVersion;
import space.devport.wertik.tracker.TrackerPlugin;
import space.devport.wertik.tracker.system.struct.DomainEntry;
import space.devport.wertik.tracker.system.struct.VersionEntry;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TrackManager {

    private final TrackerPlugin plugin;

    @Getter
    private final Map<String, DomainEntry> domains = new HashMap<>();
    @Getter
    private final Map<ClientVersion, VersionEntry> versions = new HashMap<>();

    private final Set<UUID> uniquePlayers = new HashSet<>();

    private final GsonHelper gsonHelper = new GsonHelper();

    public TrackManager(TrackerPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        CompletableFuture<Set<UUID>> future = gsonHelper.loadAsync(plugin.getDataFolder() + "/data/unique-players.json", new TypeToken<Set<UUID>>() {
        }.getType());

        future.thenAcceptAsync(loaded -> {
            if (loaded == null)
                return;

            uniquePlayers.clear();
            uniquePlayers.addAll(loaded);
            plugin.getLogger().info(String.format("Loaded %d unique player(s)...", loaded.size()));
        });

        CompletableFuture<Set<VersionEntry>> versionFuture = gsonHelper.loadAsync(plugin.getDataFolder() + "/data/version-data.json", new TypeToken<Set<VersionEntry>>() {
        }.getType());

        versionFuture.thenAcceptAsync(loaded -> {
            if (loaded == null)
                return;

            versions.clear();
            for (VersionEntry entry : loaded) {
                versions.put(entry.getVersion(), entry);
            }
            plugin.getLogger().info(String.format("Loaded %d version entries...", loaded.size()));
        });

        CompletableFuture<Set<DomainEntry>> domainFuture = gsonHelper.loadAsync(plugin.getDataFolder() + "/data/domain-data.json", new TypeToken<Set<DomainEntry>>() {
        }.getType());

        domainFuture.thenAcceptAsync(loaded -> {
            if (loaded == null)
                return;

            domains.clear();
            for (DomainEntry entry : loaded) {
                domains.put(entry.getHostname(), entry);
            }
            plugin.getLogger().info(String.format("Loaded %d domain entries...", loaded.size()));
        });
    }

    public void save() {
        gsonHelper.save(uniquePlayers, plugin.getDataFolder() + "/data/unique-players.json");
        gsonHelper.save(new HashSet<>(versions.values()), plugin.getDataFolder() + "/data/version-data.json");
        gsonHelper.save(new HashSet<>(domains.values()), plugin.getDataFolder() + "/data/domain-data.json");
    }

    public DomainEntry createDomain(String hostname) {
        DomainEntry domainEntry = new DomainEntry(hostname);
        domains.put(hostname, domainEntry);
        return domainEntry;
    }

    public DomainEntry getOrCreateDomain(String hostname) {
        return domains.containsKey(hostname) ? domains.get(hostname) : createDomain(hostname);
    }

    public VersionEntry createVersion(ClientVersion version) {
        VersionEntry versionEntry = new VersionEntry(version);
        versions.put(version, versionEntry);
        return versionEntry;
    }

    public VersionEntry getOrCreateVersion(ClientVersion version) {
        return versions.containsKey(version) ? versions.get(version) : createVersion(version);
    }

    public void handleJoin(UUID uniqueId, ClientVersion version, String hostname) {

        VersionEntry versionEntry = getOrCreateVersion(version);
        DomainEntry domainEntry = getOrCreateDomain(hostname);

        versionEntry.incrementOnline();
        domainEntry.incrementOnline();

        if (uniquePlayers.contains(uniqueId))
            return;

        uniquePlayers.add(uniqueId);

        versionEntry.incrementUnique();
        domainEntry.incrementUnique();
    }

    public void handleQuit(ClientVersion version, String hostname) {
        getOrCreateVersion(version).decrementOnline();
        getOrCreateDomain(hostname).decrementOnline();
    }

    public int getUniqueJoins() {
        return uniquePlayers.size();
    }
}
