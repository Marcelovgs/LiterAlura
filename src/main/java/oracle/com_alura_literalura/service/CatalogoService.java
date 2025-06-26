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

    public Livro buscarLivroPorTitulo(String titulo) {
        var livroApi = gutendexService
                .buscarPrimeiroLivroPorTitulo(titulo)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado na API"));

        // 1) Busco no banco por ID do Gutendex
        Optional<Livro> optLivro = livroRepository.findById(livroApi.id());
        Livro livro;

        if (optLivro.isPresent()) {
            livro = optLivro.get();
            // Atualiza sempre os campos (título, idioma, downloads)
            livro.setTitulo(livroApi.title());
            livro.setIdioma(livroApi.languages().stream().findFirst().orElse("desconhecido"));
            livro.setDownloads(livroApi.download_count());
            // Substitui a coleção inteira para evitar ConcurrentModificationException
            livro.setAutores(new HashSet<>());
        } else {
            livro = new Livro();
            livro.setIdGutendex(livroApi.id());
            livro.setTitulo(livroApi.title());
            livro.setIdioma(livroApi.languages().stream().findFirst().orElse("desconhecido"));
            livro.setDownloads(livroApi.download_count());
            // inicializa vazio
            livro.setAutores(new HashSet<>());
        }

        // 2) Processa autores da API
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

        // 3) Salva/atualiza autores e associa ao livro
        autores = new HashSet<>(autorRepository.saveAll(autores));
        livro.getAutores().addAll(autores);

        // 4) Persiste o livro atualizado
        Livro livroSalvo = livroRepository.save(livro);

        // Exibe no console, se desejar
        exibirDadosDoLivro(livroSalvo);

        return livroSalvo;
    }

    private void exibirDadosDoLivro(Livro livro) {
        System.out.println("---- LIVRO ----");
        System.out.println("Título: " + livro.getTitulo());
        livro.getAutores().forEach(a -> System.out.println("Autor: " + a.getNome()));
        System.out.println("Idioma: " + livro.getIdioma());
        System.out.println("Downloads: " + livro.getDownloads());
        System.out.println("----------------");
    }

    // demais métodos...
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