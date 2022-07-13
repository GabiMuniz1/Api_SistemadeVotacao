package com.sistema.votacao.entity.enuns;

public enum RespostaVotoEnum {

	SIM("SIM"),
    NAO("NAO");
	
	private final String label;
	
	RespostaVotoEnum(String label){
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
}
