package space.devport.wertik.domains.commands;

import net.md_5.bungee.api.CommandSender;
import space.devport.wertik.domains.DomainTrackerPlugin;
import space.devport.wertik.domains.commands.framework.CommandBase;
import space.devport.wertik.domains.language.Lang;
import space.devport.wertik.domains.language.Message;

import java.util.Map;

public class DomainTrackerCommand extends CommandBase {

    public DomainTrackerCommand(DomainTrackerPlugin plugin) {
        super(plugin, "domaintracker", "", "dt", "domains", "tracker", "dtracker");

        withSubCommand("reload")
                .withRange(0)
                .withAliases("rl")
                .withPermission("domaintracker.reload")
                .withExecutor((sender, args) -> plugin.reload(sender));

        withSubCommand("show")
                .withRange(0)
                .withAliases("list", "info")
                .withPermission("domaintracker.show")
                .withExecutor((sender, args) -> {
                    Message str = new Message(Lang.SHOW_HEADER.get()
                            .replace("online", plugin.getProxy().getOnlineCount())
                            .replace("unique", plugin.getDomainManager().getCount()));

                    String lineFormat = Lang.SHOW_LINE.get().toString();
                    for (Map.Entry<String, Integer> entry : plugin.getDomainManager().getDomains().entrySet()) {
                        str.append(lineFormat
                                .replaceAll("(?i)%domain%", entry.getKey())
                                .replaceAll("(?i)%count%", String.valueOf(entry.getValue())));
                    }

                    str.append(Lang.SHOW_FOOTER.get()
                            .replace("online", plugin.getProxy().getOnlineCount())
                            .replace("unique", plugin.getDomainManager().getCount())
                            .toString());

                    str.send(sender);
                });
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (sender.hasPermission("domaintracker.show") || sender.hasPermission("domaintracker.reload"))
            new Message(Lang.HELP_HEADER.get())
                    .append(Lang.HELP_BLANK.get().toString())
                    .append(Lang.HELP_RELOAD.get().toString())
                    .append(Lang.HELP_SHOW.get().toString())
                    .send(sender);
    }
}
