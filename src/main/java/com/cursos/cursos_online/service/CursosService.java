package com.cursos.cursos_online.service;

import com.cursos.cursos_online.domain.Cursos;
import com.cursos.cursos_online.repository.CursosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursosService {

    @Autowired
    private CursosRepository repo;

    public List<Cursos> listAll(){
        return repo.findAll();
    }

    public void save(Cursos c){
        repo.save(c);
    }

    public Cursos get(long id){
        return repo.findById(id).get();
    }

    public void delete(long id){
        repo.deleteById(id);
    }

}
