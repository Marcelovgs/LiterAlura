package oracle.com_alura_literalura;

import oracle.com_alura_literalura.controller.LivroController;
import oracle.com_alura_literalura.service.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    private final CatalogoService catalogoService;

    @Autowired
    public LiteraluraApplication(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Exibe o menu
            System.out.println("Escolha o número da sua opção:");
            System.out.println("1 - Buscar Livro pelo título");
            System.out.println("2 - Listar Livros registrados");
            System.out.println("3 - Listar Autores registrados");
            System.out.println("4 - Listar Autores vivos em um determinado ano");
            System.out.println("5 - Listar Livros em um determinado idioma");
            System.out.println("0 - Sair");

            // Lê a entrada do usuário
            int opcao = scanner.nextInt();
            scanner.nextLine();  // Limpa o buffer do scanner

            switch (opcao) {
                case 1:
                    // Buscar livro pelo título
                    System.out.print("Digite o título do livro: ");
                    String titulo = scanner.nextLine();
                    // Chama o método para buscar o livro
                    catalogoService.buscarLivroPorTitulo(titulo);
                    break;
                case 2:
                    // Listar livros registrados
                    catalogoService.listarLivros().forEach(livro -> System.out.println(livro.getTitulo()));
                    break;
                case 3:
                    // Listar autores registrados
                    catalogoService.listarAutores().forEach(autor -> System.out.println(autor.getNome()));
                    break;
                case 4:
                    // Listar autores vivos em um determinado ano
                    System.out.print("Digite o ano: ");
                    int ano = scanner.nextInt();
                    catalogoService.listarAutoresVivosEm(ano).forEach(autor -> System.out.println(autor.getNome()));
                    break;
                case 5:
                    // Listar livros em um determinado idioma
                    System.out.println("Escolha um idioma para realizar a busca:");
                    System.out.println("es - Espanhol");
                    System.out.println("en - Inglês");
                    System.out.println("fr - Francês");
                    System.out.println("pt - Português");

                    System.out.print("Digite o idioma: ");
                    String idioma = scanner.nextLine();
                    catalogoService.listarLivrosPorIdioma(idioma).forEach(livro -> System.out.println(livro.getTitulo()));
                    break;
                case 0:
                    // Sair
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        }
    }
}
