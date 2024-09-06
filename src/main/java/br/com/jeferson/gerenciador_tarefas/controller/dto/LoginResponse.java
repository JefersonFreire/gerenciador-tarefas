package br.com.jeferson.gerenciador_tarefas.controller.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
