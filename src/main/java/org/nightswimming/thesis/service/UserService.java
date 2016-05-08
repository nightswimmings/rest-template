package org.nightswimming.thesis.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nightswimming.thesis.domain.Role;
import org.nightswimming.thesis.domain.User;
import org.nightswimming.thesis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username);
		if (user == null) throw new UsernameNotFoundException("User "+username+" not found");
		return new CustomUserDetails(user);
	}

	//Spring-Security UserDetails Implementation
	public static class CustomUserDetails implements UserDetails {

		private static final long serialVersionUID = 1L;
		private Collection<? extends GrantedAuthority> authorities;
		private String password;
		private String username;
		
		public CustomUserDetails(User user) {
			this.username = user.getUserName();
			this.password = user.getPassword();
			this.authorities = translate(user.getRoles());
		}

		private Collection<? extends GrantedAuthority> translate(List<Role> roles) {
			List<GrantedAuthority> authorities = new ArrayList<>();
			for (Role role : roles) {
				String name = role.getName().toUpperCase();
				if (!name.startsWith("ROLE_")) {
					name = "ROLE_" + name;
				}
				authorities.add(new SimpleGrantedAuthority(name));
			}
			return authorities;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return authorities;
		}

		@Override public String  getPassword()             { return password; }
		@Override public String  getUsername()             { return username; }			
		@Override public boolean isAccountNonExpired()     { return true; }
		@Override public boolean isAccountNonLocked()      { return true; }
		@Override public boolean isCredentialsNonExpired() { return true; }
		@Override public boolean isEnabled()               { return true; }
	}
}
