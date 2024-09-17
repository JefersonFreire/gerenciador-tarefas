package br.com.jeferson.gerenciador_tarefas.repository;

import br.com.jeferson.gerenciador_tarefas.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}
