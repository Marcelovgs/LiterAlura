Literalura
Um catálogo inteligente de livros e autores que explora a API pública Gutendex, armazena tudo em PostgreSQL e oferece um menu interativo no console para você navegar por títulos, autores, idiomas e muito mais.

📚 O que é
Literalura é uma aplicação CLI (Command-Line Interface) desenvolvida com Spring Boot que permite:
- Buscar livros na API Gutendex por título
- Persistir resultados localmente em um banco PostgreSQL
- Listar todos os livros ou autores já registrados
- Filtrar autores que estavam vivos em um determinado ano
- Exibir livros disponíveis em um idioma específico
Tudo isso com saídas de console formatadas de forma legível e completa.

⚙️ Tecnologias
- Java 17
- Spring Boot (starter-web, starter-data-jpa)
- Spring Data JPA — abstração de acesso a dados
- PostgreSQL — banco de dados relacional
- Lombok — redução de boilerplate (getters, setters, construtores)
- JUnit 5 / Spring Boot Test — testes automatizados
- Gutendex API — fonte de dados públicos de obras em domínio público

🚀 Como usar
Pré-requisitos
- Java 17+ instalado
- Maven 3.6+
- PostgreSQL rodando localmente (ou em container)

📖 Exemplos de uso
- Buscar um livro
Escolha 1 → digite Dom Casmurro → o sistema buscará na API, salvará no banco e exibirá:
---- LIVRO ----
Título: Dom Casmurro
Autor: Machado de Assis
Idioma: pt
Downloads: 1443
- Listar autores vivos em 1800
Escolha 4 → digite 1800 → exibe todos os autores com nascimento ≤1800 e falecimento ≥1800.
- Filtrar por idioma
Escolha 5 → digite pt, en, es ou fr → exibe todos os livros registrados naquele idioma.

✅ O que já funciona
- Integração com Gutendex
- CRUD de livros e autores via Spring Data JPA
- Menu completo de busca e listagem
- Filtros: idioma e ano de vida dos autores

🚧 Possíveis evoluções
- Transformar em REST API com endpoints e Swagger
- Implementar paginação e ordenação
- Adicionar cache (Redis, Caffeine)
- Criar uma interface web (React/Vue)
- Melhorar logging e tratamento global de erros

🤝 Contribuindo
- Fork este repositório
- Crie uma branch feature: git checkout -b feature/nova-funcionalidade
- Faça suas alterações e testes
- Abra um Pull Request descrevendo sua mudança

Autor
Marcelo – Porto Alegre, RS
Desenvolvedor Java e apaixonado por livros.

Obrigado por visitar o Literalura! Espero que te ajude a explorar clássicos em domínio público de um jeito simples e organizado. 🚀


