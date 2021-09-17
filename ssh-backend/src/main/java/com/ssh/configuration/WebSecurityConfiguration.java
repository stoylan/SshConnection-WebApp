package com.ssh.configuration;

import com.ssh.service.auth.JwtTokenFilter;
import com.ssh.service.auth.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final JwtTokenFilter jwtTokenFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    public WebSecurityConfiguration(JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Autowired
    public void configurePasswordEncoder(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(getBrycptPasswordEncoder());
    }

    @Bean
    BCryptPasswordEncoder getBrycptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable()
                .authorizeRequests().antMatchers(
                        "/v2/api-docs",
                        "/api/*",
                        "/api/ssh/isConnect",
                        "/api/ssh/getEthernet",
                        "/api/ssh/getLocal",
                        "/api/ssh/add",
                        "/api/ssh/getSsh",
                        "/api/ssh/getSshById",
                        "/api/ssh/delete",
                        "/api/ssh/setCommand",
                        "/api/command/getCommandByCredentialId",
                        "/swagger-resources/**",
                        "/swagger-ui/**",
                        "/webjars/**")
                .permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }


}
