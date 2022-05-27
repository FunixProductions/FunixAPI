package fr.funixgaming.api.server.user.components;

import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.server.user.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final JwtTokenFilter jwtTokenFilter;

    public WebSecurity(UserService userService,
                       JwtTokenFilter jwtTokenFilter) {
        this.userService = userService;
        this.jwtTokenFilter = jwtTokenFilter;

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http = http.cors().and().csrf().disable();

        //Set unauthorized requests exception handler
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                ex.getMessage()
                        )
                )
                .and();

        http.authorizeRequests()
                .antMatchers("/funixbot/**").hasAuthority(UserRole.MODERATOR.getRole())
                .antMatchers("/mail/**").hasAuthority(UserRole.MODERATOR.getRole())

                .antMatchers(HttpMethod.POST, "/user/register").permitAll()
                .antMatchers(HttpMethod.POST, "/user/login").permitAll()
                .antMatchers(HttpMethod.GET, "/user/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "/user/valid").authenticated()
                .antMatchers("/user/**").hasAuthority(UserRole.ADMIN.getRole())

                .anyRequest().authenticated();

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return new FunixApiAuth(userService);
    }

}
