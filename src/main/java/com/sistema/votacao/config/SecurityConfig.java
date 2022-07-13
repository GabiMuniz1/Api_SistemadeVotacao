package com.sistema.votacao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sistema.votacao.repositories.AssociadoRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	String ROLE_ADMIN = "ADMIN";
    String ROLE_COOPERADO = "COOPERADO";
    
    private final TokenService tokenService;


    private AssociadoRepository associadoRepository;
    
    @Autowired
    public SecurityConfig(AssociadoRepository associadoRepository, TokenService tokenService){
        this.associadoRepository = associadoRepository;
        this.tokenService = tokenService;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        .antMatchers(HttpMethod.POST, "/v1/voto/voto").permitAll()
        .antMatchers(HttpMethod.POST, "/v1/auth/login").permitAll()
        .antMatchers(HttpMethod.POST, "/v1/associado/criar").permitAll()
        .antMatchers("/swagger-ui/#/").permitAll()
        .antMatchers("/swagger-resources/**").permitAll()
        .antMatchers("swagger-ui.html").permitAll()
        .antMatchers("/v1/**").hasRole(ROLE_ADMIN)
        .anyRequest().authenticated()
        .and().csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, associadoRepository), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer()throws Exception{
        return (web -> web.ignoring()
                .antMatchers("/.html", "/v2/api-docs",
                        "/webjars", "/configuration/"
                ,"/swagger-resources/",
                        "/swagger-ui/**"));
    }
}
