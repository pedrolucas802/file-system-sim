import java.io.IOException;
import java.nio.file.*;

public class VirtualFile {
    private final Path path;

    public VirtualFile(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public void create() throws IOException {
        Files.createDirectories(path.getParent());
        Files.createFile(path);
    }

    public void delete() throws IOException {
        Files.deleteIfExists(path);
    }
}