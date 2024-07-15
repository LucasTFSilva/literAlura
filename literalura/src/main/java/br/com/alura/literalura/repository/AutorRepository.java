package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    boolean existsByNome(String nome);

    Autor findByNome(String nome);

    @Query("SELECT a FROM Autor a WHERE a.anoFalecimento >= :ano AND :ano >= a.anoNascimento")
    List<Autor> buscarPorAnoMorte(int ano);

    @Query("SELECT a FROM Autor a WHERE a.nome ILIKE %:autorPesquisa%")
    List<Autor> encontrarPorNome(String autorPesquisa);
}
