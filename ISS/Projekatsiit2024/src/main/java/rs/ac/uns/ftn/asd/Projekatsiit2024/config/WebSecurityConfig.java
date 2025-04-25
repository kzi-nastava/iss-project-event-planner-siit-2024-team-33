package rs.ac.uns.ftn.asd.Projekatsiit2024.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import rs.ac.uns.ftn.asd.Projekatsiit2024.security.auth.RestAuthenticationEntryPoint;
import rs.ac.uns.ftn.asd.Projekatsiit2024.security.auth.TokenAuthenticationFilter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.AuthenticationService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.TokenUtils;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
public class WebSecurityConfig {
	
	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	
	@Autowired
	private TokenUtils tokenUtils;
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new AuthenticationService();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors(Customizer.withDefaults());
		http.csrf((csrf) -> csrf.disable());
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(restAuthenticationEntryPoint));
		
        
        http.authorizeHttpRequests(request -> {
            request//.requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
                   //.requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
                   //.requestMatchers(new AntPathRequestMatcher("/api/events/**")).permitAll()
                   //.requestMatchers(new AntPathRequestMatcher("/api/offers/**")).permitAll()
                   //.requestMatchers(new AntPathRequestMatcher("/api/services/**")).permitAll()
                   //.requestMatchers(new AntPathRequestMatcher("/api/whoami")).hasRole("USER")
            .anyRequest().permitAll();
            ;
        });
        http.addFilterBefore(new TokenAuthenticationFilter(tokenUtils, userDetailsService()), UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(authenticationProvider());
		return http.build();
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
				//.requestMatchers(HttpMethod.POST, "/api/auth/login")
				.requestMatchers(HttpMethod.GET, "/", "/webjars/*", "/*.html", "favicon.ico",
		    			"/*/*.html", "/*/*.css", "/*/*.js");
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		configuration.setAllowedMethods(Arrays.asList("POST", "PUT", "GET", "OPTIONS", "DELETE", "PATCH"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		
		return source;
	}
	
}
