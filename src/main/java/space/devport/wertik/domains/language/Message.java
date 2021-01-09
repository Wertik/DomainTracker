package space.devport.wertik.domains.language;

import com.google.common.base.Strings;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import space.devport.wertik.domains.TextUtil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Message {

    @Getter
    private List<String> content = new LinkedList<>();

    public Message(Message message) {
        this.content.addAll(message.getContent());
    }

    public Message(String content) {
        this.content.add(content);
    }

    public Message(String[] content) {
        this.content.addAll(Arrays.asList(content));
    }

    public Message append(String str) {
        this.content.add(str);
        return this;
    }

    public Message replace(String str, Object obj) {
        content = content.stream()
                .map(l -> l.replaceAll("(?i)%" + str + "%", String.valueOf(obj)))
                .collect(Collectors.toList());
        return this;
    }

    public void send(CommandSender sender) {
        if (content.stream().allMatch(Strings::isNullOrEmpty))
            return;

        if (sender instanceof ProxiedPlayer) {
            TextUtil.sendMessage(sender, toString());
        } else {
            for (String line : content)
                TextUtil.sendMessage(sender, line);
        }
    }

    public String toString(boolean color) {
        return color ? TextUtil.color(toString()) : toString();
    }

    @Override
    public String toString() {
        return String.join("\n", content);
    }
}
