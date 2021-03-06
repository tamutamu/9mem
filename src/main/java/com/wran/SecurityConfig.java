package com.wran;

import com.wran.Service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    private static final String SALT = "salt";

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }

//    @Bean
//    public SpringSecurityDialect springSecurityDialect(){
//        return new SpringSecurityDialect();
//    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    private static final String[] PUBLIC_MATCHERS = {
            "/webjars/**",
            "/api/**",
            "/css/**",
            "/js/**",
            "/images/**",
            "/",
            "/about/**",
            "/contact/**",
            "/error/**",
            "/console/**",
            "/login",
            "/register",
            "/page/**",
            "/new",
            "/new/**",
            "/searchTag",
            "/tag/**",
            "/post/**",
            "/post",
            "/voteUp/**",
            "/voteDown/**",
            "/api/votesValue"
    };

    private static final String[] LOGGED_MATCHERS = {
            "/uploadPost",
            "/postUploadedSuccessfully",
            "/voteUp/**",
            "/voteDown/**",
            "/submitComment",
            "/api/userVote/**"

    };

    private static final String[] ADMIN_MATCHERS = {
            "/admin",
            "/accept/**",
            "/delete/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                .antMatchers(ADMIN_MATCHERS).hasRole("ADMIN")
                .antMatchers(LOGGED_MATCHERS).hasAnyRole("ADMIN", "USER")
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/");

    }
}
