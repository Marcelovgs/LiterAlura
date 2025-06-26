// src/main/java/oracle/com_alura_literalura/service/CatalogoService.java
package oracle.com_alura_literalura.service;

import oracle.com_alura_literalura.model.Livro;
import oracle.com_alura_literalura.model.Autor;
import oracle.com_alura_literalura.repository.LivroRepository;
import oracle.com_alura_literalura.repository.AutorRepository;
import oracle.com_alura_literalura.dto.GutendexAuthor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CatalogoService {

    private final GutendexService gutendexService;
    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    public CatalogoService(GutendexService gutendexService,
                           LivroRepository livroRepository,
                           AutorRepository autorRepository) {
        this.gutendexService = gutendexService;
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    /**
     * Busca um livro na API Gutendex, salva/atualiza no banco e exibe no console.
     */
    public Livro buscarLivroPorTitulo(String titulo) {
        var livroApi = gutendexService
                .buscarPrimeiroLivroPorTitulo(titulo)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado na API"));

        Optional<Livro> optLivro = livroRepository.findById(livroApi.id());
        Livro livro;

        if (optLivro.isPresent()) {
            livro = optLivro.get();
            // atualiza campos
            livro.setTitulo(livroApi.title());
            livro.setIdioma(livroApi.languages().stream().findFirst().orElse("desconhecido"));
            livro.setDownloads(livroApi.download_count());
            // substitui a coleção para evitar C.M.E
            livro.setAutores(new HashSet<>());
        } else {
            livro = new Livro();
            livro.setIdGutendex(livroApi.id());
            livro.setTitulo(livroApi.title());
            livro.setIdioma(livroApi.languages().stream().findFirst().orElse("desconhecido"));
            livro.setDownloads(livroApi.download_count());
            livro.setAutores(new HashSet<>());
        }

        // processa autores
        Set<Autor> autores = new HashSet<>();
        for (GutendexAuthor aApi : livroApi.authors()) {
            Autor autor = autorRepository
                    .findByNomeIgnoreCase(aApi.name())
                    .orElseGet(() -> {
                        Autor novo = new Autor();
                        novo.setNome(aApi.name());
                        novo.setAnoNascimento(aApi.birth_year());
                        novo.setAnoFalecimento(aApi.death_year());
                        return novo;
                    });
            autores.add(autor);
        }

        // salva/atualiza autores e associa ao livro
        autores = new HashSet<>(autorRepository.saveAll(autores));
        livro.getAutores().addAll(autores);

        // persiste livro
        Livro salvo = livroRepository.save(livro);

        // exibe no console
        exibirDadosDoLivro(salvo);
        return salvo;
    }

    /** Exibe dados de um único livro no console */
    public void exibirDadosDoLivro(Livro livro) {
        System.out.println("---- LIVRO ----");
        System.out.println("Título: " + livro.getTitulo());
        livro.getAutores().forEach(a -> System.out.println("Autor: " + a.getNome()));
        System.out.println("Idioma: " + livro.getIdioma());
        System.out.println("Downloads: " + livro.getDownloads());
        System.out.println("----------------");
    }

    /** Lista e exibe todos os livros registrados */
    public void exibirTodosOsLivros() {
        listarLivros().forEach(this::exibirDadosDoLivro);
    }

    /** Exibe dados de um único autor no console */
    public void exibirDadosDoAutor(Autor autor) {
        System.out.println("---- AUTOR ----");
        System.out.println("Autor: " + autor.getNome());
        System.out.println("Ano de nascimento: " + autor.getAnoNascimento());
        System.out.println("Ano de falecimento: " + autor.getAnoFalecimento());
        List<String> titulos = autor.getLivros()
                .stream()
                .map(Livro::getTitulo)
                .collect(Collectors.toList());
        System.out.println("Livros: " + titulos);
        System.out.println("----------------");
    }

    /** Lista e exibe todos os autores registrados */
    public void exibirTodosAutores() {
        listarAutores().forEach(this::exibirDadosDoAutor);
    }

    public List<Livro> listarLivros() {
        return livroRepository.findAll();
    }

    public List<Autor> listarAutores() {
        return autorRepository.findAll();
    }

    public List<Autor> listarAutoresVivosEm(int ano) {
        return autorRepository.autoresVivosEm(ano);
    }

    public List<Livro> listarLivrosPorIdioma(String idioma) {
        return livroRepository.findByIdiomaIgnoreCase(idioma);
    }
}