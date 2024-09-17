package br.com.jeferson.gerenciador_tarefas.service.projeto;

import br.com.jeferson.gerenciador_tarefas.entity.Projeto;
import br.com.jeferson.gerenciador_tarefas.entity.Usuario;
import br.com.jeferson.gerenciador_tarefas.repository.ProjetoRepository;
import br.com.jeferson.gerenciador_tarefas.repository.UsuarioRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class ProjetoServiceImpl implements ProjetoService{

    private final ProjetoRepository projetoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProjetoServiceImpl(ProjetoRepository repository, UsuarioRepository usuarioRepository) {
        this.projetoRepository = repository;
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    public List<Projeto> listarProjetos() {
        listarProjetos().forEach(System.out::println);
        return listarProjetos();
    }

    @Override
    public void adicionarMembro(Projeto projeto, Usuario usuario) {
        Optional<Projeto> projetoExistente = projetoRepository.findById(projeto.getId());
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getId());

        if(projetoExistente.isPresent()){
            if(usuarioExistente.isPresent()){
                projeto.getMembros().add(usuarioExistente.get());
                projetoRepository.save(projeto);
            }
        }
    }

    @Override
    public void removerMembro(Projeto projeto, Usuario usuario) {
        if(projeto.getMembros().contains(usuario)){
                projeto.getMembros().remove(usuario);
                usuario.getProjetos().remove(projeto);
                usuarioRepository.save(usuario);
                projetoRepository.save(projeto);
        }
    }

    @Override
    public void excluirProjeto(Long id) {
        if(projetoRepository.existsById(id)){
            projetoRepository.deleteById(id);
        }
    }

}
