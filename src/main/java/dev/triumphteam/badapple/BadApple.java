package dev.triumphteam.badapple;

import dev.triumphteam.annotations.BukkitMain;
import dev.triumphteam.badapple.commands.BadAppleCommand;
import dev.triumphteam.badapple.video.VideoManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

@BukkitMain
public final class BadApple extends JavaPlugin {

    @Override
    public void onEnable() {
        final BadAppleCommand badAppleCommand = new BadAppleCommand(new VideoManager(this));
        final PluginCommand command = getCommand("badapple");
        // Throws is the command is not found, which should never happen.
        if (command == null) throw new IllegalStateException("Could not find bad apple command.");
        command.setExecutor(badAppleCommand);
        command.setTabCompleter(badAppleCommand);
    }
}
