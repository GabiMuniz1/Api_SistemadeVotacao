package com.sistema.votacao.dto;

import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.sistema.votacao.entity.enuns.RespostaVotoEnum;

import lombok.Data;

@Data
public class VotoDTO {
	@NotNull
	private Long sessaoVotacaoId;
	
	@NotNull(message = "Voto é obrigatório e precisa seguir o padrão: SIM/NAO")
    @Enumerated
    private RespostaVotoEnum voto;
	
	 @NotNull(message = "CPF é obrigatório")
	    private String cpf;
}
