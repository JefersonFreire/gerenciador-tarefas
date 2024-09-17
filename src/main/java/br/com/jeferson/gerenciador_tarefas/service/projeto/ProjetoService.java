package br.com.jeferson.gerenciador_tarefas.service.projeto;

import br.com.jeferson.gerenciador_tarefas.entity.Projeto;
import br.com.jeferson.gerenciador_tarefas.entity.Usuario;

import java.util.List;


public interface ProjetoService {
    List<Projeto> listarProjetos();
    void adicionarMembro(Projeto projeto, Usuario usuario);
    void removerMembro(Projeto projeto, Usuario usuario);
    void excluirProjeto(Long id);
}
