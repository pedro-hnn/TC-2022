package com.cursos.cursos_online.config;

import com.cursos.cursos_online.domain.Usuarios;
import com.cursos.cursos_online.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService  {

    @Autowired
    private UsuariosRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuarios user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }
        return new CustomUserDetails(user);
    }


}
