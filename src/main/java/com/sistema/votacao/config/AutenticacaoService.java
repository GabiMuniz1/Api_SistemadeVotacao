package com.sistema.votacao.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sistema.votacao.entity.Associado;
import com.sistema.votacao.repositories.AssociadoRepository;

@Service
public class AutenticacaoService implements UserDetailsService {

	@Autowired
	private AssociadoRepository associadoRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Associado> associado = associadoRepository.findByEmail(username);
		if (associado.isPresent()) {
			return associado.get();
		}
		throw new UsernameNotFoundException("Dados inválidos");
	}

}
