package oracle.com_alura_literalura.repository;

import oracle.com_alura_literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    // Método para buscar autor pelo nome, ignorando maiúsculas/minúsculas
    Optional<Autor> findByNomeIgnoreCase(String nome);

    // Método para listar autores vivos em determinado ano
    @Query("SELECT a FROM Autor a WHERE a.anoNascimento <= :ano AND (a.anoFalecimento IS NULL OR a.anoFalecimento >= :ano)")
    List<Autor> autoresVivosEm(int ano);
}
