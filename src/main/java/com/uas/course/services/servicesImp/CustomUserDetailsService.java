package com.uas.course.services.servicesImp;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.uas.course.models.Person;
import com.uas.course.repositories.PersonRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

	private final PersonRepository personRepository;

	public CustomUserDetailsService(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Person person = personRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("email " + username + " not found!"));
		return User.withUsername(person.getEmail()).password(person.getPassword()).roles(person.getRole().getName())
				.build();
	}

}
