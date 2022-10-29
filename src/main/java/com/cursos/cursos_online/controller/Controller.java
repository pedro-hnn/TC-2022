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

import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private UsuariosService service;

    @Autowired
    private FuncoesRepository funcoesRepo;

    @Autowired
    private UsuarioCursoService serviceUC;

    @GetMapping("/")
    public String index(){
        service.iniciarPrograma();
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model){

        return "login";
    }

    @GetMapping("/admin/novo-usuario")
    public String adicionarUsuario(Model m){
        m.addAttribute("Usuario",new Usuarios());
        List<Funcoes> listaFuncoes = funcoesRepo.findAll();
        m.addAttribute("listaFuncoes", listaFuncoes);
        return "admin/novousuario";
    }

    @RequestMapping("/admin/deletar-usuario/{id}")
    public String deleteUser(@PathVariable(name = "id") int id){
        try{
            service.delete(id);
            return "redirect:/admin/users?delete";
        }catch(Exception e){
            return "redirect:/admin/users?deleteerror";
        }
    }

    @GetMapping("/registro")
    public String registroAluno(Model model){
        model.addAttribute("Usuario", new Usuarios());

        return "registroaluno";
    }

    @PostMapping("/salvar-registro")
    public String saveRegistro(Usuarios user) {

        if(service.findByEmail(user.getEmail()) != null){
            return "redirect:/registro?emailinvalido";
        }else{
            service.registroAluno(user);
            return "redirect:/login?registrado";
        }
    }



    @PostMapping("/editar-registro")
    public String editRegistroAdmin(Usuarios user) {
        try {
            service.save(user);
            return "redirect:/admin/users?save";
        }catch (Exception e){
            return "redirect:/admin/users?saveerror";
        }

    }

    @GetMapping("/admin/users")
    public String usuarios(Model model) {
        List<Usuarios> listaUsuarios = service.listAll();
        model.addAttribute("listaUsuarios", listaUsuarios);

        return "admin/listausuarios";
    }

    @GetMapping("/admin/users/{id}")
    public String editarUsuario(@PathVariable("id") Long id, Model model) {
        Usuarios user = service.get(id);
        model.addAttribute("Usuario", user);
        List<Funcoes> listaFuncoes = funcoesRepo.findAll();
        model.addAttribute("listaFuncoes", listaFuncoes);

        return "admin/editarusuario";
    }

    @RequestMapping("/perfil")
    public String viewUsuario(Model model){

        Usuarios user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        List<UsuarioCurso> listaCursos = serviceUC.findByUsuario(user.getId());
        if(listaCursos.isEmpty()){
            model.addAttribute("checagem", 0);
        }else{
            model.addAttribute("checagem", 1);
        }
        model.addAttribute("listaCursosConcluidos", listaCursos);


        return "perfil";
    }

    @GetMapping("/editar-perfil/{id}")
    public String editarPerfil(@PathVariable("id") Long id, Model model) {
        Usuarios user = service.get(id);
        model.addAttribute("Usuario", user);
        List<Funcoes> listaFuncoes = funcoesRepo.findAll();
        model.addAttribute("listaFuncoes", listaFuncoes);

        return "editarperfil";
    }

}
