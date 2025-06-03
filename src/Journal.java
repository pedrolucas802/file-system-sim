import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;

public class Journal {
    private final Path logPath;

    public Journal(String filename) throws IOException {
        this.logPath = Paths.get(filename);
        if (!Files.exists(logPath)) {
            Files.write(logPath, ("--- Log criado em " + LocalDateTime.now() + " ---\n").getBytes(),
                    StandardOpenOption.CREATE);
        }
    }

    public void log(String entry) throws IOException {
        Files.write(logPath,
                (LocalDateTime.now() + " - " + entry + "\n").getBytes(),
                StandardOpenOption.APPEND);
    }

    public void showLog() throws IOException {
        Files.lines(logPath).forEach(System.out::println);
    }
}