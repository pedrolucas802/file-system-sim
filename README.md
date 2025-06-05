
# Simulador de Sistema de Arquivos com Journaling (Projeto disciplina de SO)

## ✍️ Metodologia

O simulador foi desenvolvido utilizando a linguagem de programação **Java**, com foco em chamadas de métodos que simulam comandos de um sistema operacional. As funcionalidades básicas — como criar, apagar, renomear e listar arquivos e diretórios — foram implementadas de forma modular, utilizando orientação a objetos.

O programa funciona em modo **shell interativo**, permitindo que o usuário insira comandos diretamente, veja os resultados no terminal e acompanhe o registro de ações por meio de um log (`journal.log`), que é reiniciado a cada execução.

---

## 📚 Parte 1: Introdução ao Sistema de Arquivos com Journaling

### O que é um Sistema de Arquivos?

Um sistema de arquivos é a estrutura lógica que permite que dados sejam armazenados e organizados em dispositivos como HDs e SSDs. Ele fornece uma hierarquia de diretórios e arquivos, controla permissões, localização e integridade dos dados.

### O que é Journaling?

**Journaling** é uma técnica que visa preservar a integridade dos dados no caso de falhas, como quedas de energia ou interrupções inesperadas. Antes de qualquer alteração no sistema de arquivos, a operação é registrada em um arquivo de log (journal).

Se algo falhar, o sistema pode usar esse log para restaurar ou desfazer operações incompletas, garantindo um estado consistente.

#### Tipos de Journaling:

- **Write-Ahead Logging (WAL):** a operação é registrada antes de ser executada.
- **Log-Structured File System (LFS):** o sistema inteiro é tratado como um log sequencial.
- **Metadata Journaling:** registra apenas alterações nos metadados dos arquivos.

---

## 🏗️ Parte 2: Arquitetura do Simulador

### Estrutura de Dados

O simulador é composto pelas seguintes classes:

- `Main`: ponto de entrada, gerencia o loop de comandos.
- `old.FileSystemSimulator_old`: responsável por interpretar e executar comandos.
- `old.VirtualFile`: representa um arquivo, com métodos como `create()` e `delete()`.
- `old.VirtualDirectory`: representa um diretório (estrutura básica).
- `Journal`: gerencia o registro de logs (`journal.log`).

A classe `old.FileSystemSimulator_old` mantém o estado do diretório atual e usa `Path` para manipular arquivos e diretórios. Cada comando executado gera uma entrada no log.

### Journaling

- Toda operação relevante (como criar, apagar ou renomear arquivos e diretórios) é registrada no `journal.log`.

[//]: # (- Ao iniciar o sistema, se um `journal.log` antigo existir, ele é automaticamente **renomeado** para `journal_<timestamp>.log`.)

[//]: # (- Um novo `journal.log` é iniciado com a marcação de "Sistema iniciado".)

---

## 💻 Parte 3: Implementação em Java

### 📁 Classe `old.FileSystemSimulator_old`

- Gerencia os comandos como `mkdir`, `rmdir`, `cd`, `ls`, `cp`, `rename`, etc.
- Controla o estado atual (`currentPath`) e executa as operações com base em `java.nio.file`.

### 📄 Classe `old.VirtualFile`

- Representa arquivos do sistema.
- Métodos:
  - `create()`: cria o arquivo e seu diretório pai, se necessário.
  - `delete()`: remove o arquivo com segurança.

### 📁 Classe `old.VirtualDirectory`

- Representa um diretório.
- Utilizada para encapsular `Path`, mantendo coesão da arquitetura.

### 📘 Classe `Journal`

- Gerencia o arquivo de log `journal.log`.
- Registra as ações com timestamps.
- Exibe o log completo com o comando `log`.

---

## ⚙️ Parte 4: Instalação e Funcionamento

### ✅ Requisitos

- Java JDK 17 ou superior
- IntelliJ IDEA, VS Code ou outro editor compatível

### 🚀 Como executar

1. Clone o projeto:

```bash
git clone https://github.com/pedrolucas802/file-system-sim.git
cd file-system-simulator/src
```

2. Compile os arquivos:

```bash
javac Main.java old.FileSystemSimulator_old.java Journal.java old.VirtualFile.java old.VirtualDirectory.java
```

3. Execute o simulador:

```bash
java Main
```

4. Use comandos como:

```bash
mkdir projetos
cd projetos
touch arquivo.txt
cp arquivo.txt copia.txt
ls
log
exit
```

---

## 🎯 Resultados Esperados

O simulador permite:

- Compreensão prática do funcionamento de sistemas de arquivos e hierarquias.
- Visualização do efeito de cada comando em tempo real.
- Entendimento do papel do journaling na preservação da integridade do sistema.
- Auditoria de operações via `journal.log`, reiniciado automaticamente a cada execução.

---

## 🔗 Link para o Projeto

[GitHub - Link](https://github.com/pedrolucas802/file-system-sim)

---

## 👥 Autor

- Pedro Lucas Sousa Barreto – 2220318 – Turma: GR-R-251-T303-26
