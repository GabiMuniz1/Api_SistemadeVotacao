package com.sistema.votacao.controllers;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.votacao.entity.Associado;
import com.sistema.votacao.services.AssociadoService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/associado")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class AssociadoController {
	
	@Autowired
	public AssociadoService associadoService;
	
	@GetMapping("/{id}")
	@Transactional
	public ResponseEntity<Associado> buscarPorId(@PathVariable Long id) {
		log.info("Buscando usuario por Id {}", id);
		return new ResponseEntity<>(associadoService.buscarPorId(id), HttpStatus.OK);
	}
	
	@GetMapping("/nome")
    @Transactional
    public ResponseEntity<Associado> buscarPorNome(@RequestParam String nome){
    	log.info("Buscando por nome {}", nome);
        return new ResponseEntity<>(associadoService.buscarPorNome(nome), HttpStatus.OK);
    }
	
	@PostMapping("/criar")
    @Transactional
    public ResponseEntity<Associado> criar(@Valid @RequestBody Associado associado){
        log.info("Criando associado");
        System.out.println("Conexao criada");
    	return new ResponseEntity(associadoService.criarAssociado(associado), HttpStatus.CREATED);
    	
    }
	@PutMapping("/atualizar")
	@Transactional
    public ResponseEntity<Associado> atualizar(@Valid @RequestBody Associado associado){
        log.info("Atualizando usuario de CPF {}", associado.getCpf());
		return new ResponseEntity<>(associadoService.atualizarAssociado(associado), HttpStatus.OK);
    }
	@DeleteMapping("/{id}")
	@Transactional
	public void deletarAssociado(@PathVariable Long id) {
		log.info("Deletando associado");
		associadoService.deletaAssociado(id);
	}
}
