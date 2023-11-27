import java.io.File;
import java.util.LinkedList; 
import java.util.Queue;
import java.util.Stack;
import java.util.Scanner;

class TNo {
    static final int IDNAME = 30; // Tamanho máximo de um nome de diretório ou arquivo
    static final int PATH = 100; // Tamanho máximo de um caminho

    String id; // Nome do diretório ou arquivo
    String path; // Caminho a partir da raiz até o ID
    char tipo; // Se é Arquivo ‘a’ ou Diretório ‘d’
    TNo dir_ant; // Diretório anterior..
    LinkedList<TNo> filhos; // Lista de diretórios ou arquivos a partir do corrente

    public TNo(String id, String path, char tipo, TNo dir_ant) {
        this.id = id;
        this.path = path;
        this.tipo = tipo;
        this.dir_ant = dir_ant;
        this.filhos = new LinkedList<>();
    }
    
    public long getTamanho() {
    	//obtem o tamanho do arquivo
    	File file = new File(path);
    	return file.length();
    }
    
}

public class Shell {
    private TNo raiz; // Raiz da árvore de diretórios
    private TNo corrente; // Diretório corrente

    public Shell(String raizPath) {
        this.raiz = new TNo("raiz", raizPath, 'd', null);
        this.corrente = this.raiz;
    }

    // m - criar diretório
    public void criarDiretorio(String nome) {
    	//Cria diretorio no computador
    	File dir = new File(corrente.path + "/" + nome);
    	dir.mkdirs();
    	
    	//Add diretorio a arvore
        TNo novoDir = new TNo(nome, corrente.path + "/" + nome, 'd', corrente);
        corrente.filhos.add(novoDir);
    }
    
    
    
    // c - mudar diretório corrente
    public void mudarDiretorio(String nome) {
        for (TNo filho : corrente.filhos) {
            if (filho.tipo == 'd' && filho.id.equals(nome)) {
                corrente = filho;
                return;
            }
        }
        System.out.println("Diretorio nao encontrado: " + nome);
    }

    // d - remover um arquivo
    public void removerArquivo(String nome) {
        for (TNo filho : corrente.filhos) {
            if (filho.tipo == 'a' && filho.id.equals(nome)) {
                corrente.filhos.remove(filho);
                return;
            }
        }
        System.out.println("Arquivo nao encontrado: " + nome);
    }

    // p – Apresentar a árvore realizando um percurso em profundidade
    public void percursoProfundidade(TNo no) {
        System.out.println(no.path);
        for (TNo filho : no.filhos) {
            percursoProfundidade(filho);
        }
    }

    // l - Apresentar a árvore realizando um percurso em largura
    public void percursoLargura() {
        Queue<TNo> fila = new LinkedList<>();
        fila.add(raiz);

        while (!fila.isEmpty()) {
            TNo no = fila.poll();
            System.out.println(no.path + " (" + no.getTamanho() + " bytes)");

            fila.addAll(no.filhos);
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java ShellSimulator <raizPath>");
            System.exit(1);
        }
        

        Shell shell = new Shell(args[0]);
        Scanner scanner = new Scanner(System.in);
        
        while(true) {
        	System.out.println("\n--- Menu ---");
        	System.out.println("m - criar diretorio");
        	System.out.println("c - mudar diretorio corrente");
        	System.out.println("d - remover arquivo");
        	System.out.println("p - apresentar arvore em profundidade");
        	System.out.println("l - apresentar arvore em largura");
        	System.out.println("q - sair");
        	
        	System.out.print("Escolha a operacao: ");
        	String escolha = scanner.nextLine().trim();
        	
        	switch(escolha) {
        	case "m":
        		System.out.print("Digite o nome do diretorio a ser criado: ");
        		String nomeDir = scanner.nextLine().trim();
        		shell.criarDiretorio(nomeDir);
        		break;
        	case "c":
        		System.out.print("Digite o nome do diretorio para mudar: ");
        		String nomeMudarDir = scanner.nextLine().trim();
        		shell.mudarDiretorio(nomeMudarDir);
        		break;
        	case "d":
        		System.out.print("Digite o nome do arquivo a ser removido: ");
        		String nomeArqRemover = scanner.nextLine().trim();
        		shell.removerArquivo(nomeArqRemover);
        		break;
        	case "p":
        		System.out.print("Percurso em profundidade: \n");
        		shell.percursoProfundidade(shell.raiz);
        		break;
        	case "l":
        		System.out.print("Percurso em largura: \n");
        		shell.percursoLargura();
        		break;
        	case "q":
        		System.out.print("Encerrando o programa.");
        		System.exit(0);
        		break;
        	default: 
        		System.out.println("Escolha invalida. Tente novamente");
        	}
        }
    }
}
