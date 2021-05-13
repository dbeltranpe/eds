package com.eds.cogua.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler 
{
	@Override
	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)	throws IOException, ServletException 
	{

		String targetUrl = determineTargetUrl(authentication);

		if(response.isCommitted())
		{
			return;
		}

		RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
		redirectStrategy.sendRedirect(request, response, targetUrl);

	}

	protected String determineTargetUrl(Authentication authentication)
	{		
		String url = "/login?error=true";

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		List<String> roles = new ArrayList<>();

		for (GrantedAuthority a : authorities)
		{
			roles.add(a.getAuthority());
		}

		if (roles.contains("ROLE_ADMIN"))
		{
			url = "/admin";
		}

		else if (roles.contains("ROLE_ISLERO"))
		{
			url = "/inicio";
		}

		else if (roles.contains("ROLE_GEROP"))
		{
			url = "/dashBoardClientes";
		}

		else if (roles.contains("ROLE_AUXPATIO"))
		{
			url = "/inicio";
		}

		return url;

	} 

}
