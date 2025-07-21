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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import rs.ac.uns.ftn.asd.Projekatsiit2024.security.auth.RestAuthenticationEntryPoint;
import rs.ac.uns.ftn.asd.Projekatsiit2024.security.auth.TokenAuthenticationFilter;
import rs.ac.uns.ftn.asd.Projekatsiit2024.service.auth.AuthenticationService;
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
		return new BCryptPasswordEncoder(12);
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
//                   .requestMatchers(new AntPathRequestMatcher("/api/events/**")).permitAll()
//                   .requestMatchers(new AntPathRequestMatcher("/api/offers/**")).permitAll()
                   //.requestMatchers(new AntPathRequestMatcher("/api/services/**")).permitAll()
                   //.requestMatchers(new AntPathRequestMatcher("/api/whoami")).hasRole("USER")
            
            	//authentication
		        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
		        .requestMatchers(HttpMethod.GET, "/api/auth/check-email").permitAll()

		        //event types
		        .requestMatchers(HttpMethod.GET, "api/eventTypes/active").permitAll()
		        .requestMatchers(HttpMethod.GET, "api/eventTypes").hasAuthority("ADMIN_ROLE")
		        .requestMatchers(HttpMethod.POST, "/api/eventTypes").hasAuthority("ADMIN_ROLE")
		        .requestMatchers(HttpMethod.PUT, "/api/eventTypes/*").hasAuthority("ADMIN_ROLE")
		        .requestMatchers(HttpMethod.PUT, "/api/eventTypes/*/activation").hasAuthority("ADMIN_ROLE")
		        .requestMatchers(HttpMethod.PUT, "/api/eventTypes/*/deactivation").hasAuthority("ADMIN_ROLE")
		        .requestMatchers(HttpMethod.GET, "api/eventTypes/exists").hasAuthority("ADMIN_ROLE")
		        
		        //events
		        .requestMatchers(HttpMethod.POST, "/api/events").hasAuthority("ORGANIZER_ROLE")
		        .requestMatchers(HttpMethod.POST, "/api/events/*/join").authenticated()
		        .requestMatchers(HttpMethod.GET, "/api/events/*").permitAll()
		        .requestMatchers(HttpMethod.GET, "/api/events/*/reports/details").permitAll()
		        .requestMatchers(HttpMethod.GET, "/api/events/*/reports/statistics").authenticated()
		        .requestMatchers(HttpMethod.GET, "/api/events").authenticated()
		        
		        //favorites
		        .requestMatchers(HttpMethod.GET, "/api/favorites/events").authenticated()
		        .requestMatchers(HttpMethod.GET, "/api/favorites/events/*/exists").permitAll()
		        .requestMatchers(HttpMethod.POST, "/api/favorites/events/*").authenticated()
		        .requestMatchers(HttpMethod.DELETE, "/api/favorites/events/*").authenticated()
		        
		        //offer categories
		        .requestMatchers("/api/offerCategories/**").authenticated()
		        .requestMatchers(HttpMethod.GET, "/api/offerCategories/available").permitAll()
		        .requestMatchers(HttpMethod.GET, "/api/event-types/*/offer-categories").hasAuthority("ORGANIZER_ROLE")
		        
		        .requestMatchers(HttpMethod.PUT, "/api/events").hasAuthority("ADMIN_ROLE")
		        
		        //offers
		        .requestMatchers("/api/offers/mine/**").authenticated()
		        
		        //service
		        .requestMatchers(HttpMethod.POST, "/api/services").authenticated()
		        .requestMatchers(HttpMethod.GET, "/api/services/**").permitAll()
		        .requestMatchers(HttpMethod.PUT, "/api/services/**").authenticated()
		        .requestMatchers(HttpMethod.DELETE, "/api/services/**").authenticated()
		        
		        .requestMatchers("/api/events/invitations/pending").authenticated()
		        .requestMatchers("/api/events/top5/authentified").authenticated()
		        .requestMatchers("/api/events/filter/authentified").authenticated()
		        .requestMatchers("/api/images/**").permitAll()
		        .requestMatchers("/api/providers/**").permitAll()
		        .requestMatchers("/api/users/signup").permitAll()
		        .requestMatchers("/api/users/me").authenticated()
		        .requestMatchers("/api/users/update/profile").authenticated()
		        .requestMatchers("/api/users/update/password").authenticated()
		        .requestMatchers("/api/users/terminate/profile").authenticated()
		        .requestMatchers("/h2-console/**").permitAll()
		        .requestMatchers("/api/events/*/budget/**").permitAll()
		        .requestMatchers("/api/chat/*/**").permitAll()
		        .requestMatchers("/api/offers/*/**").permitAll()
		        .requestMatchers("/api/events/**").permitAll()
		        .requestMatchers("api/events/paginated").permitAll()
		        .requestMatchers("/api/events/types/**").permitAll()
		        .requestMatchers("/api/events/*/invitations/**").permitAll()
		        .requestMatchers("/api/notifications/**").permitAll()
		        .requestMatchers("/api/offerCategories/**").permitAll()
		        .requestMatchers("/api/offers/**").permitAll()
		        .requestMatchers("/api/user/*/offer-prices/**").permitAll()
		        .requestMatchers("/api/products/**").permitAll()
		        .requestMatchers("/api/ratings/**").permitAll()
		        .requestMatchers("/api/reports/**").permitAll()
		        .requestMatchers("/api/services/**").permitAll()
		        .requestMatchers("/api/services/*/reservations/**").permitAll()
		        .requestMatchers("/api/events/filter/unauthentified").permitAll()
		        .requestMatchers("/api/events/filter/authentified").authenticated()
		        .requestMatchers("/api/events/filter/unauthentified").permitAll()
		        .anyRequest().permitAll();
        });
        http.addFilterBefore(new TokenAuthenticationFilter(tokenUtils, userDetailsService()), UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(authenticationProvider());
        
        http.headers(headers -> headers
                .addHeaderWriter((request, response) ->
                    response.addHeader("Content-Security-Policy", "frame-ancestors 'self'")
                )
            );
        
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

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
