package com.cursos.cursos_online.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "aulas")
public class Aulas {


    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String titulo;

    @Getter
    @Setter
    private String descricao;

    @Getter
    @Setter
    private String path_video;


    @Getter
    @Setter
    @ManyToOne(optional=false)
    @JoinColumn(name="fk_curso",referencedColumnName="id")
    private Cursos fk_curso;

    public Aulas() {
    }

    public Aulas(Long id, String titulo, String descricao, String path_video, Cursos fk_curso) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.path_video = path_video;
        this.fk_curso = fk_curso;
    }
}
