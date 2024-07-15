package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.Autor;
import br.com.alura.literalura.model.DadosAutor;
import br.com.alura.literalura.model.DadosLivro;
import br.com.alura.literalura.model.Livro;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.repository.LivroRepository;
import br.com.alura.literalura.services.ConsumoAPI;
import br.com.alura.literalura.services.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner scaner = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private LivroRepository repositoryLivro;
    private AutorRepository repositoryAutor;
    private ConverteDados ConverteDados = new ConverteDados();
    private List<Livro> livros = new ArrayList<>();
    private final String ENDERECO = "https://gutendex.com/books?search=";

    public Principal(LivroRepository repositoryLivro, AutorRepository repositoryAutor) {
        this.repositoryLivro = repositoryLivro;
        this.repositoryAutor = repositoryAutor;
    }

    public void menu() {
        var selecaoMenu = -1;

        while (selecaoMenu != 0) {
            System.out.print("""
                    ***************************************
                    1) Buscar livro por titulo.
                    2) Listar livros registrados.
                    3) Listar autores registrados.
                    4) Listar autores em atividade por ano.
                    5) Listar livros por idioma.
                    6) Buscar autor por nome
                    7) 10 livros mais baixados
                    0) Sair
                    ***************************************
                    """);
            selecaoMenu = scaner.nextInt();
            scaner.nextLine();

            switch (selecaoMenu) {
                case 1:
                    buscarLivrosPorTitulo();
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLivrosPorIdioma();
                    break;
                case 6:
                    buscarAutorPorNome();
                    break;
                case 7:
                    topLivrosBaixados();
                    break;
                case 0:
                    System.out.println("Encerrando operação...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente");
            }
        }
    }

    private void buscarLivrosPorTitulo() {
        System.out.println("Digite o nome do livro para busca: ");
        var buscaLivro = scaner.nextLine();
        var dados = consumo.obterDados(ENDERECO + buscaLivro.replace(" ", "%20"));
        registrarBusca(dados);
    }

    private void registrarBusca(String dados) {
        try {
            Livro livro = new Livro(ConverteDados.obterDados(dados, DadosLivro.class));
            Autor autor = new Autor(ConverteDados.obterDados(dados, DadosAutor.class));
            Autor bancoAutor = null;
            Livro bancoLivro = null;
            if (!repositoryAutor.existsByNome(autor.getNome())) {
                repositoryAutor.save(autor);
                bancoAutor = autor;
            } else {
                bancoAutor = repositoryAutor.findByNome(autor.getNome());
            }
            if (!repositoryLivro.existsByTitulo(livro.getTitulo())) {
                livro.setAutor(bancoAutor);
                repositoryLivro.save(livro);
                bancoLivro = livro;
            } else {
                bancoLivro = repositoryLivro.findByTitulo(livro.getTitulo());
            }
            System.out.println(bancoLivro);
        } catch (NullPointerException e) {
            System.out.println("Livro não encontrado.");
        }
    }

    private void listarLivrosRegistrados() {
        var buscarNoBanco = repositoryLivro.findAll();
        if (!buscarNoBanco.isEmpty()) {
            System.out.println("\nLivros cadastrados: ");
            buscarNoBanco.forEach(System.out::println);
        } else {
            System.out.println("\nNenhum livro encontrado.");
        }
    }

    private void listarAutoresRegistrados() {
        var buscarNoBanco = repositoryAutor.findAll();
        if (!buscarNoBanco.isEmpty()) {
            System.out.println("\nAutores cadastrados: ");
            buscarNoBanco.forEach(System.out::println);
        } else {
            System.out.println("\nNenhum autor encontrado.");
        }
    }

    private void listarAutoresVivos() {
        System.out.println("\nDigite o ano para pesquisa: \n");
        var ano = scaner.nextInt();
        scaner.nextLine();
        var buscarAutoresNoBanco = repositoryAutor.buscarPorAnoMorte(ano);
        if (!buscarAutoresNoBanco.isEmpty()) {
            System.out.println("\nAutores vivos no ano de " + ano + ": ");
            buscarAutoresNoBanco.forEach(System.out::println);
        } else {
            System.out.println("\nNenhum autor encontrado.");
        }
    }

    private void listarLivrosPorIdioma() {
        var idiomasNoBanco = repositoryLivro.buscarIdiomas();
        System.out.println("\nIdiomas cadastrados: ");
        idiomasNoBanco.forEach(System.out::println);
        System.out.println("\nDigite um dos idiomas para pesquisa: \n");
        var idiomaPesquisa = scaner.nextLine();
        repositoryLivro.buscarPorIdiomas(idiomaPesquisa).forEach(System.out::println);
    }

    private void buscarAutorPorNome() {
        System.out.println("\nDigite um autor pra busca: \n");
        var autorPesquisa = scaner.nextLine();
        var autor = repositoryAutor.encontrarPorNome(autorPesquisa);
        if (!autor.isEmpty()) {
            autor.forEach(System.out::println);
        } else {
            System.out.println("\nAutor não encontrado.");
        }
    }

    private void topLivrosBaixados() {
        var top10 = repositoryLivro.findTop10ByOrderByNumeroDownloadsDesc();
        top10.forEach(System.out::println);
    }
}
