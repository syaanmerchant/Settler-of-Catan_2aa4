package CatanAssignment1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigTest {

    @TempDir
    Path tmpDir;

    // valid file → parsed correctly
    @Test
    void config_validFile_returnsCorrectTurns() throws IOException {
        Path f = tmpDir.resolve("config.txt");
        Files.writeString(f, "turns 50");
        Config cfg = new Config(f.toString());
        assertEquals(50, cfg.getMaxTurns());
    }

    // missing file → default 100
    @Test
    void config_missingFile_defaultsTo100() {
        Config cfg = new Config(tmpDir.resolve("nope.txt").toString());
        assertEquals(100, cfg.getMaxTurns());
    }

    // huge value → clamped to 8192
    @Test
    void config_valueExceedsCap_clampedTo8192() throws IOException {
        Path f = tmpDir.resolve("big.txt");
        Files.writeString(f, "turns 99999");
        Config cfg = new Config(f.toString());
        assertEquals(8192, cfg.getMaxTurns());
    }
}
