package com.cursos.cursos_online.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="funcoes")
public class Funcoes {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    private String nome;

    @Getter
    @OneToMany
    private List<Usuarios> usuarios;

    public String toString() {
        return nome;
    }

    public Funcoes() {
    }

    public Funcoes(String nome) {
        this.nome = nome;
    }

    public Funcoes(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
