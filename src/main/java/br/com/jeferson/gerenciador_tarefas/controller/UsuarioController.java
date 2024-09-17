package br.com.jeferson.gerenciador_tarefas.controller;

import br.com.jeferson.gerenciador_tarefas.controller.dto.CreateUserDto;
import br.com.jeferson.gerenciador_tarefas.entity.Role;
import br.com.jeferson.gerenciador_tarefas.entity.Usuario;
import br.com.jeferson.gerenciador_tarefas.repository.RoleRepository;
import br.com.jeferson.gerenciador_tarefas.repository.UsuarioRepository;
import jakarta.persistence.PostUpdate;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;


@RestController
public class UsuarioController {

    private final UsuarioRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsuarioController(UsuarioRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    @PostMapping("/users")
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto) {
        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());
        var userFromDb = userRepository.findByUsername(dto.username());

        if (basicRole == null) {
            System.out.println("Role basic não encontrado");
            basicRole = new Role();
            basicRole.setName(Role.Values.BASIC.name());
            roleRepository.save(basicRole);
        }

        Role finalRole = basicRole;
        userFromDb.ifPresentOrElse(
                user -> {
                    System.out.println("Usuário já existe!");
                },
                () -> {

                    var user = new Usuario();
                    user.setUsername(dto.username());
                    user.setPassword(bCryptPasswordEncoder.encode(dto.password()));
                    user.setEmail(dto.email());
                    user.setRoles(Set.of(finalRole));

                    userRepository.save(user);
                }
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<Usuario>> listUsers() {
        var users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @Transactional
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_BASIC')")
    public ResponseEntity<Usuario> atualizaUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
        return userRepository.findById(id)
                .map(usuarioExistente -> {
                    if (usuarioAtualizado.getUsername() != null) {
                        usuarioExistente.setUsername(usuarioAtualizado.getUsername());
                    }
                    if (usuarioAtualizado.getPassword() != null) {
                        // Criptografe a senha antes de salvar
                        usuarioExistente.setPassword(bCryptPasswordEncoder.encode(usuarioAtualizado.getPassword()));
                    }
                    if (usuarioAtualizado.getEmail() != null) {
                        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
                    }
                    userRepository.save(usuarioExistente);
                    return ResponseEntity.ok(usuarioExistente);
                }).orElseGet(() -> ResponseEntity.notFound().build());  // Retorna 404 se o usuário não for encontrado
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
        return ResponseEntity.ok().build();
    }
}
