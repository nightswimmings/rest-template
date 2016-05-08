package org.nightswimming.thesis.security;

//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.ImportResource;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@EnableWebSecurity
//@ImportResource("classpath:META-INF/spring/securityConfig.xml")
//public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {
//}


//@Configuration
//@EnableAuthorizationServer
//public class OAuth2Config extends AuthorizationServerConfigurerAdapter {
//
//	@Autowired
//	private AuthenticationManager authenticationManager;
//
//	@Override
//	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//		endpoints.authenticationManager(authenticationManager);
//	}
//
//	@Override
//	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//		security.checkTokenAccess("isAuthenticated()");
//	}
//
//	@Override
//	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//		clients.inMemory().withClient("my-trusted-client")
//				.authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
//				.authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT").scopes("read", "write", "trust")
//				.resourceIds("oauth2-resource").accessTokenValiditySeconds(600).and()
//				.withClient("my-client-with-registered-redirect").authorizedGrantTypes("authorization_code")
//				.authorities("ROLE_CLIENT").scopes("read", "trust").resourceIds("oauth2-resource")
//				.redirectUris("http://anywhere?key=value").and().withClient("my-client-with-secret")
//				.authorizedGrantTypes("client_credentials", "password").authorities("ROLE_CLIENT").scopes("read")
//				.resourceIds("oauth2-resource").secret("secret");
//	}
//
//	@Autowired
//	public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repository) throws Exception {
//		if (repository.count()==0) {
//			repository.save(new User("user", "password", Arrays.asList(new Role("USER"))));
//		}
//		builder.userDetailsService(userDetailsService(repository));
//	}
//	
//	public UserDetailsService userDetailsService(final UserRepository repository) {
//		return new UserDetailsService() {		
//			@Override
//			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//				return new UserService.CustomUserDetails(repository.findByUsername(username));
//			}
//		};
//	}
//
//}
