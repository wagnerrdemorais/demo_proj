package com.accountify.demo.config.security;

import com.accountify.demo.repository.UserRepository;
import com.accountify.demo.service.AutenticationService;
import com.accountify.demo.service.TokenService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AutenticationService autentcationService;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final CsrfTokenRepository jwtCsrfTokenRepository;

    public SecurityConfiguration(AutenticationService autentcationService, TokenService tokenService, UserRepository userRepository, CsrfTokenRepository jwtCsrfTokenRepository) {
        this.autentcationService = autentcationService;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.jwtCsrfTokenRepository = jwtCsrfTokenRepository;
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autentcationService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        String[] ignoreCsrfAntMatchers = {
                "/dynamic-builder-compress",
                "/dynamic-builder-general",
                "/dynamic-builder-specific",
                "/set-secrets"
        };

        httpSecurity.csrf()
                .csrfTokenRepository(jwtCsrfTokenRepository)
                .ignoringAntMatchers(ignoreCsrfAntMatchers)
                .and().authorizeRequests()
                .antMatchers("/**")
                .permitAll();
                //.authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/query").hasRole("ADMIN")
//                .antMatchers(HttpMethod.POST, "/auth").permitAll()
//                .antMatchers(HttpMethod.POST, "/uploadFile").authenticated()
//                .antMatchers(HttpMethod.GET, "/zipFile").authenticated()
//                .antMatchers(HttpMethod.GET, "/files").authenticated()
//                .antMatchers(HttpMethod.GET, "/split").authenticated()
//                .anyRequest().authenticated()
//                .and().formLogin(form -> form.loginPage("/login").permitAll())
//                    .csrf()
//                    .ignoringAntMatchers(ignoreCsrfAntMatchers)
//                    .csrfTokenRepository(jwtCsrfTokenRepository)
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and().addFilterBefore(new AutenticationTokenFilter(tokenService, userRepository), UsernamePasswordAuthenticationFilter.class)
//                .requestCache((requestCache) -> requestCache.disable());

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
    }

}
