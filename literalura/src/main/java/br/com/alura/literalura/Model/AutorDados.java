package br.com.alura.literalura.Model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AutorLivros(
        @JsonAlias("name") String nome,
        @JsonAlias("birth_year") int dataNascimento,
        @JsonAlias("death_year") int dataFalecimento
) {

}
