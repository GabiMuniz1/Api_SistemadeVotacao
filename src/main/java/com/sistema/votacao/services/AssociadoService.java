package com.sistema.votacao.services;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.sistema.votacao.entity.Associado;
import com.sistema.votacao.repositories.AssociadoRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssociadoService {

	@Autowired
	CpfService cpfService;

	@Autowired
	private AssociadoRepository associadoRepository;

	@Autowired
	public AssociadoService(AssociadoRepository associadoRepository) {
		this.associadoRepository = associadoRepository;
	}

	public Associado buscarPorId(Long id) {
		log.info("Buscando pelo id {}", id);
		return associadoRepository.findById(id).orElse(null);
	}

	public Associado buscarPorNome(String nome) {
		log.info("Buscando pelo nome {}", nome);
		return associadoRepository.findByNome(nome).orElse(null);
	}

	public Optional<Associado> criarAssociado(Associado associado) {
		if (associadoRepository.findByCpf(associado.getCpf()).isPresent()) {
			log.error("CPF ja cadastrado {}", associado.getCpf());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já cadastrado", null);
		}
		if (associadoRepository.findByEmail(associado.getEmail()).isPresent()) {
			log.error("Email ja cadastrado {}", associado.getEmail());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "EMAIL já cadastrado", null);
		}
		if (!cpfService.validarCpf(associado.getCpf())) {
			log.error("CPF invalido {}", associado.getCpf());
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF não é válido", null);
		}

		associado.setSenha(encriptyPassword(associado.getSenha()));
		log.info("Salvando associado");
		return Optional.of(associadoRepository.save(associado));
	}

	public Associado atualizarAssociado(Associado associado) {

		if (associadoRepository.findById(associado.getId()).isPresent()) {
			Optional<Associado> encontrarAssociado = associadoRepository.findByEmail(associado.getEmail());

			if (encontrarAssociado.isPresent() && !Objects.equals(encontrarAssociado.get().getId(), associado.getId())) {
				log.info("Associado não existe");
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Associado não existe", null);
			}

			if (Boolean.FALSE.equals(cpfService.validarCpf(associado.getCpf()))) {
				log.info("Cpf invalido");
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cpf invalido", null);
			}

			associado.setSenha(encriptyPassword(associado.getSenha()));
			log.info("Salvando usuario");
			return associadoRepository.save(associado);
		}
		log.info("Usuario nao encontrado");
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Associado não foi encontrado", null);
	}

	public void deletaAssociado(Long id) {
		log.info("Deletando associado");
		associadoRepository.deleteById(id);
	}
	
	public String encriptyPassword (String password) {
		BCryptPasswordEncoder enconder = new BCryptPasswordEncoder();
		String passwordEncoder = enconder.encode(password);
		return passwordEncoder;
	}
	
	public Associado buscarPorCPF(String cpf) {
		log.info("Buscando por CPF");
		return associadoRepository.findByCpf(cpf).orElse(null);
	}
}
