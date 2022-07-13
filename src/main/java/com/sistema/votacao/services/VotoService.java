package com.sistema.votacao.services;

import java.time.Instant;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.sistema.votacao.dto.VotoDTO;
import com.sistema.votacao.entity.Associado;
import com.sistema.votacao.entity.SessaoVotacao;
import com.sistema.votacao.entity.Voto;
import com.sistema.votacao.exceptions.NotFoundException;
import com.sistema.votacao.repositories.SessaoVotacaoRepository;
import com.sistema.votacao.repositories.VotoRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VotoService {

	@Autowired
	private VotoRepository votoRepository;

	@Autowired
	private SessaoVotacaoRepository sessaoVotacaoRepository;

	@Autowired
	private AssociadoService associadoService;

	@Autowired
	public VotoService(VotoRepository votoRepository, SessaoVotacaoRepository sessaoVotacaoRepository,
			AssociadoService associadoService) {
		this.votoRepository = votoRepository;
		this.sessaoVotacaoRepository = sessaoVotacaoRepository;
		this.associadoService = associadoService;
	}

	@Transactional
	public Voto votacao(VotoDTO dto) {
		SessaoVotacao sessaoVotacao = sessaoVotacaoRepository.findById(dto.getSessaoVotacaoId()).orElse(null);
		Associado associado = associadoService.buscarPorCPF(dto.getCpf());
		log.info("Verificando sessao votacao");
		if (sessaoVotacao == null) {
			log.info("Sessao nao encontrada");
			throw new NotFoundException("Sessao votacao nao encontrada");
		}


		Voto voto = Voto.builder()
				.voto(dto.getVoto())
				.votoInstant(Instant.now())
				.associado(associado)
				.sessaoVotacao(sessaoVotacao).build();

		if (Boolean.TRUE.equals(verificarVotoUnico(sessaoVotacao, associado))) {

			throw new RuntimeException("Usuario ja votou");
		}
		verificaSessaoVotoValidoTempo(voto);

		log.info("Salvando o voto");
		return votoRepository.save(voto);
	}

	private Boolean verificarVotoUnico(SessaoVotacao sessaoVotacao, Associado associado) {

		return votoRepository.existsBySessaoVotacaoAndAssociado(sessaoVotacao, associado);
	}

	@Transactional
	private void verificaSessaoVotoValidoTempo(Voto voto) {
		log.info("Verificando tempo valido da sessao");
		if (voto.getVotoInstant().isAfter(voto.getSessaoVotacao().getFechado())) {
			log.info("Sessao expirada");
			throw new RuntimeException("Sessao votacao expirada");
		}
	}

}