package com.sistema.votacao.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sistema.votacao.dto.SessaoIniciarDTO;
import com.sistema.votacao.dto.SessaoVotacaoDTO;
import com.sistema.votacao.entity.Pauta;
import com.sistema.votacao.entity.SessaoVotacao;
import com.sistema.votacao.entity.enuns.RespostaVotoEnum;
import com.sistema.votacao.entity.enuns.StatusEnum;
import com.sistema.votacao.exceptions.NotFoundException;
import com.sistema.votacao.repositories.PautaRepository;
import com.sistema.votacao.repositories.SessaoVotacaoRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SessaoService {
	
	private final PautaRepository pautaRepository;
	
	private final PautaService pautaService;
	
	private final SessaoVotacaoRepository sessaoRepository;
	
	public SessaoService(SessaoVotacaoRepository sessaoRepository, PautaRepository pautaRepository,
			PautaService pautaService) {
		this.sessaoRepository = sessaoRepository;
		this.pautaRepository = pautaRepository;
		this.pautaService = pautaService;
	}
	@Transactional
	private boolean verificarExistenciaPauta(Long idPauta) {
		log.info("Verificando se a pauta {} existe", idPauta);
		return sessaoRepository.existsByPautaId(idPauta);
	}
	@Transactional
	public List<SessaoVotacao> buscarTodas() {
		log.info("Buscando todas as sessoes");
		return sessaoRepository.findAll();
	}
	@Transactional
	public Optional<Pauta> buscaPorId(Long id) {
		log.info("Buscando pauta por Id {}", id);
		return pautaRepository.findById(id);
	}
	@Transactional
	public SessaoVotacao criarSessao(SessaoVotacaoDTO dto) {
		if (verificarExistenciaPauta(dto.getIdPauta())) {
			log.info("Sessao ja criada");
			throw new RuntimeException("Sessao ja criada");
		}
		Pauta pauta = pautaService.buscarPautaPeloID(dto.getIdPauta());

		SessaoVotacao sessaoVotacao = SessaoVotacao.builder()
				.duracao(tempoFechado(dto.getDuracao()))
				.pauta(pauta).build();
		log.info("Criando sessao");
		return sessaoRepository.save(sessaoVotacao);
	}
	@Transactional
	private Integer tempoFechado(Integer duracao) {

		return Objects.isNull(duracao) ? 60 : duracao;
	}
	@Transactional
	private Pauta buscarPauta(Long idPauta) {
		return pautaRepository.findById(idPauta)
				.orElseThrow(() -> new NotFoundException("Pauta nao encontrada"));
	}
	
	@Transactional
	public SessaoVotacao iniciarVotacao(SessaoIniciarDTO dto) {
		SessaoVotacao sessaoVotacao = sessaoRepository.findById(dto.getSessaoId())
				.orElseThrow(() -> new NotFoundException("Sessao votacao nao encontrada"));

		if (sessaoVotacao.getPauta()
				.getStatus()
				.equals(StatusEnum.valueOf("FECHADA"))) {
			throw new RuntimeException("A pauta esta FECHADA");
		}

		sessaoVotacao.setFechado(Instant.now()
				.plus(sessaoVotacao.getDuracao(), ChronoUnit.SECONDS));

		return sessaoRepository.save(sessaoVotacao);
	}
	
	@Scheduled(fixedDelay = 60000)
	@Transactional
	public void fecharSessao() {
		List<SessaoVotacao> listaSessaoVotacao = obterVotacaoExpiradaMasNaoFechada();

		listaSessaoVotacao.forEach(votacao -> {
			log.info("Estatisticas da sessao");
			votacao.getPauta().setQtdVotos(votacao.getVotos().size());
			votacao.getPauta().setQtdVotosSim(qtSim(votacao));
			votacao.getPauta().setQtdVotosNao(qtNao(votacao));
			sessaoRepository.save(votacao);
			pautaService.mudancaStatus(votacao.getPauta());
			pautaService.definirPercentual(votacao.getPauta());
			pautaService.definirVencedor(votacao.getPauta());
		});
	}
	@Transactional
	public Integer qtSim(SessaoVotacao sessaoVotacao) {
		return Math.toIntExact(
				sessaoVotacao.getVotos()
				.stream()
				.filter(c -> c.getVoto()
				.equals(RespostaVotoEnum.valueOf("SIM")))
				.count());
	}
	@Transactional
	public Integer qtNao(SessaoVotacao sessaoVotacao) {
		return Math.toIntExact(
				sessaoVotacao.getVotos()
				.stream()
				.filter(c -> c.getVoto()
				.equals(RespostaVotoEnum.valueOf("NÃO")))
				.count());
	}
	@Transactional
	private List<SessaoVotacao> obterVotacaoExpiradaMasNaoFechada() {
		return sessaoRepository.findAll().stream()
				.filter(RespostaVotoEnum -> RespostaVotoEnum.sessaoExpirada() && RespostaVotoEnum.aberta())
				.collect(Collectors.toList());
	}
}
