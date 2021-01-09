package space.devport.wertik.domains.language;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.config.Configuration;
import space.devport.wertik.domains.DomainTrackerPlugin;
import space.devport.wertik.domains.configuration.Config;

public enum Lang {
    RELOAD("Commands.Reload.Done", "&7Done... reload took &f%time%&7ms."),

    SHOW_HEADER("Commands.Show.Header", "&6Domains"),
    SHOW_LINE("Commands.Show.Line", "&8 - &f%domain% &7( &e%count% &7)"),
    SHOW_FOOTER("Commands.Show.Footer", "&7Unique joins: &f%unique% &7Players online: &f%online%"),

    HELP_HEADER("Commands.Help.Header", "&6Domain Tracker"),
    HELP_BLANK("Commands.Help.Blank", "&6/dt &8- &7Displays this."),
    HELP_RELOAD("Commands.Reload.Help", "&6/dt reload &8- &7Reload the plugin."),
    HELP_SHOW("Commands.Show.Help", "&6/dt show &8- &7Show domain counts."),
    HELP_FOOTER("Commands.Help.Footer", "&8|&8&m        ");

    @Getter
    private final String path;

    @Getter
    @Setter
    private String value;

    Lang(String path, String value) {
        this.path = path;
        this.value = value;
    }

    public static void load(Config config) {
        config.load();

        Configuration configuration = config.getConfiguration();

        boolean save = false;
        int count = 0;

        for (Lang entry : values()) {
            String path = entry.getPath();
            String value = entry.getValue();

            if (configuration.contains(path)) {
                value = configuration.getString(path);
                entry.setValue(value);
            } else {
                configuration.set(path, value);
                save = true;
                count++;
            }
        }

        if (save)
            config.save();

        if (count != 0)
            DomainTrackerPlugin.getPlugin().getLogger().info(String.format("Added %d new message(s)...", count));
        DomainTrackerPlugin.getPlugin().getLogger().info(String.format("Loaded %d message(s)...", values().length));
    }

    public Message get() {
        return new Message(value);
    }
}
