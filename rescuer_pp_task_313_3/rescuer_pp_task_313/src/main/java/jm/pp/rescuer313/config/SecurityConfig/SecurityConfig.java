package jm.pp.rescuer313.config.SecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final RestAuthenticationSuccessHandler successHandler;
    private final RestAuthenticationFailureHandler failureHandler;
    private final RestLogoutSuccessHandler logoutSuccessHandler;
    private final RestEntryPoint restEntryPoint;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(RestAuthenticationSuccessHandler successHandler, RestAuthenticationFailureHandler failureHandler,
                          RestLogoutSuccessHandler logoutSuccessHandler, RestEntryPoint restEntryPoint, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.restEntryPoint = restEntryPoint;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static BCryptPasswordEncoder encoderReal() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/static/**").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .loginProcessingUrl("/api/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .usernameParameter("j_username")
                .passwordParameter("j_password");

        http.exceptionHandling()
                .authenticationEntryPoint(restEntryPoint);

        http.logout()
                .permitAll()
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/api/logout"))
                .logoutSuccessHandler(logoutSuccessHandler)
                .and().csrf().disable();
    }

    @Autowired
    public void configGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoderReal());
    }


}


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers("/login").anonymous()
//                .antMatchers("/admin").hasAnyRole("ADMIN")
//                .antMatchers("index").permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/login")
//                .successHandler(successUserHandler)
//                .failureUrl("/login?error=true")
//                .usernameParameter("j_username")
//                .passwordParameter("j_password")
//                .and()
//                .exceptionHandling()
//                .accessDeniedPage("/exception");
//
//        http.logout()
//                .permitAll()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                // указываем URL при удачном логауте
//                .logoutSuccessUrl("/login")
//                .and().csrf().disable();
//
//    }





