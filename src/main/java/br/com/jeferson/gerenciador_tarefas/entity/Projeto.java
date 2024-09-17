package br.com.jeferson.gerenciador_tarefas.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "tb_projetos")
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "projeto_id")
    private Long id;

    @Column(nullable = false)
    private String nome;
    private String descricao;
    private String dataInicio = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    private String dataFim = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

 //   private List<Tarefa> tarefas;
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "projeto_id")
    private List<Usuario> membros = new ArrayList<>();
}
