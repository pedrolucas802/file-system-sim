import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;

public class Journal {
    private final Path journalPath;

    public Journal(String filename) {
        this.journalPath = Paths.get(filename);
    }

    public void log(String message) throws IOException {
        Files.write(journalPath,
                (LocalDateTime.now() + " - " + message + System.lineSeparator()).getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
