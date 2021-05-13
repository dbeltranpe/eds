package com.eds.cogua.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PassGenerator 
{
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public PassGenerator()
	{
		bCryptPasswordEncoder = new BCryptPasswordEncoder(10);
		
	}
	
	public boolean match(String pPass1, String pPass2)
	{
		return bCryptPasswordEncoder.matches(pPass1, pPass2);
	}
	
	public String cifrar(String pTexto)
	{
		return bCryptPasswordEncoder.encode(pTexto);
	}
	
	public static void main(String[] args) 
	{
		PassGenerator p = new PassGenerator();
		System.out.println(p.cifrar("Eds2020*"));
	}

}