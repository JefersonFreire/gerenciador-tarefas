package br.com.jeferson.gerenciador_tarefas.config;

import br.com.jeferson.gerenciador_tarefas.entity.Role;
import br.com.jeferson.gerenciador_tarefas.entity.Usuario;
import br.com.jeferson.gerenciador_tarefas.repository.RoleRepository;
import br.com.jeferson.gerenciador_tarefas.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final UsuarioRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;

    public AdminUserConfig(UsuarioRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());
        var userAdmin = userRepository.findByUsername("admin");

        if(roleAdmin == null) {
            System.out.println("Role admin não encontrado");
            roleAdmin = new Role();
            roleAdmin.setName(Role.Values.ADMIN.name());
            roleRepository.save(roleAdmin);
        }


        Role finalRoleAdmin = roleAdmin;
        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("admin já existe");
                },
                () -> {
                    var user = new Usuario();
                    user.setUsername("admin");
                    user.setPassword(bCryptPasswordEncoder.encode("123"));
                    user.setEmail("admin@email.com");
                    user.setRoles(Set.of(finalRoleAdmin));
                    userRepository.save(user);
                }

        );

    }
}
