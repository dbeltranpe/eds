package com.eds.cogua.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eds.cogua.entity.Authority;
import com.eds.cogua.entity.Usuario;
import com.eds.cogua.repository.UsuarioRepository;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService 
{
	@Autowired
	UsuarioRepository userRepository;

	@SuppressWarnings("unchecked")
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{	
		Usuario appUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No existe usuario"));
		
		@SuppressWarnings("rawtypes")
		List grantList = new ArrayList();
		
		for (Authority authority: appUser.getAuthority()) 
		{
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
			grantList.add(grantedAuthority);
		}
		
		UserDetails user = (UserDetails) new User(appUser.getUsername(), appUser.getPassword(), grantList);
		return user;
	}
	
}