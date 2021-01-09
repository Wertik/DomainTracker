package space.devport.wertik.domains;

import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class TextUtil {

    public void sendMessage(CommandSender sender, String message) {
        if (!Strings.isNullOrEmpty(message) && sender != null)
            sender.sendMessage(format(message));
    }

    public TextComponent format(String message) {
        return new TextComponent(color(message));
    }

    public String color(String message) {
        return message == null ? "" : ChatColor.translateAlternateColorCodes('&', message);
    }

    public <T> String joinStream(String delimiter, Stream<T> s, Function<T, String> conv) {
        return s.map(conv)
                .collect(Collectors.joining(delimiter));
    }
}
