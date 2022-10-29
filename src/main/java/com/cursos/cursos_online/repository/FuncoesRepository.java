package com.cursos.cursos_online.repository;

import com.cursos.cursos_online.domain.Funcoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FuncoesRepository extends JpaRepository<Funcoes, Integer> {


    @Query(value="SELECT * FROM funcoes WHERE nome = ?1", nativeQuery = true)
    Funcoes findByNome(@Param("nome") String nome);
}
