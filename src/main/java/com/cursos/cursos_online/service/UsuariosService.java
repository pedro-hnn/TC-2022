package com.cursos.cursos_online.service;

import com.cursos.cursos_online.domain.Funcoes;
import com.cursos.cursos_online.domain.Usuarios;
import com.cursos.cursos_online.repository.FuncoesRepository;
import com.cursos.cursos_online.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuariosService {

    @Autowired
    private UsuariosRepository userRepo;

    @Autowired
    private FuncoesRepository funcoesRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Usuarios findByEmail(String email){
        return userRepo.findByEmail(email);
    }

    //registro
    public void registroAluno(Usuarios user) {
        Funcoes funcaoUser = funcoesRepo.findByNome("Aluno");
        user.setFk_funcao(funcaoUser);
        encodePassword(user);
        userRepo.save(user);
    }

    public void delete(long id){
        userRepo.deleteById(id);
    }

    public List<Usuarios> listAll() {
        return userRepo.findAll();
    }

    public Usuarios get(Long id) {
        return userRepo.findById(id).get();
    }

    public List<Funcoes> listRoles() {
        return funcoesRepo.findAll();
    }

    public void save(Usuarios user) {
        encodePassword(user);
        userRepo.save(user);
    }

    private void encodePassword(Usuarios user) {
        String encodedPassword = passwordEncoder.encode(user.getSenha());
        user.setSenha(encodedPassword);
    }

    public void iniciarPrograma(){
        if(funcoesRepo.findAll().isEmpty() & userRepo.findAll().isEmpty()) {
            funcoesRepo.save(new Funcoes("Aluno"));
            funcoesRepo.save(new Funcoes("Administrador"));
            save(new Usuarios(Long.parseLong("1"), "admin@admin.com", "admin", "admin1", funcoesRepo.findByNome("Administrador")));
        }
    }

}
