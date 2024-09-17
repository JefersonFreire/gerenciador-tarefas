package br.com.jeferson.gerenciador_tarefas.repository;

import br.com.jeferson.gerenciador_tarefas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    @Query("UPDATE Usuario u SET u.username = :username, u.password = :password, u.email = :email WHERE u.id = :id")
    void atualizaUsuario(@Param("id") Long id, @Param("username")String username, @Param("password")String password, @Param("email")String email);
}
