package space.devport.wertik.tracker;

import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import space.devport.wertik.tracker.commands.TrackerCommand;
import space.devport.wertik.tracker.configuration.Config;
import space.devport.wertik.tracker.language.Lang;
import space.devport.wertik.tracker.listeners.PlayerListener;
import space.devport.wertik.tracker.system.TrackManager;

public class TrackerPlugin extends Plugin {

    //TODO: Save Users instead of domains and versions separately.
    //TODO: Add user info.
    //TODO: Add messages when a player joins to players with certain permissions.

    @Getter
    private static TrackerPlugin plugin;

    public static boolean debug = false;

    @Getter
    private final Config config = new Config(this, "config");

    @Getter
    private final Config language = new Config(this, "language");

    @Getter
    private final TrackManager trackManager = new TrackManager(this);

    @Override
    public void onEnable() {
        plugin = this;

        config.load();
        debug = config.getConfiguration().getBoolean("debug-enabled", false);

        trackManager.load();

        Lang.load(language);

        getProxy().getPluginManager().registerListener(this, new PlayerListener(this));
        getProxy().getPluginManager().registerCommand(this, new TrackerCommand(this));

        getLogger().info("PlayerTracker loaded successfully.");
    }

    @Override
    public void onDisable() {
        trackManager.save();
    }

    public void reload(CommandSender sender) {
        long start = System.currentTimeMillis();

        config.load();
        debug = config.getConfiguration().getBoolean("debug-enabled", false);

        Lang.load(language);

        Lang.RELOAD.get()
                .replace("time", (System.currentTimeMillis() - start))
                .send(sender);
    }

    public static void debug(String str) {
        if (debug)
            ProxyServer.getInstance().getLogger().info(str);
    }
}
