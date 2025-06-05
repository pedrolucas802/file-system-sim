import java.io.*;
import java.util.*;

public class FileSystemSimulator {
    private static final String FS_FILE = "filesystem.dat";
    private static final String LOG_FILE = "journal.log";
    private final Journal journal = new Journal(LOG_FILE);

    protected VirtualDirectoryNode root;
    protected VirtualDirectoryNode current;

    public FileSystemSimulator() {
        loadFromDisk();
    }

    public void handleCommand(String input) throws IOException {
        String[] cmd = input.trim().split(" ", 2);
        String action = cmd[0];
        String params = cmd.length > 1 ? cmd[1] : "";

        switch (action) {
            case "mkdir" -> mkdir(params);
            case "touch" -> touch(params);
            case "ls" -> ls();
            case "cd" -> cd(params);
            case "pwd" -> System.out.println(current.getPath());
            case "rm" -> rm(params);
            case "rmdir" -> rmdir(params);
            case "rename" -> rename(params);
            case "log" -> journal.printLog();
            case "save" -> saveToDisk();
            case "exit" -> {
                saveToDisk();
                journal.log("exit");
                System.out.println("Sistema finalizado.");
                System.exit(0);
            }
            default -> System.out.println("Comando não reconhecido");
        }
    }

    private void mkdir(String path) {
        path = cleanPath(path);
        if (path.isEmpty()) {
            System.out.println("Nome inválido.");
            return;
        }

        String name = extractName(path);
        VirtualDirectoryNode parent = resolveParentDirectory(path);

        if (parent == null) {
            System.out.println("Diretório pai não encontrado.");
            return;
        }

        if (parent.hasChild(name)) {
            System.out.println("Diretório já existe.");
            return;
        }

        VirtualDirectoryNode dir = new VirtualDirectoryNode(name, parent.getPath() + "/" + name);
        parent.addChild(dir);
        journal.log("mkdir " + dir.getPath());
        saveToDisk();
    }

    private void touch(String path) {
        path = cleanPath(path);
        if (path.isEmpty()) {
            System.out.println("Nome inválido.");
            return;
        }

        String name = extractName(path);
        VirtualDirectoryNode parent = resolveParentDirectory(path);

        if (parent == null) {
            System.out.println("Diretório pai não encontrado.");
            return;
        }

        if (parent.hasChild(name)) {
            System.out.println("Arquivo já existe.");
            return;
        }

        VirtualFileNode file = new VirtualFileNode(name, parent.getPath() + "/" + name);
        parent.addChild(file);
        journal.log("touch " + file.getPath());
        saveToDisk();
    }

    private void ls() {
        current.getChildren().forEach(n -> {
            if (n instanceof VirtualDirectoryNode) {
                System.out.println("[DIR]  " + n.getName());
            } else {
                System.out.println("[FILE] " + n.getName());
            }
        });
    }

    private void cd(String path) {
        if (path.equals("..")) {
            if (!current.getPath().equals("/")) {
                String[] parts = current.getPath().split("/");
                String parentPath = String.join("/", Arrays.copyOf(parts, parts.length - 1));
                current = findDirectory(root, parentPath.equals("") ? "/" : parentPath);
            }
            return;
        }

        VirtualNode node = current.getChild(path);
        if (node instanceof VirtualDirectoryNode dir) {
            current = dir;
        } else {
            System.out.println("Diretório não encontrado.");
        }
    }

    private void rm(String path) {
        path = cleanPath(path);
        String name = extractName(path);
        VirtualDirectoryNode parent = resolveParentDirectory(path);

        if (parent != null && parent.getChild(name) instanceof VirtualFileNode) {
            parent.removeChild(name);
            journal.log("rm " + parent.getPath() + "/" + name);
            saveToDisk();
        } else {
            System.out.println("Arquivo não encontrado.");
        }
    }

    private void rmdir(String path) {
        path = cleanPath(path);
        String name = extractName(path);
        VirtualDirectoryNode parent = resolveParentDirectory(path);

        if (parent != null && parent.getChild(name) instanceof VirtualDirectoryNode) {
            parent.removeChild(name);
            journal.log("rmdir " + parent.getPath() + "/" + name);
            saveToDisk();
        } else {
            System.out.println("Diretório não encontrado.");
        }
    }

    private void rename(String params) {
        String[] parts = params.split(" ");
        if (parts.length != 2) {
            System.out.println("Uso: rename <orig> <novo>");
            return;
        }

        String oldName = cleanPath(parts[0]);
        String newName = cleanPath(parts[1]);

        String nameOld = extractName(oldName);
        String nameNew = extractName(newName);

        VirtualDirectoryNode parentOld = resolveParentDirectory(oldName);

        if (parentOld == null || !parentOld.hasChild(nameOld)) {
            System.out.println("Arquivo ou diretório original não encontrado.");
            return;
        }

        VirtualNode node = parentOld.getChild(nameOld);
        parentOld.removeChild(nameOld);
        node.name = nameNew;
        node.path = parentOld.getPath() + "/" + nameNew;
        parentOld.addChild(node);
        journal.log("rename " + nameOld + " " + nameNew);
        saveToDisk();
    }

    private void saveToDisk() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FS_FILE))) {
            out.writeObject(root);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o sistema de arquivos: " + e.getMessage());
        }
    }

    private void loadFromDisk() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FS_FILE))) {
            root = (VirtualDirectoryNode) in.readObject();
            current = root;
        } catch (IOException | ClassNotFoundException e) {
            root = new VirtualDirectoryNode("/", "/");
            current = root;
        }
    }

    private String cleanPath(String raw) {
        return raw.replaceAll("/{2,}", "/").replaceAll("/$", "").replaceAll("^/", "");
    }

    private String extractName(String fullPath) {
        String[] parts = fullPath.split("/");
        return parts[parts.length - 1];
    }

    private VirtualDirectoryNode resolveParentDirectory(String fullPath) {
        String[] parts = cleanPath(fullPath).split("/");
        if (parts.length <= 1) return current;

        VirtualDirectoryNode dir = fullPath.startsWith("/") ? root : current;

        for (int i = 0; i < parts.length - 1; i++) {
            VirtualNode next = dir.getChild(parts[i]);
            if (next instanceof VirtualDirectoryNode) {
                dir = (VirtualDirectoryNode) next;
            } else {
                return null;
            }
        }

        return dir;
    }

    private VirtualDirectoryNode findDirectory(VirtualDirectoryNode node, String path) {
        if (node.getPath().equals(path)) return node;
        for (VirtualNode child : node.getChildren()) {
            if (child instanceof VirtualDirectoryNode dir) {
                VirtualDirectoryNode result = findDirectory(dir, path);
                if (result != null) return result;
            }
        }
        return root;
    }
}