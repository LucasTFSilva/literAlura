package br.com.alura.literalura.services;

public interface iConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
