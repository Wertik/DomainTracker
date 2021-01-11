package space.devport.wertik.tracker.commands.framework;

import net.md_5.bungee.api.CommandSender;
import space.devport.wertik.tracker.TrackerPlugin;

public abstract class CommandBase extends AbstractCommand {

    public CommandBase(TrackerPlugin plugin, String name) {
        super(plugin, name);
    }

    public CommandBase(TrackerPlugin plugin, String name, String permissionKey, String... aliases) {
        super(plugin, name, "", aliases);
        setPermission(permissionKey);
    }

    public abstract void onCommand(CommandSender sender, String[] args);

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!checkPreconditions(sender, args))
            return;

        onCommand(sender, args);
    }

    public CommandBase withRange(int wanted) {
        setRange(wanted);
        return this;
    }

    public CommandBase withRange(int min, int max) {
        setRange(min, max);
        return this;
    }

    public CommandBase withPermission(String key) {
        setPermission(key);
        return this;
    }
}
