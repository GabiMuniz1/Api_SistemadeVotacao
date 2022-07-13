package com.sistema.votacao.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema.votacao.entity.Pauta;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

	Optional<Pauta> findById(Long id);
}
