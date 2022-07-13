package com.sistema.votacao.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sistema.votacao.entity.audit.DataPauta;
import com.sistema.votacao.entity.enuns.AssociadoTipoEnum;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "tb_associado")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Associado extends DataPauta implements UserDetails{

	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;
	@Column(name = "Nome")
	@NotNull(message = "Preencha seu nome")
	private String nome;
	@Column(name = "Cpf")
	@NotNull(message = "Insira somente os numeros")
	private String cpf;
	@Column(name= "Tipo")
	@NotNull(message = "Associado Administrativo ou Não")
	@Enumerated(EnumType.STRING)
	private AssociadoTipoEnum tipo;
	@Column(name = "Email")
	@NotNull(message = "Insira email valido")
	private String email;
	@Column(name = "Senha")
	@NotNull(message = "Digite a senha")
	@Size(min = 5, max = 50)
	private String senha;
	@OneToMany(mappedBy = "associado", cascade = CascadeType.REMOVE)     
	@JsonIgnoreProperties("associado")
	private List<Voto> listaVoto = new ArrayList<>();
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority(tipo.name()));
	}

	@Override
	public String getPassword() {
		return this.senha;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
