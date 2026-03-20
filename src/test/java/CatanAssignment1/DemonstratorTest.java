package CatanAssignment1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class DemonstratorTest {

    private final InputStream origIn = System.in;

    @AfterEach
    void restoreSystemIn() {
        System.setIn(origIn);
    }

    // default config path doesn't exist → 100 turns, "Go" is pre-roll so scanner drains
    @Test
    void main_runsWithoutException_defaultConfig() {
        System.setIn(new ByteArrayInputStream("Go\n".getBytes()));
        assertDoesNotThrow(() -> Demonstrator.main(new String[]{"config/config.txt"}));
    }

    // explicit config with 2 turns and proper Roll/Go input
    @Test
    void main_runsWithExplicitConfig() throws IOException {
        Path tmp = Files.createTempFile("cfg", ".txt");
        Files.writeString(tmp, "turns 2");
        tmp.toFile().deleteOnExit();

        // 2 rounds: each needs Roll+Go for human, Go for each of 3 machine waitForGoGate
        String input = "Roll\nGo\nGo\nGo\nGo\nRoll\nGo\nGo\nGo\nGo\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        assertDoesNotThrow(() -> Demonstrator.main(new String[]{tmp.toString()}));
    }

    // empty input → human player hits EOF on every read
    @Test
    void main_handlesEmptyInput() {
        System.setIn(new ByteArrayInputStream(new byte[0]));
        assertDoesNotThrow(() -> Demonstrator.main(new String[]{}));
    }
}
