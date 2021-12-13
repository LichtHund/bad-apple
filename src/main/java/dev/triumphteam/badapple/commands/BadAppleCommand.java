package dev.triumphteam.badapple.commands;

import dev.triumphteam.badapple.video.VideoManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class BadAppleCommand implements CommandExecutor {

    private final VideoManager videoManager;

    public BadAppleCommand(@NotNull final VideoManager videoManager) {
        this.videoManager = videoManager;
    }

    @Override
    public boolean onCommand(
            @NotNull final CommandSender sender,
            @NotNull final Command command,
            @NotNull final String label,
            final @NotNull String[] args
    ) {

        System.out.println("bad apple");
        return true;
    }
}
