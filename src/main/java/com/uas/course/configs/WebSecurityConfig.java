package com.uas.course.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private final AuthenticationProvider authenticationProvider;

	public WebSecurityConfig(AuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
			throws Exception {
		MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
		// @formatter:off
		http.authorizeHttpRequests(
				request -> request.requestMatchers(mvcMatcherBuilder.pattern("/login/**")).permitAll()
								  .requestMatchers(mvcMatcherBuilder.pattern("/register")).permitAll()
								  .requestMatchers(mvcMatcherBuilder.pattern("/")).permitAll()
								  .requestMatchers(mvcMatcherBuilder.pattern("")).permitAll()
								  .requestMatchers(mvcMatcherBuilder.pattern("/assets/**")).permitAll()
								  .requestMatchers(mvcMatcherBuilder.pattern("/createUser")).permitAll()
								  .requestMatchers(mvcMatcherBuilder.pattern("/local/**")).authenticated()
								  .requestMatchers(mvcMatcherBuilder.pattern("/logout")).authenticated()
								  .requestMatchers(mvcMatcherBuilder.pattern("/dashboard")).authenticated()
								  .requestMatchers(mvcMatcherBuilder.pattern("/viewProfile")).authenticated()
								  .requestMatchers(mvcMatcherBuilder.pattern("/updateProfile")).authenticated()
								  .requestMatchers(mvcMatcherBuilder.pattern("/member/**")).hasRole("MEMBER")
								  .requestMatchers(mvcMatcherBuilder.pattern("/admin/**")).hasRole("ADMIN")
								  .anyRequest().authenticated()
		);
		http.authenticationProvider(authenticationProvider);
		http.formLogin(loginConfig -> loginConfig.loginPage("/login").defaultSuccessUrl("/dashboard", true).failureUrl("/login?error=true"));
		http.logout(logoutConfig -> logoutConfig.invalidateHttpSession(true));
		http.rememberMe(configs -> configs.tokenValiditySeconds(10 * 60));
		http.httpBasic(Customizer.withDefaults());
		return http.build();
	}
		// @formatter:on

}
