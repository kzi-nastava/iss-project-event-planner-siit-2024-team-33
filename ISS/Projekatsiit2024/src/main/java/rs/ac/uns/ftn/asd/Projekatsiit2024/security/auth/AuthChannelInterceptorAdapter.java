package rs.ac.uns.ftn.asd.Projekatsiit2024.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import rs.ac.uns.ftn.asd.Projekatsiit2024.utils.TokenUtils;

@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor{
	@Autowired
    private TokenUtils jwtTokenProvider; // your JWT utility class
	@Autowired
	@Qualifier("userDetailsService")
	private UserDetailsService userDetailService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorization = accessor.getFirstNativeHeader("Authorization");

            if (authorization != null && authorization.startsWith("Bearer ")) {
                String token = authorization.substring(7);
                
                String username = jwtTokenProvider.getUsernameFromToken(token);
                UserDetails userDetails = userDetailService.loadUserByUsername(username);
                
                if (userDetails.isEnabled() || 
					    userDetails.isAccountNonLocked() || 
					    userDetails.isAccountNonExpired() || 
					    userDetails.isCredentialsNonExpired()) {
						if (jwtTokenProvider.validateToken(token, userDetails)) {
							
							TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
							authentication.setToken(token);
							SecurityContextHolder.getContext().setAuthentication(authentication);
							accessor.setUser(authentication);
			                System.out.println("User set in accessor: " + authentication.getName());

						}
					}
                
            }
        }
        return message;
    }
}
