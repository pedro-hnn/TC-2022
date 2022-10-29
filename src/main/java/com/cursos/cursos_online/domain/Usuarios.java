package com.cursos.cursos_online.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="usuarios")
public class Usuarios {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String senha;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    @ManyToOne(optional=false)
    @JoinColumn(name="fk_funcao",referencedColumnName="id")
    private Funcoes fk_funcao;

    @OneToMany
    private List<UsuarioCurso> usuarioCursos;

    public Usuarios() {
    }

    public Usuarios(Long id, String email, String senha, String username, Funcoes fk_funcao) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.username = username;
        this.fk_funcao = fk_funcao;
    }
}
