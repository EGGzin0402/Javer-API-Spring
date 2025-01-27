package com.eggzin.cliente_service.config;

import com.eggzin.cliente_service.jwt.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.eggzin.cliente_service.jwt.JwtAuthorizationFilter;

@EnableMethodSecurity
@EnableWebMvc
@Configuration
public class SpringSecurityConfig {
	
	private static final String[] DOCUMENTATION_OPENAPI = {
	        "/cliente-service/docs/index.html",
	        "/cliente-service/docs.html",
	        "/cliente-service/docs/**",
	        "/v3/api-docs/**",
	        "/cliente-service/swagger-ui-custom.html",
	        "/cliente-service/swagger-ui.html",
	        "/cliente-service/swagger-ui/**",
	        "/**.html",
	        "/webjars/**",
	        "/configuration/**",
	        "/swagger-resources/**"
	};
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.formLogin(form -> form.disable())
				.httpBasic(basic -> basic.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.POST, "cliente-service/usuarios").permitAll()
						.requestMatchers(HttpMethod.POST, "cliente-service/auth").permitAll()
						.requestMatchers(DOCUMENTATION_OPENAPI).permitAll()
						.anyRequest().authenticated()
				).sessionManagement(
						session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				).addFilterBefore(
						jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class
				).exceptionHandling(ex -> ex
						.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
				)
				.build();
	}
	
	@Bean
	public JwtAuthorizationFilter jwtAuthorizationFilter() {
		return new JwtAuthorizationFilter();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

}
