package br.com.alura.literalura.Model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivro(
        @JsonAlias("title") String titles,
        @JsonAlias("authors")List<DadosAutor> authors,
        @JsonAlias("languages")List<String> languages,
        @JsonAlias("download_count") int downloads
) {
}
