package com.devsuperior.bds04.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Autowired
	private Environment env;
	
	@Autowired
	private JwtTokenStore tokenStore; 
	
	private static final String[] PUBLIC = { "/oauth/token", "/h2-console/**" };
	
	private static final String[] OPERATOR_GET = { "/cities/**", "/events/**" };	
	

	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore);
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		// Liberar o H2
		if (Arrays.asList(env.getActiveProfiles()).contains("test")){
			http.headers().frameOptions().disable();
			
		}
		
		http.authorizeRequests()
        .antMatchers(PUBLIC).permitAll() // totalmente público
        .antMatchers(HttpMethod.GET, OPERATOR_GET).permitAll() // GET liberado para todos
        .antMatchers(HttpMethod.POST, OPERATOR_GET).hasAnyRole("CLIENT", "ADMIN") // Adicionada permissão para CLIENT e Admin para fazer POST
        .anyRequest().hasAnyRole("ADMIN"); // qualquer outra coisa precisa estar logado
	}
}
