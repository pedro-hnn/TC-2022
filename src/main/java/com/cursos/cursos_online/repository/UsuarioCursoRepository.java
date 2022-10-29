package com.cursos.cursos_online.repository;

import com.cursos.cursos_online.domain.UsuarioCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioCursoRepository extends JpaRepository<UsuarioCurso,Long> {

    @Query(value="SELECT * FROM usuario_curso WHERE fk_usuario = ?1 AND fk_curso = ?2", nativeQuery = true)
    UsuarioCurso findByUsuarioEcurso(@Param("fk_usuario") Long user_id, @Param("fk_curso") Long curso_id );

    @Query(value="SELECT * FROM usuario_curso WHERE fk_usuario = ?1", nativeQuery = true)
    List<UsuarioCurso> findByUsuario(@Param("fk_usuario") Long user_id);

}
