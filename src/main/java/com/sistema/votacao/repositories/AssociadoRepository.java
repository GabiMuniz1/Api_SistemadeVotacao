package com.sistema.votacao.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.votacao.entity.Associado;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, Long>{
	
	Optional<Associado> findById(Long id);

	Optional<Associado> findByNome(String nome);
	
	Optional<Associado> findByCpf(String cpf);
	
	Optional<Associado> findByEmail(String email);

	
	
}