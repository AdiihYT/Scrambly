package eu.adiih.scrambly;

import eu.adiih.scrambly.listeners.ChatListener;
import eu.adiih.scrambly.utils.ScramblyUtils;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ScramblyPlugin extends JavaPlugin {

    private ScramblyUtils utils;
    private String currentWord;
    private long startedAt;
    private boolean isActive = false;

    @Override
    public void onEnable() {
        utils = new ScramblyUtils();
        setupConfiguration();
        registerListeners();
        startEventTimer();
    }

    protected void setupConfiguration() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    protected void registerListeners() {
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    protected void startEventTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                isActive = true;
                String topic = utils.getRandomTopic();
                currentWord = utils.getRandomWord(topic);
                TextComponent announcementComponent = new TextComponent(
                    utils.getMessage("announcement")
                    .replace("%topic%", utils.getDisplayTopic(topic))
                );
                announcementComponent.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new Text(utils.scrambleWord(currentWord)))
                );
                startedAt = System.currentTimeMillis();
                getServer().getOnlinePlayers().forEach(player -> player.spigot().sendMessage(announcementComponent));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(!isActive) {
                            this.cancel();
                            return;
                        }
                        isActive = false;
                        getServer().broadcastMessage(utils.getMessage("timed-out").replace("%word%", currentWord));
                    }
                }.runTaskLater(ScramblyPlugin.getPlugin(ScramblyPlugin.class), getConfig().getLong("event-time") * 20L);
            }
        }.runTaskTimer(this, 20L, getConfig().getInt("interval") * 20L);
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public boolean getActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public long getStartedAt() {
        return startedAt;
    }

}
