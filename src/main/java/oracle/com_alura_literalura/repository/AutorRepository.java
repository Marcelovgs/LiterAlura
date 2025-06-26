package oracle.com_alura_literalura.repository;

import oracle.com_alura_literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("""
        SELECT a 
          FROM Autor a
         WHERE a.anoNascimento <= :ano
           AND (a.anoFalecimento IS NULL OR a.anoFalecimento >= :ano)
    """)
    List<Autor> autoresVivosEm(@Param("ano") int ano);

    // já tinha este
    Optional<Autor> findByNomeIgnoreCase(String nome);
}