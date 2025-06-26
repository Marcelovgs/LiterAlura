package oracle.com_alura_literalura.dto;

import java.util.List;

public record GutendexBook(
        Long id,
        String title,
        List<String> languages,
        Integer download_count,
        List<GutendexAuthor> authors) {}
