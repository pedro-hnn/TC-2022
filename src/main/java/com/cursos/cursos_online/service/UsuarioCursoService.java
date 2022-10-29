package com.cursos.cursos_online.service;

import com.cursos.cursos_online.domain.UsuarioCurso;
import com.cursos.cursos_online.repository.UsuarioCursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioCursoService {

    @Autowired
    private UsuarioCursoRepository repo;

    public List<UsuarioCurso> listAll(){
        return repo.findAll();
    }

    public void save(UsuarioCurso c){
        repo.save(c);
    }

    public UsuarioCurso get(long id){
        return repo.findById(id).get();
    }

    public void delete(long id){
        repo.deleteById(id);
    }

    public UsuarioCurso findByUsuarioEcurso(Long user_id, Long curso_id){return repo.findByUsuarioEcurso(user_id,curso_id);}

    public List<UsuarioCurso> findByUsuario(Long user_id){return repo.findByUsuario(user_id);}


}
