package com.sistema.votacao.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sistema.votacao.entity.Associado;
import com.sistema.votacao.repositories.AssociadoRepository;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

    private TokenService tokenService;
    private AssociadoRepository associadoRepository;

    public AutenticacaoViaTokenFilter(TokenService tokenService, AssociadoRepository associadoRepository){
        this.tokenService = tokenService;
        this.associadoRepository=associadoRepository;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = recuperarToken(request);
        boolean valido = tokenService.isTokenValido(token);
        if (valido){
            autenticarAssociado(token);
        }

        filterChain.doFilter(request, response);
    }

    private void autenticarAssociado(String token){
        Long idAssociado = tokenService.getIdAssociado(token);
        Associado associado = associadoRepository.findById(idAssociado).get();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(associado, null, associado.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
    private String recuperarToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")){
             return null;
        }

        return token.substring(7, token.length());
    }
}