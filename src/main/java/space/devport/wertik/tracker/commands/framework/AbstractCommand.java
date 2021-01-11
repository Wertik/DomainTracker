package space.devport.wertik.tracker.commands.framework;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.jetbrains.annotations.Nullable;
import space.devport.wertik.tracker.TrackerPlugin;
import space.devport.wertik.tracker.language.Lang;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractCommand extends Command {

    protected final TrackerPlugin plugin;

    @Getter
    @Setter
    private String permission;

    @Getter
    @Setter
    private boolean consoleOnly = false;
    @Getter
    @Setter
    private boolean playerOnly = false;

    @Getter
    private Range range;

    @Getter
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public AbstractCommand(TrackerPlugin plugin, String name) {
        super(name.toLowerCase());
        this.plugin = plugin;
    }

    public AbstractCommand(TrackerPlugin plugin, String name, String permission, String... aliases) {
        super(name.toLowerCase(), permission, aliases);
        this.plugin = plugin;
    }

    public abstract void execute(CommandSender sender, String[] args);

    protected boolean checkPreconditions(CommandSender sender, String[] args) {

        if (!sender.hasPermission(permission)) {
            Lang.NO_PERMISSIONS.get().send(sender);
            return false;
        }

        if (args.length > 0) {
            Optional<SubCommand> subCommand = getSubCommands().values().stream()
                    .filter(s -> s.matches(args[0]))
                    .findAny();

            if (subCommand.isPresent()) {
                String[] cutArgs = Arrays.copyOfRange(args, 1, args.length);
                subCommand.get().execute(sender, cutArgs);
                return false;
            }
        }

        if (!checkRange(sender, getRange(), args.length))
            return false;

        if (isPlayerOnly() && !(sender instanceof ProxiedPlayer)) {
            Lang.NO_CONSOLE.get().send(sender);
            return false;
        }

        if (isConsoleOnly() && sender instanceof ProxiedPlayer) {
            Lang.NO_PLAYER.get().send(sender);
            return false;
        }

        return true;
    }

    protected boolean checkRange(CommandSender sender, @Nullable Range range, int length) {
        if (range != null) {
            int res = range.check(length);

            if (res == -1) {
                Lang.NOT_ENOUGH_ARGS.get().send(sender);
                return false;
            } else if (res == 1) {
                Lang.TOO_MANY_ARGS.get().send(sender);
                return false;
            }
        }
        return true;
    }

    public void setRange(int min, int max) {
        this.range = new Range(min, max);
    }

    public void setRange(int wanted) {
        this.range = new Range(wanted);
    }

    public AbstractCommand withSubCommand(SubCommand subCommand) {
        this.subCommands.put(subCommand.getName(), subCommand);
        return this;
    }

    public SubCommand withSubCommand(String str) {
        SubCommand subCommand = new SubCommand(plugin, str.toLowerCase());
        this.subCommands.put(subCommand.getName(), subCommand);
        return subCommand;
    }
}
