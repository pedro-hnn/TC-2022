package com.cursos.cursos_online.repository;

import com.cursos.cursos_online.domain.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {

    @Query(value="SELECT * FROM usuarios WHERE email = ?1", nativeQuery = true)
    Usuarios findByEmail(@Param("email") String email);

}
