import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class ReadFileSystemDat {
    public static void main(String[] args) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("filesystem.dat"))) {
            VirtualDirectoryNode root = (VirtualDirectoryNode) in.readObject();
            System.out.println("Sistema de arquivos:");
            printStructure(root, 0);
        } catch (Exception e) {
            System.err.println("Erro ao ler filesystem.dat: " + e.getMessage());
        }
    }

    private static void printStructure(VirtualDirectoryNode dir, int depth) {
        String indent = "  ".repeat(depth);
        for (VirtualNode node : dir.getChildren()) {
            if (node instanceof VirtualDirectoryNode subDir) {
                System.out.println(indent + "[DIR]  " + subDir.getName());
                printStructure(subDir, depth + 1);
            } else {
                System.out.println(indent + "[FILE] " + node.getName());
            }
        }
    }
}