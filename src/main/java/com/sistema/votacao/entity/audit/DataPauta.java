package com.sistema.votacao.entity.audit;

import java.time.Instant;

import javax.persistence.Column;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

public class DataPauta {

	@CreationTimestamp
	@Column(name = "CRIACAO", nullable = false, updatable = false)
	private Instant criacao;
	
	@UpdateTimestamp
	@Column(name = "ALTERACAO", nullable = false)
	private Instant alteracao;
	
}
