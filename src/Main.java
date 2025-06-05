import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileSystemSimulator simulator = new FileSystemSimulator();

        System.out.println("SO - Simulador de Sistema de Arquivos:");
        printMenu();

        while (true) {
            System.out.print("fs:" + simulator.current.getPath() + "> ");
            String input = scanner.nextLine();

            try {
                simulator.handleCommand(input);
            } catch (Exception e) {
                System.err.println("Erro ao executar comando: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nComandos disponíveis:");
        System.out.println("mkdir <dir>         - Criar diretório");
        System.out.println("touch <file>        - Criar arquivo");
        System.out.println("ls                  - Listar conteúdo do diretório atual");
        System.out.println("cd <dir>/..         - Navegar entre diretórios");
        System.out.println("pwd                 - Mostrar diretório atual");
        System.out.println("rm <file>           - Remover arquivo");
        System.out.println("rmdir <dir>         - Remover diretório");
        System.out.println("rename <a> <b>      - Renomear arquivo ou diretório");
        System.out.println("log                 - Exibir conteúdo do journal.log");
        System.out.println("save                - Forçar salvamento manual");
        System.out.println("exit                - Sair do simulador (salvando automaticamente)");
        System.out.println();
    }
}