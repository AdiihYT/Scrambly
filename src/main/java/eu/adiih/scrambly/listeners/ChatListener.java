package eu.adiih.scrambly.listeners;

import eu.adiih.scrambly.ScramblyPlugin;
import eu.adiih.scrambly.utils.ScramblyUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatListener implements Listener {

    private final ScramblyPlugin plugin = ScramblyPlugin.getPlugin(ScramblyPlugin.class);
    private final ScramblyUtils utils = new ScramblyUtils();

    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        if(!plugin.getActive()) return;
        if(!message.equalsIgnoreCase(plugin.getCurrentWord())) return;
        plugin.setActive(false);
        event.setCancelled(true);
        long elapsed = System.currentTimeMillis() - plugin.getStartedAt();
        plugin.getServer().broadcastMessage(
            utils.getMessage("unscrambled")
            .replace("%player%", event.getPlayer().getName())
            .replace("%seconds%", String.valueOf(elapsed / 10 / 100.0))
        );
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getConfig().getStringList("reward-commands").forEach(command -> plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command.replace("%player%", event.getPlayer().getName())));
            }
        }.runTask(plugin);

    }

}
