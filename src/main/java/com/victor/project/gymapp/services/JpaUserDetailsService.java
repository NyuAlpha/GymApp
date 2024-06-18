package com.victor.project.gymapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.victor.project.gymapp.models.Role;
import com.victor.project.gymapp.repositories.UserRepository;
import com.victor.project.gymapp.security.CustomUserDetails;



/*
 * Sobreescribe el UserDetailsService de Spring Security
 */
@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {

	//Repositorio para acceso a usaurio en la BBDD
	@Autowired
	private UserRepository userRepository;

	private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);


	//Sobreescribe el método con el que spring Security carga los usuarios
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		//Busca el usuario
		Optional<com.victor.project.gymapp.models.User> optionalUser = userRepository.findByUsername(username);

		//Si no se encuentra el usuario
		if (optionalUser.isEmpty()) {
			logger.error("Error en el Login: no existe el usuario '" + username + "' en el sistema!");
			throw new UsernameNotFoundException("Username: " + username + " no existe en el sistema!");
		}
		com.victor.project.gymapp.models.User user = optionalUser.get();

		//Obtiene los roles del usuario para almacenarlo como List<GrantedAuthority>
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Role role : user.getRoles()) {
			logger.info("Role: ".concat(role.getName()));
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}

		//Si el usuario no tiene roles, aunque en principio es imposible
		if (authorities.isEmpty()) {
			logger.error("Error en el Login: Usuario '" + username + "' no tiene roles asignados!");
			throw new UsernameNotFoundException(
					"Error en el Login: usuario '" + username + "' no tiene roles asignados!");
		}

		//Crea y devuelve el UserDetails personalizado
		return new CustomUserDetails(
				user.getUsername(),
				user.getPassword(),
				user.isEnabled(),
				authorities,
				user.getUuid());
	}

}
