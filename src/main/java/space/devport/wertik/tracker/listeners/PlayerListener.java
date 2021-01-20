package space.devport.wertik.tracker.listeners;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import space.devport.wertik.tracker.ClientVersion;
import space.devport.wertik.tracker.TrackerPlugin;
import space.devport.wertik.tracker.system.TrackManager;

import java.util.UUID;

public class PlayerListener implements Listener {

    private final TrackManager trackManager;

    public PlayerListener(TrackerPlugin plugin) {
        this.trackManager = plugin.getTrackManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PostLoginEvent event) {
        UUID uniqueId = event.getPlayer().getUniqueId();

        String hostname = event.getPlayer().getPendingConnection().getVirtualHost().getHostName();
        ClientVersion version = ClientVersion.fromProtocol(event.getPlayer().getPendingConnection().getVersion());

        trackManager.handleJoin(uniqueId, version, hostname);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerDisconnectEvent event) {
        String hostname = event.getPlayer().getPendingConnection().getVirtualHost().getHostName();
        ClientVersion version = ClientVersion.fromProtocol(event.getPlayer().getPendingConnection().getVersion());

        trackManager.handleQuit(version, hostname);
    }
}
