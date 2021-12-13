package dev.triumphteam.badapple.video;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.util.List;

public final class DisplayTask implements Runnable {

    // The offset for each pixel, it is negative so the image is flipped
    // I know there are ther ways to flip the image, but this is just simpler
    private static final double OFFSET = -0.025;

    private final List<BufferedImage> frames;
    private final Location location;
    private final World world;
    private final int maxFrames;

    private int frameCounter;
    private boolean paused;

    public DisplayTask(final List<BufferedImage> frames, @NotNull final Location location) {
        this.frames = frames;
        this.location = location;
        this.world = location.getWorld();
        this.maxFrames = frames.size();
    }

    @Override
    public void run() {
        if (frameCounter >= maxFrames) {
            // Keeps a loop
            frameCounter = 0;
        }

        final BufferedImage frame = frames.get(frameCounter);

        final int width = frame.getWidth();
        final int height = frame.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final int rgb = frame.getRGB(x, y);
                final int red = (rgb >> 16) & 0xFF;
                final int green = (rgb >> 8) & 0xFF;
                final int blue = rgb & 0xFF;

                final Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(red, green, blue), 1.0F);
                world.spawnParticle(
                        Particle.REDSTONE,
                        location.clone().add(x * OFFSET, y * OFFSET, 0),
                        1,
                        dustOptions
                );
            }
        }

        if (!paused) frameCounter++;
    }

    /**
     * Checks if the task is paused.
     *
     * @return true if the task is paused, false otherwise.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Pauses the task.
     */
    public void pause() {
        paused = true;
    }

    /**
     * Resumes the task.
     */
    public void resume() {
        paused = false;
    }
}
