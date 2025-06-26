// src/main/java/oracle/com_alura_literalura/LiteraluraApplication.java
package oracle.com_alura_literalura;

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
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Escolha o número da sua opção:");
            System.out.println("1 - Buscar Livro pelo título");
            System.out.println("2 - Listar Livros registrados");
            System.out.println("3 - Listar Autores registrados");
            System.out.println("4 - Listar Autores vivos em um determinado ano");
            System.out.println("5 - Listar Livros em um determinado idioma");
            System.out.println("0 - Sair");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpa o buffer

            switch (opcao) {
                case 1:
                    System.out.print("Digite o título do livro: ");
                    String titulo = scanner.nextLine();
                    catalogoService.buscarLivroPorTitulo(titulo);
                    break;

                case 2:
                    catalogoService.exibirTodosOsLivros();
                    break;

                case 3:
                    catalogoService.exibirTodosAutores();
                    break;

                case 4:
                    System.out.print("Digite o ano: ");
                    int ano = scanner.nextInt();
                    scanner.nextLine();
                    catalogoService.exibirAutoresVivosEm(ano);
                    break;

                case 5:
                    System.out.println("Idiomas disponíveis: es, en, fr, pt");
                    System.out.print("Digite o idioma: ");
                    String idioma = scanner.nextLine();
                    catalogoService.exibirLivrosPorIdioma(idioma);
                    break;

                case 0:
                    System.out.println("Saindo...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        }
    }
}