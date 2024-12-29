package rs.ac.uns.ftn.asd.Projekatsiit2024.Controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2024.Model.AuthentifiedUser;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Repository.AuthentifiedUserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2024.Service.JwtService;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.LoginResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2024.dto.user.LoginUserDTO;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private AuthentifiedUserRepository userRepo;
	@Autowired
	private JwtService jwtService;
	
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody LoginUserDTO loginDTO){
		authManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDTO.email, loginDTO.password)
				);
		
		AuthentifiedUser user = userRepo.findByEmail(loginDTO.email).orElseThrow();
		
		String jwtToken = jwtService.generateToken(new User(user.getEmail(), user.getPassword(), new ArrayList<GrantedAuthority>()));
		
		return new ResponseEntity<LoginResponseDTO>(new LoginResponseDTO(jwtToken), HttpStatus.OK);
	}
}
