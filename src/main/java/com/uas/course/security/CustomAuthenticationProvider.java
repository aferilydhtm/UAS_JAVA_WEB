package com.uas.course.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.uas.course.services.servicesImp.CustomUserDetailsService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final PasswordEncoder passwordEncoder;
	private final CustomUserDetailsService customUserDetailsService;

	public CustomAuthenticationProvider(PasswordEncoder passwordEncoder,
			CustomUserDetailsService customUserDetailsService) {
		this.passwordEncoder = passwordEncoder;
		this.customUserDetailsService = customUserDetailsService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getName();
		String password = (String) authentication.getCredentials();
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

		if (passwordEncoder.matches(password, userDetails.getPassword())) {
			return new UsernamePasswordAuthenticationToken(email, null, userDetails.getAuthorities());
		}
		throw new BadCredentialsException("");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
