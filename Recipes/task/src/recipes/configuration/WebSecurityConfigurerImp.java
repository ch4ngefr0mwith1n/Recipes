package recipes.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import recipes.service.UserService;

@EnableWebSecurity
public class WebSecurityConfigurerImp extends WebSecurityConfigurerAdapter {

    private UserService userDetailsService;

    @Autowired
    public WebSecurityConfigurerImp(UserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // moramo da odradimo "override" nad metodom koja prima "AuthenticationManagerBuilder"
    // nakon toga povezuje "UserDetailsService"
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(userDetailsService.getEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // sledeći endpoint-ovi su otvoreni svima
                // "/actuator/shutdown" nam je potreban radi testova
                .mvcMatchers(HttpMethod.POST, "/api/register", "/actuator/shutdown").permitAll()
                // svi ostali endpoint-ovi su omogućeni samo registrovanim korisnicima:
                .anyRequest().authenticated()
                .and()
                .csrf().disable() // radi H2 konzole
                .httpBasic(); // Http BASIC
    }

}
