package com.sistema.votacao.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sistema.votacao.dto.CpfDTO;

@FeignClient(name = "CpfValidacao", url = "https://cpf-api-almfelipe.herokuapp.com")
public interface ValidarCpf {

	@RequestMapping(method = RequestMethod.GET, value = "/cpf/{cpf}")
	CpfDTO validarCPF(@PathVariable String cpf);
}
