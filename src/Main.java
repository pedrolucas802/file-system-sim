import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileSystemSimulator simulator = null;

        try {
            simulator = new FileSystemSimulator();
            System.out.println("Simulador de Sistema de Arquivos com Journaling");
            simulator.printMenu();
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o simulador: " + e.getMessage());
            return;
        }

        boolean executando = true;
        while (executando) {
            System.out.print("fs:" + simulator.getPromptPath() + "> ");
            String input = scanner.nextLine();

            try {
                if ("exit".equalsIgnoreCase(input.trim())) {
                    simulator.handleCommand("exit");
                    executando = false;
                } else {
                    simulator.handleCommand(input);
                }
            } catch (Exception e) {
                System.err.println("Erro ao executar comando: " + e.getMessage());
            }
        }
    }
}