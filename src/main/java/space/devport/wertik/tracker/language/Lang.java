package space.devport.wertik.tracker.language;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.config.Configuration;
import space.devport.wertik.tracker.TrackerPlugin;
import space.devport.wertik.tracker.configuration.Config;

public enum Lang {
    RELOAD("Commands.Reload.Done", "&7Done... reload took &f%time%&7ms."),

    SHOW_HEADER("Commands.Show.Header", "&6Domains"),
    SHOW_LINE("Commands.Show.Line", "&8 - &f%domain% &7( &e%unique% &8| &a%online% &7)"),
    SHOW_FOOTER("Commands.Show.Footer", "&7Unique joins: &e%unique% &7Players online: &a%online%"),

    VERSIONS_HEADER("Commands.Versions.Header", "&aVersions"),
    VERSIONS_LINE("Commands.Versions.Line", "&8 - &f%version% &7( &e%unique% &8| &a%online% &7)"),
    VERSIONS_FOOTER("Commands.Versions.Footer", "&7Unique joins: &e%unique% &7Players online: &a%online%"),

    HELP_HEADER("Commands.Help.Header", "&6Player Tracker"),
    HELP_BLANK("Commands.Help.Blank", "&6/pt &8- &7Displays this."),
    HELP_RELOAD("Commands.Reload.Help", "&6/pt reload &8- &7Reload the plugin."),
    HELP_SHOW("Commands.Show.Help", "&6/pt show &8- &7Show domain counts."),
    HELP_VERSIONS("Commands.Versions.Help", "&6/pt versions &8- &7SHow version stats."),
    HELP_FOOTER("Commands.Help.Footer", "&8|&8&m        "),

    NO_PERMISSIONS("Commands.No-Permissions", "&cYou're not allowed to do this."),
    TOO_MANY_ARGS("Commands.Too-Many-Args", "&cToo many arguments."),
    NOT_ENOUGH_ARGS("Commands.Not-Enough-Args", "&cNot enough arguments."),
    NO_CONSOLE("Commands.No-Console", "&cThis command has to be done in game."),
    NO_PLAYER("Commands.No-Player", "&cThis command has to be done from the console.");

    @Getter
    private final String path;

    @Getter
    private final String defaultValue;

    @Getter
    @Setter
    private String value;

    Lang(String path, String defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public static void load(Config config) {
        config.load();

        Configuration configuration = config.getConfiguration();

        boolean save = false;
        int count = 0;

        for (Lang entry : values()) {

            String path = entry.getPath();

            if (configuration.contains(path)) {
                String value = configuration.getString(path);
                entry.setValue(value);
            } else {
                configuration.set(path, entry.getDefaultValue());
                save = true;
                count++;
            }
        }

        if (save)
            config.save();

        if (count != 0)
            TrackerPlugin.getPlugin().getLogger().info(String.format("Added %d new message(s)...", count));
        TrackerPlugin.getPlugin().getLogger().info(String.format("Loaded %d message(s)...", values().length));
    }

    public Message get() {
        return new Message(value);
    }
}
