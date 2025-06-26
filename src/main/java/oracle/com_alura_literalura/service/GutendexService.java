package oracle.com_alura_literalura.service;

import oracle.com_alura_literalura.dto.GutendexResponse;
import oracle.com_alura_literalura.dto.GutendexBook;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GutendexService {

    private static final String API_URL = "https://gutendex.com/books?search={title}";
    private final RestTemplate rest = new RestTemplate();

    public Optional<GutendexBook> buscarPrimeiroLivroPorTitulo(String titulo) {
        var resp = rest.getForObject(API_URL, GutendexResponse.class, titulo);
        return resp != null && !resp.results().isEmpty()
                ? Optional.of(resp.results().get(0))
                : Optional.empty();
    }
}
