package space.devport.wertik.tracker.commands.framework;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import space.devport.wertik.tracker.TrackerPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SubCommand extends AbstractCommand {

    @Getter
    @Setter
    private CommandExecutor executor;

    private final List<String> aliases = new ArrayList<>();

    public SubCommand(TrackerPlugin plugin, String name) {
        super(plugin, name);
        this.executor = (sender, args) -> {
        };
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!checkPreconditions(sender, args))
            return;

        executor.onCommand(sender, args);
    }

    public SubCommand withPermission(String key) {
        setPermission(key);
        return this;
    }

    public SubCommand withRange(int wanted) {
        setRange(wanted);
        return this;
    }

    public SubCommand withRange(int min, int max) {
        setRange(min, max);
        return this;
    }

    @Override
    public SubCommand withSubCommand(SubCommand subCommand) {
        super.withSubCommand(subCommand);
        return this;
    }

    public SubCommand withExecutor(CommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    public SubCommand withAliases(String... aliases) {
        this.aliases.addAll(Arrays.stream(aliases)
                .map(String::toLowerCase)
                .collect(Collectors.toList()));
        return this;
    }

    public interface CommandExecutor {
        void onCommand(CommandSender sender, String[] args);
    }

    public List<String> getSubAliases() {
        return Collections.unmodifiableList(aliases);
    }

    public boolean matches(String arg) {
        return this.getName().equalsIgnoreCase(arg) || getSubAliases().contains(arg.toLowerCase());
    }
}
