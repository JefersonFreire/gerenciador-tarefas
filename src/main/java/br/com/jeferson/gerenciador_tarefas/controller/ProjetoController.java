package br.com.jeferson.gerenciador_tarefas.controller;

import br.com.jeferson.gerenciador_tarefas.entity.Projeto;
import br.com.jeferson.gerenciador_tarefas.entity.Usuario;
import br.com.jeferson.gerenciador_tarefas.repository.ProjetoRepository;
import br.com.jeferson.gerenciador_tarefas.repository.UsuarioRepository;
import br.com.jeferson.gerenciador_tarefas.service.projeto.ProjetoService;
import br.com.jeferson.gerenciador_tarefas.service.projeto.ProjetoServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/projetos")
public class ProjetoController {


    private final ProjetoRepository projetoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProjetoServiceImpl projetoServiceImpl;

    public ProjetoController(ProjetoRepository projetoRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ProjetoService projetoService, UsuarioRepository usuarioRepository, ProjetoServiceImpl projetoServiceImpl) {
        this.projetoRepository = projetoRepository;
        this.usuarioRepository = usuarioRepository;
        this.projetoServiceImpl = projetoServiceImpl;
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    @PostMapping("/cadastro")
    public ResponseEntity<List<Projeto>> cadastrarProjeto(@RequestBody Projeto projeto) {
        Projeto novoProjeto = projetoRepository.save(projeto);
        return ResponseEntity.ok(List.of(novoProjeto));
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_BASIC')")
    @GetMapping("/users")
    public ResponseEntity<List<Projeto>> listarProjetos() {
        List<Projeto> projeto = projetoRepository.findAll();
        return ResponseEntity.ok(projeto);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    @PostMapping("/{projeto_id}/membros/{user_id}")
    public ResponseEntity<Projeto> adicionarMembros(@PathVariable Long projeto_id, @PathVariable Long user_id) {
        Projeto projeto = projetoRepository.findById(projeto_id).get();
        Usuario usuario = usuarioRepository.findById(user_id).get();
        projetoServiceImpl.adicionarMembro(projeto, usuario);
        projetoRepository.save(projeto);
        return ResponseEntity.ok(projeto);
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{projeto_id}/membros/{user_id}")
    public ResponseEntity<Void> removerMembro(@PathVariable Long projeto_id, @PathVariable Long user_id) {
        Projeto projeto = projetoRepository.findById(projeto_id).get();
        Usuario usuario = usuarioRepository.findById(user_id).get();
        projetoServiceImpl.removerMembro(projeto, usuario);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> excluirProjeto(@PathVariable Long id) {
        projetoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
