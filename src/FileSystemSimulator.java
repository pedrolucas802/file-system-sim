
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class FileSystemSimulator {
    private static final String ROOT = "filesystem/";
    private static final String LOG_FILE = "journal.log";
    private final Journal journal;
    private Path currentPath = Paths.get(ROOT);

    public FileSystemSimulator() throws IOException {
        Files.createDirectories(currentPath);
        this.journal = new Journal(LOG_FILE);
        journal.log("Sistema iniciado");
    }

    private void rotateLog() throws IOException {
        Path logPath = Paths.get(LOG_FILE);
        if (Files.exists(logPath)) {
            String backupName = "journal_" + System.currentTimeMillis() + ".log";
            Files.move(logPath, Paths.get(backupName), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public String getPromptPath() {
        return currentPath.toString().replace(ROOT, "/");
    }

    public void printMenu() {
        System.out.println("\nComandos disponíveis:");
        System.out.println("mkdir <caminho>          - Criar diretório");
        System.out.println("rmdir <caminho>          - Remover diretório");
        System.out.println("rename-dir <orig> <dest> - Renomear diretório");
        System.out.println("touch <arquivo>          - Criar arquivo");
        System.out.println("rm <arquivo>             - Remover arquivo");
        System.out.println("rename <orig> <dest>     - Renomear arquivo");
        System.out.println("cp <orig> <dest>         - Copiar arquivo");
        System.out.println("ls [caminho]             - Listar arquivos");
        System.out.println("cd <dir>/..              - Navegar entre diretórios (vazio volta à raiz)");
        System.out.println("pwd                      - Caminho atual");
        System.out.println("log                      - Exibir conteúdo do journal.log (cat journal.log)");
        System.out.println("menu                     - Mostrar comandos");
        System.out.println("exit                     - Sair do simulador\n");
    }

    public void handleCommand(String input) throws IOException {
        String[] cmd = input.trim().split(" ", 2);
        String action = cmd[0];
        String params = cmd.length > 1 ? cmd[1] : "";

        try {
            switch (action) {
                case "mkdir" -> createDirectory(params);
                case "rmdir" -> deleteDirectory(params);
                case "rename-dir" -> renameDirectory(params);
                case "touch" -> createFile(params);
                case "rm" -> deleteFile(params);
                case "rename" -> renameFile(params);
                case "cp" -> copyFile(params);
                case "ls" -> listDirectory(params);
                case "cd" -> changeDirectory(params);
                case "pwd" -> System.out.println(getPromptPath());
                case "menu" -> printMenu();
                case "log" -> showLog();
                case "exit" -> {
                    journal.log("Sistema finalizado");
                    System.exit(0);
                }
                default -> System.out.println("Comando não reconhecido");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void showLog() throws IOException {
        Path logPath = Paths.get(LOG_FILE);
        if (Files.exists(logPath)) {
            System.out.println("\n--- Conteúdo do journal.log ---");
            Files.lines(logPath).forEach(System.out::println);
            System.out.println("--- Fim do journal.log ---\n");
        } else {
            System.out.println("Arquivo de log não encontrado.");
        }
    }

    private void createDirectory(String path) throws IOException {
        VirtualDirectory dir = new VirtualDirectory(currentPath.resolve(path));
        Files.createDirectories(dir.getPath());
        journal.log("Criado diretório: " + dir.getPath());
    }

    private void deleteDirectory(String path) throws IOException {
        Path dir = currentPath.resolve(path);
        if (Files.exists(dir)) {
            Files.walk(dir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try { Files.delete(p); } catch (IOException ignored) {}
                    });
            journal.log("Removido diretório: " + dir);
        }
    }

    private void renameDirectory(String params) throws IOException {
        String[] parts = params.split(" ");
        if (parts.length != 2) throw new IllegalArgumentException("Parâmetros inválidos para rename-dir");
        Files.move(currentPath.resolve(parts[0]), currentPath.resolve(parts[1]));
        journal.log("Renomeado diretório: " + parts[0] + " -> " + parts[1]);
    }

    private void createFile(String path) throws IOException {
        VirtualFile file = new VirtualFile(currentPath.resolve(path));
        file.create();
        journal.log("Criado arquivo: " + file.getPath());
    }

    private void deleteFile(String path) throws IOException {
        VirtualFile file = new VirtualFile(currentPath.resolve(path));
        file.delete();
        journal.log("Removido arquivo: " + file.getPath());
    }

    private void renameFile(String params) throws IOException {
        String[] parts = params.split(" ");
        if (parts.length != 2) throw new IllegalArgumentException("Parâmetros inválidos para rename");
        Files.move(currentPath.resolve(parts[0]), currentPath.resolve(parts[1]));
        journal.log("Renomeado arquivo: " + parts[0] + " -> " + parts[1]);
    }

    private void copyFile(String params) throws IOException {
        String[] parts = params.split(" ");
        if (parts.length != 2) throw new IllegalArgumentException("Parâmetros inválidos para cp");
        Path src = currentPath.resolve(parts[0]);
        Path dest = currentPath.resolve(parts[1]);
        if (!Files.exists(src)) throw new FileNotFoundException("Arquivo de origem não encontrado: " + parts[0]);
        Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
        journal.log("Copiado arquivo: " + parts[0] + " -> " + parts[1]);
    }

    private void listDirectory(String path) throws IOException {
        Path dir = path.isEmpty() ? currentPath : currentPath.resolve(path);
        if (!Files.isDirectory(dir)) throw new FileNotFoundException("Diretório não encontrado: " + dir);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path entry : stream) {
                System.out.println(entry.getFileName());
            }
        }
        //journal.log("Listou conteúdo de: " + dir);
    }

    private void changeDirectory(String path) throws IOException {
        if (path.isEmpty()) {
            currentPath = Paths.get(ROOT);
            //journal.log("Voltou para o diretório raiz");
        } else if (path.equals("..")) {
            if (!currentPath.equals(Paths.get(ROOT))) {
                currentPath = currentPath.getParent();
                //journal.log("Voltou para o diretório anterior: " + currentPath);
            } else {
                System.out.println("Já está no diretório raiz.");
            }
        } else {
            Path newPath = currentPath.resolve(path).normalize();
            if (Files.exists(newPath) && Files.isDirectory(newPath)) {
                currentPath = newPath;
                //journal.log("Entrou no diretório: " + newPath);
            } else {
                throw new FileNotFoundException("Diretório não encontrado: " + path);
            }
        }
    }
}


