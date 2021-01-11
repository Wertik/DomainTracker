package space.devport.wertik.tracker.commands;

import net.md_5.bungee.api.CommandSender;
import space.devport.wertik.tracker.TrackerPlugin;
import space.devport.wertik.tracker.commands.framework.CommandBase;
import space.devport.wertik.tracker.language.Lang;
import space.devport.wertik.tracker.language.Message;
import space.devport.wertik.tracker.system.struct.DomainEntry;
import space.devport.wertik.tracker.system.struct.VersionEntry;

public class TrackerCommand extends CommandBase {

    public TrackerCommand(TrackerPlugin plugin) {
        super(plugin, "playertracker", "", "pt", "domains", "tracker", "ptracker", "versions");

        withSubCommand("reload")
                .withRange(0)
                .withAliases("rl")
                .withPermission("playertracker.reload")
                .withExecutor((sender, args) -> plugin.reload(sender));

        withSubCommand("show")
                .withRange(0)
                .withAliases("list", "info", "domains", "domain", "d")
                .withPermission("playertracker.show")
                .withExecutor((sender, args) -> {
                    Message str = new Message(Lang.SHOW_HEADER.get()
                            .replace("online", plugin.getProxy().getOnlineCount())
                            .replace("unique", plugin.getTrackManager().getUniqueJoins()));

                    String lineFormat = Lang.SHOW_LINE.get().toString();
                    for (DomainEntry entry : plugin.getTrackManager().getDomains().values()) {
                        str.append(lineFormat
                                .replaceAll("(?i)%domain%", entry.getHostname())
                                .replaceAll("(?i)%online%", String.valueOf(entry.getOnline()))
                                .replaceAll("(?i)%unique%", String.valueOf(entry.getUnique())));
                    }

                    str.append(Lang.SHOW_FOOTER.get()
                            .replace("online", plugin.getProxy().getOnlineCount())
                            .replace("unique", plugin.getTrackManager().getUniqueJoins())
                            .toString());

                    str.send(sender);
                });

        withSubCommand("versions")
                .withRange(0)
                .withAliases("ver", "v", "version")
                .withPermission("playertracker.versions")
                .withExecutor((sender, args) -> {
                    Message str = new Message(Lang.VERSIONS_HEADER.get()
                            .replace("online", plugin.getProxy().getOnlineCount())
                            .replace("unique", plugin.getTrackManager().getUniqueJoins()));

                    String lineFormat = Lang.VERSIONS_LINE.get().toString();
                    for (VersionEntry entry : plugin.getTrackManager().getVersions().values()) {
                        str.append(lineFormat
                                .replaceAll("(?i)%version%", entry.getVersion().getName())
                                .replaceAll("(?i)%online%", String.valueOf(entry.getOnline()))
                                .replaceAll("(?i)%unique%", String.valueOf(entry.getUnique())));
                    }

                    str.append(Lang.VERSIONS_FOOTER.get()
                            .replace("online", plugin.getProxy().getOnlineCount())
                            .replace("unique", plugin.getTrackManager().getUniqueJoins())
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
                    .append(Lang.HELP_VERSIONS.get().toString())
                    .send(sender);
    }
}
