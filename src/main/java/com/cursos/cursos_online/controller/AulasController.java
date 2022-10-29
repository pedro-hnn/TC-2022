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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class AulasController {

    @Autowired
    private CursosService serviceC;

    @Autowired
    private AulasService serviceA;

    @Autowired
    private UsuarioCursoService serviceUC;

    public static String uploadDirectoryVideos = System.getProperty("user.dir")+ "/src/main/resources/media/videos";

    //listagem
    @GetMapping("/admin/aulas")
    public String aulasAdmin(Model m){

        if(serviceC.listAll().isEmpty()){
            m.addAttribute("vazio",1);
        }else{
            List<Aulas> listaAulas = serviceA.listAll();
            m.addAttribute("listaAulas",listaAulas);
            m.addAttribute("vazio",0);

        }

        return "admin/listaaulasadmin";


    }

    //single
    @RequestMapping("/cursos/{fk_curso}/{id}")
    public String viewAula(Model model, @PathVariable(name="fk_curso") Long fk_curso, @PathVariable(name="id") Long id){
        Aulas a = serviceA.get(id);
        Cursos c = serviceC.get(fk_curso);

        List<Aulas> listaAulas = serviceA.findByCurso(c.getId());

        model.addAttribute("Aula",a);
        model.addAttribute("AulasConectadas",listaAulas);

        Usuarios user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        String funcao = user.getFk_funcao().getNome();

        if(funcao.equals("Aluno")){
            try{
                UsuarioCurso usuarioCurso = serviceUC.findByUsuarioEcurso(user.getId(),fk_curso);

                ArrayList<String> lista_aulas_assistidas = new ArrayList<String>(Arrays.asList(usuarioCurso.getLista_aulas_assistidas().split(",")));


                if(!lista_aulas_assistidas.contains(Long.toString(a.getId()))){
                    usuarioCurso.setLista_aulas_assistidas(usuarioCurso.getLista_aulas_assistidas()+","+Long.toString(a.getId()));
                    lista_aulas_assistidas = new ArrayList<String>(Arrays.asList(usuarioCurso.getLista_aulas_assistidas().split(",")));
                }

                Boolean libera_certificado = false;
                Integer porcentagem = Math.round((Float.valueOf(lista_aulas_assistidas.size())/Float.valueOf(listaAulas.size()))*100);

                if(porcentagem >= 100){
                    libera_certificado = true;
                }


                serviceUC.save(new UsuarioCurso(usuarioCurso.getId(),libera_certificado,porcentagem,listaAulas.size(),usuarioCurso.getLista_aulas_assistidas(),c,user));

            }catch (NullPointerException e){
                serviceUC.save(new UsuarioCurso(false,Math.round((Float.valueOf(1)/Float.valueOf(listaAulas.size()))*100),listaAulas.size(),Long.toString(a.getId()),c,user));
            }

        }
        return "aluno/singleaula";
    }

    //criação
    @GetMapping("/admin/nova-aula")
    public String CriarAula(Model m){
        List<Cursos> listaCursos = serviceC.listAll();
        m.addAttribute("listaCursos",listaCursos);
        m.addAttribute("Aulas",new Aulas());
        return "admin/criacaoaula";
    }

    @RequestMapping(value = "/admin/salvar-aula", method = RequestMethod.POST)
    public String saveAula(@ModelAttribute("Aulas") Aulas a, @RequestParam("video_aula") MultipartFile video_aula) {
        try{
            Path VideoAulaCaminho = Paths.get(uploadDirectoryVideos, video_aula.getOriginalFilename());
            Files.write(VideoAulaCaminho,video_aula.getBytes());

            a.setPath_video("file://"+VideoAulaCaminho.toString().replace("\\","/"));

            serviceA.save(a);
            return "redirect:/admin/aulas?save";
        }catch (Exception e){
            return "redirect:/admin/aulas?saveerror";
        }

    }

    //edição
    @RequestMapping("/admin/editar-aula/{id}")
    public ModelAndView editCurso(@PathVariable(name="id") int id, Model m){
        ModelAndView mav = new ModelAndView("admin/criacaoaula");
        List<Cursos> listaCursos = serviceC.listAll();
        m.addAttribute("listaCursos",listaCursos);
        Aulas a = serviceA.get(id);
        mav.addObject("Aulas",a);
        return mav;
    }


    //deleção
    @RequestMapping("/admin/deletar-aula/{id}")
    public String deleteAula(@PathVariable(name = "id") Long id){
        try {
            serviceA.delete(id);
            return "redirect:/admin/aulas?delete";
        }catch (Exception e){
            return "redirect:/admin/aulas?deleteerror";
        }
    }
}
