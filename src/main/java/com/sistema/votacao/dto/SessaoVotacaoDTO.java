package com.sistema.votacao.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder(toBuilder = true)
@Data
@EqualsAndHashCode
public class SessaoVotacaoDTO {
	private Long idPauta;

	private Integer duracao;
}
