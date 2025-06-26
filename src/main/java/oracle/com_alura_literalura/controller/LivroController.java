package oracle.com_alura_literalura.controller;

import oracle.com_alura_literalura.service.CatalogoService;
import oracle.com_alura_literalura.model.Livro;
import oracle.com_alura_literalura.model.Autor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final CatalogoService catalogoService;

    public LivroController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping("/buscar/{titulo}")
    public Livro buscarLivro(@PathVariable String titulo) {
        return catalogoService.buscarLivroPorTitulo(titulo);
    }

    @GetMapping("/listar")
    public List<Livro> listarLivros() {
        return catalogoService.listarLivros();
    }

    @GetMapping("/autores")
    public List<Autor> listarAutores() {
        return catalogoService.listarAutores();
    }

    @GetMapping("/autores/vivos/{ano}")
    public List<Autor> listarAutoresVivosEm(@PathVariable int ano) {
        return catalogoService.listarAutoresVivosEm(ano);
    }

    @GetMapping("/idioma/{idioma}")
    public List<Livro> listarLivrosPorIdioma(@PathVariable String idioma) {
        return catalogoService.listarLivrosPorIdioma(idioma);
    }
}
