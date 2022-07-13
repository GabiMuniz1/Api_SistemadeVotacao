package com.sistema.votacao.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.votacao.entity.SessaoVotacao;

@Repository
public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {

	boolean existsByPautaId(Long pauta);
	
	Optional<SessaoVotacao> findById(Long id);

}
