package main.CatanAssignment1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Parses configuration from a config file.
 * Expected format: "turns &lt;int&gt;" (max 8192).
 */
public class Config {

    private static final int DEFAULT_TURNS = 100;
    private static final int MAX_TURNS = 8192;

    private int maxTurns;

    public Config(String configPath) {
        this.maxTurns = parseTurns(configPath);
    }

    public int getMaxTurns() {
        return maxTurns;
    }

    private int parseTurns(String configPath) {
        try {
            Path path = Paths.get(configPath);
            if (!Files.exists(path)) {
                return DEFAULT_TURNS;
            }
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                line = line.trim().toLowerCase();
                if (line.startsWith("turns ")) {
                    String val = line.substring(6).trim();
                    int t = Integer.parseInt(val);
                    return Math.min(Math.max(1, t), MAX_TURNS);
                }
            }
        } catch (IOException | NumberFormatException e) {
            // fallback to default
        }
        return DEFAULT_TURNS;
    }
}
