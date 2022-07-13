package com.sistema.votacao.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sistema.votacao.entity.audit.DataPauta;
import com.sistema.votacao.entity.enuns.RespostaVotoEnum;
import com.sistema.votacao.entity.enuns.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "tb_voto")
public class Voto extends DataPauta{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;
	
	@Column(name = "VotoInstant")
    private Instant votoInstant;
	
	@Column(name = "Voto")
    private RespostaVotoEnum voto;

    @ManyToOne
    @JsonIgnoreProperties("votos")
    @JoinColumn(name = "id_sessao_votacao")
    private SessaoVotacao sessaoVotacao;
    
    @ManyToOne
    @JsonIgnoreProperties("votos")
    @JoinColumn(name = "fk_idAssociado")
    private Associado associado;
}
