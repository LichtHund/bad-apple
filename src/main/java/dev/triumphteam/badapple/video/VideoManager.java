package dev.triumphteam.badapple.video;

import dev.triumphteam.badapple.BadApple;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class VideoManager {

    private final BadApple plugin;
    private final List<BufferedImage> frames;

    private BukkitTask task = null;
    private DisplayTask displayTask = null;

    public VideoManager(@NotNull final BadApple plugin) {
        this.plugin = plugin;

        final File framesZip = new File(plugin.getDataFolder(), "frames.zip");
        if (!framesZip.exists()) {
            // Copy the zip to plugin folder
            plugin.saveResource("frames.zip", false);
        }

        final File framesDir = new File(plugin.getDataFolder(), "frames");
        unzip(framesZip, framesDir);

        final File[] images = framesDir.listFiles();
        if (images == null) {
            throw new IllegalStateException("Could not load frames from directory: " + framesDir.getAbsolutePath());
        }

        this.frames = new ArrayList<>(images.length);

        try {
            loadFrames(images);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the video at a given location.
     *
     * @param location The location to play the video at.
     * @return True if the video was played, false if it was not.
     */
    public boolean playAt(@NotNull final Location location) {
        if (task != null) return false;

        this.displayTask = new DisplayTask(frames, location);
        // Due to being particles running async is fine
        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, displayTask, 0L, 1L);
        return true;
    }

    /**
     * Stops the video.
     *
     * @return True if the video was stopped, false if it was not.
     */
    public boolean stop() {
        if (this.task == null) return false;

        this.task.cancel();
        this.task = null;
        this.displayTask = null;
        return true;
    }

    /**
     * Resumes the video if it was paused.
     *
     * @return True if the video was resumed, false if it was not.
     */
    public boolean resume() {
        if (this.task == null || displayTask == null || !displayTask.isPaused()) return false;

        displayTask.resume();
        return true;
    }

    /**
     * Pauses the video if it is playing.
     *
     * @return True if the video was paused, false if it was not.
     */
    public boolean pause() {
        if (this.task == null || displayTask == null || displayTask.isPaused()) return false;

        displayTask.pause();
        return true;
    }

    /**
     * Loads all the video frames into memory for faster playing.
     *
     * @param images An array with all the images in the folder.
     * @throws IOException If the image could not be loaded.
     */
    private void loadFrames(@NotNull final File[] images) throws IOException {
        plugin.getLogger().info("Loading frames...");
        for (final File file : images) {
            final BufferedImage image = ImageIO.read(file);
            if (image == null) {
                throw new IOException("Could not load image from file: " + file.getAbsolutePath() + ". Perhaps it's not an image?");
            }

            this.frames.add(image);
        }
    }

    /**
     * Unzips the frames.zip into the frames directory.
     *
     * @param zipFile The zip with all the frames for the video.
     * @param destDir The directory to unzip the zip into.
     */
    private void unzip(@NotNull final File zipFile, @NotNull final File destDir) {
        // Makes sure doesn't unzip if the directory already exists
        if (destDir.exists()) {
            final File[] files = destDir.listFiles();
            // Stupid nullable array :weary:
            if (files != null && files.length > 0) return;
        }

        plugin.getLogger().info("Unzipping frames.zip...");
        try {
            final ZipFile zip = new ZipFile(zipFile);
            final Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry entry = entries.nextElement();
                final File entryDestination = new File(destDir, entry.getName());

                if (entry.isDirectory()) {
                    entryDestination.mkdirs();
                    continue;
                }

                entryDestination.getParentFile().mkdirs();
                final InputStream inputStream = zip.getInputStream(entry);
                Files.copy(inputStream, entryDestination.toPath());
                inputStream.close();
            }

            zip.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}

