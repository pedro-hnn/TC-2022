package com.cursos.cursos_online.repository;

import com.cursos.cursos_online.domain.Cursos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursosRepository extends JpaRepository<Cursos,Long> {
}
