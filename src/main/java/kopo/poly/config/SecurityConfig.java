package kopo.poly.config;

import kopo.poly.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info(this.getClass().getName() + ". PasswordEncoder Start!");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        log.info(this.getClass().getName() + ".filterChain Start!");

        http.csrf().disable();

        http.authorizeHttpRequests(authz -> authz
                                    .antMatchers("/user/**", "/notice/**").hasAnyAuthority("ROLE_USER")

                                    .antMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")

                                    .anyRequest().permitAll()
                )
                .formLogin(login -> login
                        .loginPage("/ss/loginForm")
                        .loginProcessingUrl("/ss/loginProc")
                        .usernameParameter("user_id")
                        .passwordParameter("password")
                        .successForwardUrl("/jwt/loginSuccess")
                        .failureForwardUrl("/jwt/loginFail")

                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                )

                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)

                // 세션 사용하지 않도록 설정함
                .sessionManagement(ss -> ss.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();

    }
}
