package com.cursos.cursos_online.controller;

import com.cursos.cursos_online.config.CustomUserDetails;
import com.cursos.cursos_online.domain.Funcoes;
import com.cursos.cursos_online.domain.UsuarioCurso;
import com.cursos.cursos_online.domain.Usuarios;
import com.cursos.cursos_online.repository.FuncoesRepository;
import com.cursos.cursos_online.service.UsuarioCursoService;
import com.cursos.cursos_online.service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private UsuariosService service;

    @Autowired
    private FuncoesRepository funcoesRepo;

    @Autowired
    private UsuarioCursoService serviceUC;

    //user não logado

    //index
    @GetMapping("/")
    public String index(){
        service.iniciarPrograma();
        return "index";
    }

    //login
    @GetMapping("/login")
    public String login(Model model){

        return "login";
    }

    //registro novo usuário (ALUNO)
    @GetMapping("/registro")
    public String registroAluno(Model model){
        model.addAttribute("Usuario", new Usuarios());

        return "registroaluno";
    }

    //salvar registro novo usuário (ALUNO)
    @PostMapping("/salvar-registro")
    public String saveRegistro(Usuarios user) {

        if(service.findByEmail(user.getEmail()) != null){
            return "redirect:/registro?emailinvalido";
        }else{
            service.registroAluno(user);
            return "redirect:/login?registrado";
        }
    }

    //admin

    //listagem usuarios
    @GetMapping("/admin/users")
    public String usuarios(Model model) {
        List<Usuarios> listaUsuarios = service.listAll();
        model.addAttribute("listaUsuarios", listaUsuarios);

        return "admin/listausuarios";
    }

    //adição novo usuario
    @GetMapping("/admin/novo-usuario")
    public String adicionarUsuario(Model m){
        m.addAttribute("Usuario",new Usuarios());
        List<Funcoes> listaFuncoes = funcoesRepo.findAll();
        m.addAttribute("listaFuncoes", listaFuncoes);
        return "admin/criacaousuario";
    }

    //salvar novo usuário
    @PostMapping("/salvar-usuario")
    public String SalvarRegistroAdmin(Usuarios user) {
        Usuarios procuraEmail = service.findByEmail(user.getEmail());
        try {
            if(procuraEmail != null){
                return "redirect:/admin/novo-usuario?emailinvalido";
            }else{
                service.save(user);
                return "redirect:/admin/users?save";
            }
        }catch (Exception e){
            return "redirect:/admin/users?saveerror";
        }
    }

    //deletar usuário
    @RequestMapping("/admin/deletar-usuario/{id}")
    public String deleteUser(@PathVariable(name = "id") int id){
        try{
            service.delete(id);
            return "redirect:/admin/users?delete";
        }catch(Exception e){
            return "redirect:/admin/users?deleteerror";
        }
    }

    //editar usuário
    @GetMapping("/admin/users/{id}")
    public String editarUsuario(@PathVariable("id") Long id, Model model) {
        Usuarios user = service.get(id);
        model.addAttribute("Usuario", user);
        List<Funcoes> listaFuncoes = funcoesRepo.findAll();
        model.addAttribute("listaFuncoes", listaFuncoes);

        return "admin/edicaousuario";
    }

    //salvar edição usuário
    @PostMapping("/salvar-edicao-usuario")
    public String salvarEditarUsuario(Usuarios user) {

        Usuarios procuraEmail = service.findByEmail(user.getEmail());
        try {
            if(procuraEmail != null & procuraEmail.getId() != user.getId()){
                return "redirect:/admin/novo-usuario?emailinvalido";
            }else{
                service.save(user);
                return "redirect:/admin/users?save";
            }
        }catch (Exception e){
            return "redirect:/admin/users?saveerror";
        }

    }

    //ADMIN e ALUNO

    //perfil
    @RequestMapping("/perfil")
    public String viewUsuario(Model model){

        Usuarios user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        List<UsuarioCurso> listaCursos = serviceUC.findByUsuario(user.getId());
        List<UsuarioCurso> listaCertificados = new ArrayList<>();
        if(listaCursos.isEmpty()){
            model.addAttribute("checagem", 0);
        }else{
            for (UsuarioCurso uc : listaCursos) {
                if(!uc.getBool_libera_certificado()){
                    if(uc.getPorcentagem_aulas_assistidas() >= 100){
                        uc.setBool_libera_certificado(true);
                        serviceUC.save(new UsuarioCurso(uc.getId(),uc.getBool_libera_certificado(),100,uc.getQuantidade_aulas(),uc.getLista_aulas_assistidas(),uc.getFk_curso(),user));
                        listaCertificados.add(uc);
                    }
                }else{
                    listaCertificados.add(uc);
                }
            }
            if(!listaCertificados.isEmpty()){
                model.addAttribute("checagem", 1);
            }
        }
        model.addAttribute("listaCursosConcluidos", listaCertificados);


        return "perfil";
    }

    //editar perfil
    @GetMapping("/editar-perfil/{id}")
    public String editarPerfil(@PathVariable("id") Long id, Model model) {
        Usuarios user = service.get(id);
        model.addAttribute("Usuario", user);
        List<Funcoes> listaFuncoes = funcoesRepo.findAll();
        model.addAttribute("listaFuncoes", listaFuncoes);

        return "editarperfil";
    }

    //salvar edição
    @PostMapping("/salvar-edicao")
    public String saveEdicaoPerfil(Usuarios user) {

        Usuarios user_logado = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Usuarios procuraEmail = service.findByEmail(user.getEmail());

        if(procuraEmail != null & procuraEmail.getId() != user_logado.getId()){
            return "redirect:/editar-perfil/"+user.getId()+"?emailinvalido";
        }else{
            String funcao = user_logado.getFk_funcao().getNome();
            user.setFk_funcao(user_logado.getFk_funcao());
            service.save(user);

            return "redirect:/login?editado";
        }
    }

}
