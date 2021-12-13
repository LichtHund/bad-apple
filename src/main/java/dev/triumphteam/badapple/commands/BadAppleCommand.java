package dev.triumphteam.badapple.commands;

import dev.triumphteam.badapple.video.VideoManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class BadAppleCommand implements CommandExecutor, TabCompleter {

    private final VideoManager videoManager;
    // Map with each sub command and its executor
    private final Map<String, Consumer<Player>> subCommands = Map.of(
            "start", this::start,
            "pause", this::pause,
            "resume", this::resume,
            "stop", this::stop,
            "screen", this::setScreen
    );

    private Location screenLocation = null;

    public BadAppleCommand(@NotNull final VideoManager videoManager) {
        this.videoManager = videoManager;
    }

    @Override
    public boolean onCommand(
            @NotNull final CommandSender sender,
            @NotNull final Command command,
            @NotNull final String label,
            @NotNull final String[] args
    ) {
        if (!(sender instanceof final Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        // Sends help command if it's too short or too long
        if (args.length != 1) {
            final TextComponent.@NotNull Builder help = Component.text()
                    .append(Component.text("Bad Apple commands:", NamedTextColor.GRAY))
                    .append(Component.newline());

            subCommands.keySet().forEach(key -> {
                help.append(Component.text("/badapple ", NamedTextColor.GRAY))
                        .append(Component.text(key, NamedTextColor.DARK_PURPLE))
                        .append(Component.newline());
            });

            player.sendMessage(help.build());
            return true;
        }

        // Executes the sub command
        final Consumer<Player> subCommand = subCommands.get(args[0]);
        if (subCommand == null) {
            player.sendMessage(Component.text("Unknown command.", NamedTextColor.RED));
            return true;
        }

        subCommand.accept(player);
        return true;
    }

    @NotNull
    @Override
    public List<String> onTabComplete(
            @NotNull final CommandSender sender,
            @NotNull final Command command,
            @NotNull final String alias,
            @NotNull final String[] args
    ) {
        // Simple tab completion for sub commands
        if (args.length == 1) return new ArrayList<>(subCommands.keySet());
        return Collections.emptyList();
    }

    /**
     * Sets the screen location to play the video at.
     *
     * @param player The player to get the target block and to send messages to.
     */
    private void setScreen(@NotNull final Player player) {
        final Block lookingBlock = player.getTargetBlockExact(10);
        if (lookingBlock == null) {
            player.sendMessage(Component.text("You must be looking at a block to set a screen.", NamedTextColor.RED));
            return;
        }

        this.screenLocation = lookingBlock.getLocation();
        player.sendMessage(Component.text("Screen set!", NamedTextColor.GREEN));
    }

    /**
     * Starts the video.
     *
     * @param player The player to send messages to.
     */
    private void start(@NotNull final Player player) {
        if (this.screenLocation == null) {
            player.sendMessage(Component.text("You must first set a screen to play the video.", NamedTextColor.RED));
            return;
        }

        if (videoManager.playAt(screenLocation)) {
            player.sendMessage(Component.text("Playing Bad Apple!", NamedTextColor.GREEN));
            return;
        }

        player.sendMessage(Component.text("Failed to play Bad Apple, video already playing!", NamedTextColor.RED));
    }

    /**
     * Resumes the video if it is paused.
     *
     * @param player The player to send messages to.
     */
    private void resume(@NotNull final Player player) {
        if (videoManager.resume()) {
            player.sendMessage(Component.text("Resumed Bad Apple!", NamedTextColor.GREEN));
            return;
        }

        player.sendMessage(Component.text("Failed to resume Bad Apple, the video is not paused!", NamedTextColor.RED));
    }

    /**
     * Pauses the video if it is playing.
     *
     * @param player The player to send messages to.
     */
    private void pause(@NotNull final Player player) {
        if (videoManager.pause()) {
            player.sendMessage(Component.text("Paused Bad Apple!", NamedTextColor.GREEN));
            return;
        }

        player.sendMessage(Component.text("Failed to pause Bad Apple, the video is not playing!", NamedTextColor.RED));
    }

    /**
     * Stops the video if it is playing.
     *
     * @param player The player to send messages to.
     */
    private void stop(@NotNull final Player player) {
        if (videoManager.stop()) {
            player.sendMessage(Component.text("Stopped Bad Apple!", NamedTextColor.GREEN));
            return;
        }

        player.sendMessage(Component.text("Failed to stop Bad Apple, the video is not playing!", NamedTextColor.RED));
    }
}
