package com.si.broker.config;

import com.si.broker.security.JwtAuthenticationFilter;
import com.si.broker.security.JwtAuthorizationFilter;
import com.si.broker.security.MyUserDetailsService;
import com.si.broker.services.IProviderService;
import com.si.broker.services.IUserService;
import com.si.broker.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MyUserDetailsService userDetailsService;
    private final IUserService userService;
    private final IProviderService providerService;

    @Autowired
    public SecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder, MyUserDetailsService userDetailsService, IUserService userService, IProviderService providerService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.providerService = providerService;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        String allRoutes = "/**";
        web.ignoring().antMatchers(Constants.ROLES_BASE_URL + allRoutes)
                .antMatchers(Constants.PROVIDERS_BASE_URL + "/register")
                .antMatchers(Constants.USERS_BASE_URL + "/register");
        //.antMatchers("/api/v1/teski/**");
    }

    //HTTP ZAHTEVI ZA IZOSTAVLJANJE OD AUTENTIFIKACIJE
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, Constants.ROLES_BASE_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter((authenticationManager()), userService, providerService))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsService))

                //turn off seesion creation
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
