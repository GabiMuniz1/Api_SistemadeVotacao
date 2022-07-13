package com.sistema.votacao.entity.enuns;

import org.springframework.security.core.GrantedAuthority;

public enum AssociadoTipoEnum implements GrantedAuthority{

	ROLE_ADMIN("Associado tipo Administrador"), 
	ROLE_COPERADO("Associado tipo Coperado");
	
	private final String label;

	AssociadoTipoEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String getAuthority() {
		return null;
	}
}
