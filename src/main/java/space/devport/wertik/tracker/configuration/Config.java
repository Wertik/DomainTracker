package space.devport.wertik.tracker.configuration;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import space.devport.wertik.tracker.TextUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class Config {

    private final Plugin plugin;

    // Name with extension
    @Getter
    private final String name;

    @Getter
    private final File file;
    @Getter
    private final ConfigurationProvider configurationProvider;

    @Getter
    private Configuration configuration;

    public Config(Plugin plugin, String name) {
        this.plugin = plugin;

        String finalName = name.contains(".yml") ? name : name.concat(".yml");
        this.name = finalName;
        this.file = new File(plugin.getDataFolder(), finalName);

        this.configurationProvider = ConfigurationProvider.getProvider(YamlConfiguration.class);
    }

    private boolean create() {
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs())
            return false;

        InputStream in = plugin.getResourceAsStream(name);

        try {
            if (in == null) {
                if (!file.createNewFile())
                    plugin.getProxy().getLogger().severe("Could not create file " + name);
                else
                    return true;
            } else {
                Files.copy(in, file.toPath());
                return true;
            }
        } catch (IOException e) {
            plugin.getProxy().getLogger().severe("Could not create file " + name);
            e.printStackTrace();
        }
        return false;
    }

    public boolean load() {

        if (!file.exists() && !create())
            return false;

        try {
            configuration = configurationProvider.load(file);
        } catch (IOException e) {
            plugin.getProxy().getLogger().severe("Could not load " + name);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void delete() {

        if (!file.exists())
            return;

        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        delete();
        load();
    }

    public boolean save() {
        try {
            configurationProvider.save(configuration, file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get line or list message.
     */
    public String getMessage(String key) {
        Object obj = getConfiguration().get(key);
        String message = null;
        if (obj instanceof String)
            message = (String) obj;
        else if (obj instanceof List<?>)
            message = String.join("\n&r", getConfiguration().getStringList(key));
        return TextUtil.color(message);
    }
}
