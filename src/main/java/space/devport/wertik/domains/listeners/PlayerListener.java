package space.devport.wertik.domains.listeners;

import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import space.devport.wertik.domains.DomainTrackerPlugin;

public class PlayerListener implements Listener {

    private final DomainTrackerPlugin plugin;

    public PlayerListener(DomainTrackerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(LoginEvent event) {
        String hostname = event.getConnection().getVirtualHost().getHostName();
        DomainTrackerPlugin.debug("Handshake for " + event.getConnection().getName() + ", host: " + hostname);

        plugin.getDomainManager().updateCount(hostname);
        plugin.getDomainManager().incrementTotal();
    }
}
