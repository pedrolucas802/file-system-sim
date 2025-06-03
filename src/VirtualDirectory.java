import java.nio.file.Path;

public class VirtualDirectory {
    private final Path path;

    public VirtualDirectory(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}