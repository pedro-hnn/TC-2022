package com.cursos.cursos_online.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="usuario_curso")
public class UsuarioCurso {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private Boolean bool_libera_certificado;

    @Getter
    @Setter
    private Integer porcentagem_aulas_assistidas;

    @Getter
    @Setter
    private Integer quantidade_aulas;

    @Getter
    @Setter
    private String lista_aulas_assistidas;

    @Getter
    @Setter
    @ManyToOne(optional=false)
    @JoinColumn(name="fk_curso",referencedColumnName="id")
    private Cursos fk_curso;

    @Getter
    @Setter
    @ManyToOne(optional=false)
    @JoinColumn(name="fk_usuario",referencedColumnName="id")
    private Usuarios fk_usuario;

    public UsuarioCurso() {
    }

    public UsuarioCurso(Long id, Boolean bool_libera_certificado, Integer porcentagem_aulas_assistidas, Integer quantidade_aulas, String lista_aulas_assistidas, Cursos fk_curso, Usuarios fk_usuario) {
        this.id = id;
        this.bool_libera_certificado = bool_libera_certificado;
        this.porcentagem_aulas_assistidas = porcentagem_aulas_assistidas;
        this.quantidade_aulas = quantidade_aulas;
        this.lista_aulas_assistidas = lista_aulas_assistidas;
        this.fk_curso = fk_curso;
        this.fk_usuario = fk_usuario;
    }

    public UsuarioCurso(Boolean bool_libera_certificado, Integer porcentagem_aulas_assistidas, Integer quantidade_aulas, String lista_aulas_assistidas, Cursos fk_curso, Usuarios fk_usuario) {
        this.bool_libera_certificado = bool_libera_certificado;
        this.porcentagem_aulas_assistidas = porcentagem_aulas_assistidas;
        this.quantidade_aulas = quantidade_aulas;
        this.lista_aulas_assistidas = lista_aulas_assistidas;
        this.fk_curso = fk_curso;
        this.fk_usuario = fk_usuario;
    }
}
