package knugel.structurebounds;

import org.bukkit.Color;

public class Config {
    private int radius;
    private float particleDistance;
    private Color color;
    private int frequency;

    public Config(int radius, float particleDistance, int color, int frequency) {
        this.radius = radius;
        this.particleDistance = particleDistance;
        this.color = Color.fromRGB(color);
        this.frequency = frequency;
    }

    public int getRadiusSquared() {
        return radius * radius;
    }

    public float getParticleDistance() {
        return particleDistance;
    }

    public Color getColor() {
        return color;
    }

    public int getFrequency() {
        return frequency;
    }
}
