package com.sistema.votacao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.votacao.entity.Associado;
import com.sistema.votacao.entity.SessaoVotacao;
import com.sistema.votacao.entity.Voto;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

	Boolean existsBySessaoVotacaoAndAssociado(SessaoVotacao sessaoVotacao, Associado associado);
}
