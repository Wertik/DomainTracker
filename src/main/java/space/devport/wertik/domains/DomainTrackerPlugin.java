package space.devport.wertik.domains;

import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import space.devport.wertik.domains.commands.DomainTrackerCommand;
import space.devport.wertik.domains.configuration.Config;
import space.devport.wertik.domains.language.Lang;
import space.devport.wertik.domains.listeners.PlayerListener;
import space.devport.wertik.domains.system.DomainManager;

public class DomainTrackerPlugin extends Plugin {

    @Getter
    private static DomainTrackerPlugin plugin;

    public static boolean debug = false;

    @Getter
    private final Config config = new Config(this, "config");

    @Getter
    private final Config language = new Config(this, "language");

    @Getter
    private final DomainManager domainManager = new DomainManager(this);

    @Override
    public void onEnable() {
        plugin = this;

        config.load();
        debug = config.getConfiguration().getBoolean("debug-enabled", false);

        domainManager.load();

        Lang.load(language);

        getProxy().getPluginManager().registerListener(this, new PlayerListener(this));
        getProxy().getPluginManager().registerCommand(this, new DomainTrackerCommand(this));

        getLogger().info("DomainTracker loaded successfully.");
    }

    @Override
    public void onDisable() {
        domainManager.save();
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
