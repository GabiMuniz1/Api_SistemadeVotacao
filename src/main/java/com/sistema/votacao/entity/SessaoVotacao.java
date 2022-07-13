package com.sistema.votacao.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sistema.votacao.entity.audit.DataPauta;
import com.sistema.votacao.entity.enuns.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "tb_sessaoVotacao")
public class SessaoVotacao extends DataPauta{

	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "Duracao")
	private Integer duracao;
	
	@Column(name = "Fechado")
	private Instant fechado;
	
	@OneToOne
	@JoinColumn(name = "id_pauta")
	private Pauta pauta;
	
	@JsonIgnoreProperties("sessaoVotacao")
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "sessaoVotacao", cascade = CascadeType.ALL)
	private List<Voto> votos = new ArrayList<>();
	
	public boolean aberta() {
		return getPauta().getStatus().equals(StatusEnum.valueOf("ABERTA"));
	}
	
	public boolean sessaoExpirada(){
        return this.getFechado() != null && this.getFechado().isBefore(Instant.now());
   }
}
