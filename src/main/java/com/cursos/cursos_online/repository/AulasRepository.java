package com.cursos.cursos_online.repository;


import com.cursos.cursos_online.domain.Aulas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AulasRepository extends JpaRepository<Aulas,Long> {

    @Query(value="SELECT * FROM aulas WHERE fk_curso = ?1", nativeQuery = true)
    List<Aulas> findByCurso(@Param("curso_id") Long curso_id);

}


