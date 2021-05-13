package com.eds.cogua.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.eds.cogua.service.impl.UsuarioDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

	@Autowired
	private CustomLoginSuccessHandler successHandler;
	
	String[] resources = new String[]
			{
			"/include/**","/css/**","/icons/**","/media/**","/js/**","/plugins/**"
	};

	@Override
	protected void configure(HttpSecurity http) throws Exception 
	{
		http
		.authorizeRequests()
		.antMatchers(resources).permitAll()  
		.antMatchers("/","/login","/error", "/privacidad").permitAll()
		.antMatchers("/admin", "/adminAddUser", "/adminDetalleUser","/adminCalculoNomina", "/adminConsultaNomina", "/nominaTrabajador").access("hasRole('ADMIN')")
		.antMatchers("/adicionCliente","/addClient","/dashBoardClientes","/clientList","/detalleClient").access("hasAnyRole('ADMIN','GEROP')")
		.antMatchers("/horarioTrabajador", "/adminListUser").access("hasAnyRole('ADMIN','AUXPATIO')")
		.antMatchers("/inicio", "/calculoOP").access("hasAnyRole('ISLERO','AUXPATIO')")
		.antMatchers("/cambioPassword").access("hasAnyRole('ISLERO','AUXPATIO','ADMIN','GEROP')")
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/login")
		.permitAll()
		.successHandler(successHandler)
		.failureUrl("/login?error=true")
		.usernameParameter("username")
		.passwordParameter("password")
		.and()
		.logout()
		.permitAll()
		.logoutSuccessUrl("/login?logout");
		
		http.headers().frameOptions().sameOrigin();
	}
	
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() 
	{
		bCryptPasswordEncoder = new BCryptPasswordEncoder(10);
		return bCryptPasswordEncoder;
	}

	@Autowired
	UsuarioDetailsServiceImpl userDetailsService;

	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception 
	{
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());     
	}

}
