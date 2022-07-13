package com.sistema.votacao.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sistema.votacao.entity.Pauta;
import com.sistema.votacao.services.PautaService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/v1/pauta")
@CrossOrigin(origins = "*")
public class PautaController {
	@Autowired
	private PautaService pautaService;

	@Autowired
	public PautaController(PautaService pautaService) {
		this.pautaService = pautaService;
	}
	
	@GetMapping
	@Cacheable(value = "ListaDePautas")
	public ResponseEntity<List<Pauta>> buscarTodasPautas() {
		log.info("Buscando todas as pautas");

		return ResponseEntity.ok(pautaService.buscarTodasPautas());
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<Pauta> buscarById(@PathVariable Long id){
    	log.info("Buscando pauta por Id {}", id);
        return new ResponseEntity<>(pautaService.buscarPautaPeloID(id), HttpStatus.OK);
    }
	@PostMapping("/criar")
	@CacheEvict(value = "ListaDePautas", allEntries = true)
	public ResponseEntity<Pauta> criarPauta(@Valid @RequestBody Pauta pauta) {
		log.info("Criando Pauta");
		return new ResponseEntity<>(pautaService.criarPauta(pauta), HttpStatus.CREATED);
	}
}
