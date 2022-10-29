package com.cursos.cursos_online.controller;

import com.cursos.cursos_online.config.CustomUserDetails;
import com.cursos.cursos_online.domain.Aulas;
import com.cursos.cursos_online.domain.Cursos;
import com.cursos.cursos_online.domain.UsuarioCurso;
import com.cursos.cursos_online.domain.Usuarios;
import com.cursos.cursos_online.service.AulasService;
import com.cursos.cursos_online.service.CursosService;
import com.cursos.cursos_online.service.UsuarioCursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class CursosController {

    @Autowired
    private CursosService service;

    @Autowired
    private AulasService serviceA;

    @Autowired
    private UsuarioCursoService serviceUC;

    public static String uploadDirectoryImages = System.getProperty("user.dir")+ "/src/main/resources/media/images";


    //listagem

    //listagem público
    @GetMapping("/aluno/cursos")
    public String cursos(Model m){

        if(service.listAll().isEmpty()){
            m.addAttribute("vazio",1);
        }else{
            List<Cursos> listaCursos = service.listAll();
            m.addAttribute("listaCursos",listaCursos);
            m.addAttribute("vazio",0);
        }
        return "aluno/listacursosaluno";
    }

    //listagem admin
    @GetMapping("/admin/cursos")
    public String cursosAdmin(Model m){
        List<Cursos> listaCursos = service.listAll();
        m.addAttribute("listaCursos",listaCursos);
        return "admin/listacursosadmin";
    }

    @RequestMapping("/cursos/{id}/certificado")
    public String viewCertificado(Model model, @PathVariable(name="id") Long id){
        Cursos curso = service.get(id);
        Usuarios user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        try{
            UsuarioCurso usuarioCurso = serviceUC.findByUsuarioEcurso(user.getId(),id);
            if(usuarioCurso.getBool_libera_certificado()){

                model.addAttribute("Curso",curso);
                model.addAttribute("Usuario",user);

                return "certificado";
            }else{
                return "redirect:/perfil";
            }
        }catch (NullPointerException e){
            return "redirect:/perfil";
        }
    }

    //single
    @RequestMapping("/cursos/{id}")
    public String viewCurso(Model model, @PathVariable(name="id") Long id){

        if(service.listAll().isEmpty()){
            model.addAttribute("vazio",1);
        }else{
            Cursos c = service.get(id);
            List<Aulas> listaAulas = serviceA.findByCurso(id);
            model.addAttribute("Curso",c);
            model.addAttribute("AulasConectadas",listaAulas);

            Usuarios user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            try{
                UsuarioCurso usuarioCurso = serviceUC.findByUsuarioEcurso(user.getId(),id);
                model.addAttribute("UsuarioCurso",usuarioCurso.getPorcentagem_aulas_assistidas());
            }catch (NullPointerException e){
                model.addAttribute("UsuarioCurso","0");
            }
        }
        return "aluno/singlecurso";
    }

    //criação
    @GetMapping("/admin/novo-curso")
    public String CriarCurso(Model m){
        m.addAttribute("Cursos",new Cursos());
        return "admin/criacaocurso";
    }

    @RequestMapping(value = "/admin/salvar-curso", method = RequestMethod.POST)
    public String saveCurso(@ModelAttribute("Cursos") Cursos c, @RequestParam("capa_curso") MultipartFile capa_curso, @RequestParam("fundo_certificado") MultipartFile fundo_certificado ){

        try {
            Path CapaCursoCaminho = Paths.get(uploadDirectoryImages, capa_curso.getOriginalFilename());
            Path FundoCertificadoCaminho = Paths.get(uploadDirectoryImages, fundo_certificado.getOriginalFilename());

            Files.write(CapaCursoCaminho, capa_curso.getBytes());
            Files.write(FundoCertificadoCaminho, fundo_certificado.getBytes());

            c.setPath_capa("file://" + CapaCursoCaminho.toString().replace("\\", "/"));
            c.setPath_imagem_certificado("file://" + FundoCertificadoCaminho.toString().replace("\\", "/"));

            service.save(c);

            return "redirect:/admin/cursos?save";
        }catch(Exception e){
            return "redirect:/admin/cursos?saveerror";
        }
    }

    //edição
    @RequestMapping("/admin/editar-curso/{id}")
    public ModelAndView editCurso(@PathVariable(name="id") int id){
        ModelAndView mav = new ModelAndView("admin/criacaocurso");
        Cursos c = service.get(id);
        mav.addObject("Cursos",c);
        return mav;
    }

    //deleção

    @RequestMapping("/admin/deletar-curso/{id}")
    public String deleteCurso(@PathVariable(name = "id") int id){
        try {
            service.delete(id);
            return "redirect:/admin/cursos?delete";
        }catch (Exception e){
            return "redirect:/admin/cursos?deleteerror";
        }
    }













}
