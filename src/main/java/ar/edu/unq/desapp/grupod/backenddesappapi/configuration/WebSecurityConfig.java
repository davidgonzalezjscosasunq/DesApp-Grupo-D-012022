package ar.edu.unq.desapp.grupod.backenddesappapi.configuration;

import ar.edu.unq.desapp.grupod.backenddesappapi.controller.filters.JwtRequestFilter;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.UserRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.CryptoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.bypassAuthentication}")
    private boolean bypassAuthentication;

    @Autowired
    private CryptoUserDetailsService cryptoUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (bypassAuthentication) {
            http
                    .csrf().disable()
                    .authorizeRequests().anyRequest().permitAll();
        }
        else {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/auth/register").permitAll()
                    .antMatchers("/auth/login").permitAll()
                    .anyRequest().authenticated()
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(cryptoUserDetailsService);
    }
}
