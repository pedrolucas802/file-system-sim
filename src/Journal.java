import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Journal {
    private final Path logPath;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Journal(String fileName) {
        this.logPath = Paths.get(fileName);
        try {
            if (!Files.exists(logPath)) {
                Files.createFile(logPath);
            }
        } catch (IOException e) {
            System.err.println("Erro ao criar arquivo de log: " + e.getMessage());
        }
    }

    public void log(String entry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logPath.toFile(), true))) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            writer.write("[" + timestamp + "] " + entry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao escrever no log: " + e.getMessage());
        }
    }

    public void printLog() {
        try {
            if (Files.exists(logPath)) {
                List<String> lines = Files.readAllLines(logPath);
                if (lines.isEmpty()) {
                    System.out.println("O log está vazio.");
                } else {
                    System.out.println("\n--- Conteúdo do journal.log ---");
                    lines.forEach(System.out::println);
                    System.out.println("--- Fim do journal.log ---\n");
                }
            } else {
                System.out.println("Arquivo de log não encontrado.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o log: " + e.getMessage());
        }
    }
}