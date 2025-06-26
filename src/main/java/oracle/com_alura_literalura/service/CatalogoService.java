package oracle.com_alura_literalura.service;

import oracle.com_alura_literalura.model.Autor;
import oracle.com_alura_literalura.model.Livro;
import oracle.com_alura_literalura.repository.AutorRepository;
import oracle.com_alura_literalura.repository.LivroRepository;
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

    /** Busca um livro na API, salva/atualiza no banco e exibe no console */
    public Livro buscarLivroPorTitulo(String titulo) {
        var livroApi = gutendexService.buscarPrimeiroLivroPorTitulo(titulo)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado na API"));

        Optional<Livro> optLivro = livroRepository.findById(livroApi.id());
        Livro livro = optLivro.orElseGet(() -> {
            Livro novo = new Livro();
            novo.setIdGutendex(livroApi.id());
            return novo;
        });

        // atualiza campos
        livro.setTitulo(livroApi.title());
        livro.setIdioma(livroApi.languages().stream().findFirst().orElse("desconhecido"));
        livro.setDownloads(livroApi.download_count());
        livro.setAutores(new HashSet<>());

        // processa autores
        Set<Autor> autores = livroApi.authors()
                .stream()
                .map(aApi -> autorRepository
                        .findByNomeIgnoreCase(aApi.name())
                        .orElseGet(() -> {
                            Autor novo = new Autor();
                            novo.setNome(aApi.name());
                            novo.setAnoNascimento(aApi.birth_year());
                            novo.setAnoFalecimento(aApi.death_year());
                            return novo;
                        })
                ).collect(Collectors.toSet());

        autores = new HashSet<>(autorRepository.saveAll(autores));
        livro.getAutores().addAll(autores);

        Livro salvo = livroRepository.save(livro);
        exibirDadosDoLivro(salvo);
        return salvo;
    }

    /** Exibe os dados de um livro */
    public void exibirDadosDoLivro(Livro livro) {
        System.out.println("---- LIVRO ----");
        System.out.println("Título: " + livro.getTitulo());
        livro.getAutores().forEach(a -> System.out.println("Autor: " + a.getNome()));
        System.out.println("Idioma: " + livro.getIdioma());
        System.out.println("Downloads: " + livro.getDownloads());
        System.out.println("----------------");
    }

    /** Lista e exibe todos os livros */
    public void exibirTodosOsLivros() {
        listarLivros().forEach(this::exibirDadosDoLivro);
    }

    public List<Livro> listarLivros() {
        return livroRepository.findAll();
    }

    /** Lista autores cadastrados */
    public List<Autor> listarAutores() {
        return autorRepository.findAll();
    }

    /** Lista autores vivos em um determinado ano */
    public List<Autor> listarAutoresVivosEm(int ano) {
        return autorRepository.autoresVivosEm(ano);
    }

    /** Exibe os dados de um autor */
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

    /** Lista e exibe todos os autores */
    public void exibirTodosAutores() {
        listarAutores().forEach(this::exibirDadosDoAutor);
    }

    /** Exibe autores vivos em um ano específico */
    public void exibirAutoresVivosEm(int ano) {
        System.out.println("----- AUTORES VIVOS EM " + ano + " -----");
        listarAutoresVivosEm(ano).forEach(this::exibirDadosDoAutor);
    }

    /** Lista livros por idioma */
    public List<Livro> listarLivrosPorIdioma(String idioma) {
        return livroRepository.findByIdiomaIgnoreCase(idioma);
    }

    /** Exibe todos os livros de um determinado idioma */
    public void exibirLivrosPorIdioma(String idioma) {
        System.out.println("----- LIVROS EM IDIOMA: " + idioma + " Não existem livros neste idioma no banco de dados.");
        listarLivrosPorIdioma(idioma).forEach(this::exibirDadosDoLivro);
    }
}