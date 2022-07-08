package eu.adiih.scrambly.utils;

import eu.adiih.scrambly.ScramblyPlugin;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ScramblyUtils {

    private final ScramblyPlugin plugin = ScramblyPlugin.getPlugin(ScramblyPlugin.class);

    public String scrambleWord(String word) {
        List<Character> characters = new ArrayList<>();
        for(char c : word.toCharArray()) characters.add(c);
        StringBuilder output = new StringBuilder(word.length());
        while(characters.size() != 0) {
            int randPicker = (int)(Math.random() * characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }

    public String getRandomTopic() {
        Set<String> topics = plugin.getConfig().getConfigurationSection("topics").getKeys(false);
        int rand = new Random().nextInt(topics.size());
        int i = 0;
        for(String topic : topics) {
            if(i == rand) return topic;
            i++;
        }
        return null;
    }

    public String getRandomWord(String topic) {
        List<String> words = plugin.getConfig().getStringList("topics." + topic + ".words");
        return words.get((int)(Math.random() * words.size()));
    }

    public String getDisplayTopic(String topic) {
        return plugin.getConfig().getString("topics." + topic + ".display");
    }

    public String getMessage(String key) {
        return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages." + key).replace("\\n", "\n"));
    }

}
