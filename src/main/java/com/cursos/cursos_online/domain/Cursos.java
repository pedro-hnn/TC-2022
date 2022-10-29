package com.cursos.cursos_online.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cursos")
public class Cursos {

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
    private String path_capa;

    @Getter
    @Setter
    private String path_imagem_certificado;

    @Getter
    @Setter
    private String texto_certificado;

    @Getter
    @OneToMany
    private List<Aulas> aulas;

    @OneToMany
    private List<UsuarioCurso> usuarioCursos;

    public Cursos() {

    }

    public Cursos(Long id, String titulo, String descricao, String path_capa, String path_imagem_certificado, String texto_certificado) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.path_capa = path_capa;
        this.path_imagem_certificado = path_imagem_certificado;
        this.texto_certificado = texto_certificado;
    }

    public Cursos(Long id, String titulo, String descricao, String texto_certificado) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.texto_certificado = texto_certificado;
    }
}
