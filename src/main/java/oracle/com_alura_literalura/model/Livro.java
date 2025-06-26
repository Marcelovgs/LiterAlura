package oracle.com_alura_literalura.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Livro {

    @Id
    @EqualsAndHashCode.Include
    private Long idGutendex;

    private String titulo;
    private String idioma;
    private Integer downloads;

    /**
     * Quando você faz livro.setAutores(...), trocamos toda a instância
     * de autores, evitando ConcurrentModificationException
     */
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "autores_livros",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores = new HashSet<>();

    /**
     * Sobrescreve o conjunto inteiro de autores.
     * Evita mexer na coleção original do Hibernate
     */
    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }
}