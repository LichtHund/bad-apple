package dev.triumphteam.badapple;

import dev.triumphteam.annotations.BukkitMain;
import dev.triumphteam.badapple.commands.BadAppleCommand;
import dev.triumphteam.badapple.video.VideoManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

@BukkitMain
public final class BadApple extends JavaPlugin {

    private final VideoManager videoManager = new VideoManager();

    @Override
    public void onEnable() {
        final PluginCommand badAppleCommand = getCommand("badapple");
        // Throws is the command is not found, which should never happen.
        if (badAppleCommand == null) throw new IllegalStateException("Could not find bad apple command.");
        badAppleCommand.setExecutor(new BadAppleCommand(videoManager));
    }
}
