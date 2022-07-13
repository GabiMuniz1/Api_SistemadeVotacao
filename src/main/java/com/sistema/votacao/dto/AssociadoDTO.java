package com.sistema.votacao.dto;

import com.sistema.votacao.entity.enuns.AssociadoTipoEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssociadoDTO {
	private String nome;
	private String cpf;
	private AssociadoTipoEnum tipo;
}
