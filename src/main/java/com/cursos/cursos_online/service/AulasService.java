package com.cursos.cursos_online.service;

import com.cursos.cursos_online.domain.Aulas;
import com.cursos.cursos_online.repository.AulasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AulasService {

    @Autowired
    private AulasRepository repo;

    public List<Aulas> listAll(){
        return repo.findAll();
    }

    public void save(Aulas a){
        repo.save(a);
    }

    public Aulas get(long id){
        return repo.findById(id).get();
    }

    public void delete(long id){
        repo.deleteById(id);
    }

    public List<Aulas> findByCurso(Long id_curso){
        return repo.findByCurso(id_curso);
    };
}
