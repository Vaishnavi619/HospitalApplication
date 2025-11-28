package com.example.demo.jwt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity 
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors() 
            .and()
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/auth/**", "/api/users/**").permitAll()
                .requestMatchers("/api/doctors/**").hasAnyRole("ADMIN","RECEPTIONIST","DOCTOR","SUPER_ADMIN")
                .requestMatchers("/api/patients/**").hasAnyRole("RECEPTIONIST", "ADMIN","SUPER_ADMIN")
                .requestMatchers("/api/appointments/**").hasAnyRole("RECEPTIONIST", "ADMIN", "DOCTOR","PATIENT","SUPER_ADMIN")
                .requestMatchers("/api/medicines/**").hasAnyRole("ADMIN","DOCTOR","SUPER_ADMIN","RECEPTIONIST")
                .requestMatchers("/api/prescriptions/**").hasAnyRole("DOCTOR","PATIENT","RECEPTIONIST","SUPER_ADMIN")
                .requestMatchers("/api/bills/**").hasAnyRole("RECEPTIONIST", "ADMIN","PATIENT","SUPER_ADMIN")
                .requestMatchers("/api/payments/create-order/**").hasRole("PATIENT")
                .requestMatchers("/api/reports/**").hasRole("SUPER_ADMIN")
                .requestMatchers("/api/doctor-availability/**").hasAnyRole("DOCTOR","RECEPTIONIST","ADMIN","SUPER_ADMIN")
                .requestMatchers("/api/invoices/**").hasRole("PATIENT")
                .requestMatchers("/api/patients/search/**").hasAnyRole("RECEPTIONIST", "ADMIN", "DOCTOR","PATIENT","SUPER_ADMIN")
                .requestMatchers("/api/bookings/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}

/*
 * import lombok.RequiredArgsConstructor; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.security.authentication.AuthenticationManager; import
 * org.springframework.security.authentication.AuthenticationProvider; import
 * org.springframework.security.authentication.dao.DaoAuthenticationProvider;
 * import
 * org.springframework.security.config.annotation.authentication.configuration.
 * AuthenticationConfiguration; import
 * org.springframework.security.config.annotation.method.configuration.
 * EnableMethodSecurity; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.http.SessionCreationPolicy; import
 * org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import
 * org.springframework.security.crypto.password.PasswordEncoder; import
 * org.springframework.security.web.SecurityFilterChain; import
 * org.springframework.security.web.authentication.
 * UsernamePasswordAuthenticationFilter;
 *
 * @Configuration
 *
 * @EnableMethodSecurity // enables @PreAuthorize
 *
 * @RequiredArgsConstructor public class SecurityConfig {
 *
 * @Autowired private JwtAuthenticationFilter jwtAuthFilter;
 *
 * @Autowired private UserDetailsServiceImpl userDetailsService;
 *
 * @Bean public PasswordEncoder passwordEncoder() { return new
 * BCryptPasswordEncoder(); }
 *
 * @Bean public AuthenticationProvider authenticationProvider() {
 * DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
 * authProvider.setUserDetailsService(userDetailsService);
 * authProvider.setPasswordEncoder(passwordEncoder()); return authProvider; }
 *
 * @Bean public AuthenticationManager
 * authenticationManager(AuthenticationConfiguration config) throws Exception {
 * return config.getAuthenticationManager(); }
 *
 * @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http)
 * throws Exception { return http .csrf(csrf -> csrf.disable())
 * .authorizeHttpRequests(auth -> auth .requestMatchers("/auth/**", "/genToken",
 * "/api/users/**", "/api/doctors/**",
 * "/api/patients/**","/api/medicines/**").permitAll() // allow login/register
 * .requestMatchers("/api/appointments/**").hasRole("RECEPTIONIST") // only
 * RECEPTIONIST can manage appointments .anyRequest().authenticated() )
 * .sessionManagement(sess ->
 * sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
 * .authenticationProvider(authenticationProvider())
 * .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
 * .build(); } }
 *
 * //untill receptionist this code works properly
 *
 *
 */

//import com.example.demo.jwt.config.JwtAuthenticationFilter;

/*
 * import lombok.RequiredArgsConstructor;
 *
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.security.authentication.AuthenticationManager; import
 * org.springframework.security.authentication.AuthenticationProvider; import
 * org.springframework.security.authentication.dao.DaoAuthenticationProvider;
 * import
 * org.springframework.security.config.annotation.authentication.configuration.
 * AuthenticationConfiguration; import
 * org.springframework.security.config.annotation.method.configuration.
 * EnableMethodSecurity; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.http.SessionCreationPolicy;
 * //import org.springframework.security.core.userdetails.UserDetailsService;
 * import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 * import org.springframework.security.crypto.password.PasswordEncoder; import
 * org.springframework.security.web.SecurityFilterChain; import
 * org.springframework.security.web.authentication.
 * UsernamePasswordAuthenticationFilter;
 *
 * @Configuration
 *
 * @EnableMethodSecurity // enables @PreAuthorize("hasRole('ADMIN')")
 *
 * @RequiredArgsConstructor public class SecurityConfig {
 *
 * @Autowired private JwtAuthenticationFilter jwtAuthFilter;
 *
 * @Autowired private UserDetailsServiceImpl userDetailsService;
 *
 * // 1. Define Password Encoder Bean
 *
 * @Bean public PasswordEncoder passwordEncoder() { return new
 * BCryptPasswordEncoder(); }
 *
 * // 2. AuthenticationProvider
 *
 * @Bean public AuthenticationProvider authenticationProvider() {
 * DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
 * authProvider.setUserDetailsService(userDetailsService);
 * authProvider.setPasswordEncoder(passwordEncoder()); return authProvider; }
 *
 * // 3. AuthenticationManager Bean
 *
 * @Bean public AuthenticationManager
 * authenticationManager(AuthenticationConfiguration config) throws Exception {
 * return config.getAuthenticationManager(); }
 *
 * // 4. Security Filter Chain
 *
 * @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http)
 * throws Exception { return http .csrf(csrf -> csrf.disable())
 * .authorizeHttpRequests(auth -> auth .requestMatchers("/auth/**",
 * "/api/users","/api/users/{userId}","/api/doctors","/api/doctors/{doctorId}",
 * "/api/patients","/api/patients/{patientId}","/api/appointments",
 * "/api/appointments/{appoiintmentId}").permitAll() // allow login/register
 * .anyRequest().authenticated() // other endpoints require auth )
 * .sessionManagement(sess ->
 * sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
 * .authenticationProvider(authenticationProvider())
 * .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
 * .build(); } }
 *
 *
 */

/*
 * import jakarta.servlet.http.HttpServletResponse;
 *
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration;
 *
 * import org.springframework.security.authentication.AuthenticationManager;
 * import
 * org.springframework.security.config.annotation.authentication.configuration.
 * AuthenticationConfiguration; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.http.SessionCreationPolicy; import
 * org.springframework.security.core.userdetails.UserDetailsService; import
 * org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import
 * org.springframework.security.crypto.password.PasswordEncoder; import
 * org.springframework.security.web.SecurityFilterChain;
 */

/*
 * @Configuration
 *
 * public class SecurityConfig {
 *
 * @Autowired private UserDetailsService userDetailsService;
 *
 * @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http)
 * throws Exception { return http .csrf(csrf -> csrf.disable())
 * .authorizeHttpRequests(auth -> auth .requestMatchers("/auth/login",
 * "/api/users").permitAll() // 👈 allow login + register
 * .anyRequest().authenticated() )
 *
 *
 * .userDetailsService(userDetailsService) .sessionManagement(session ->
 * session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
 * .exceptionHandling(e -> e .authenticationEntryPoint((req, res, ex) ->
 * res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")) )
 * .build(); }
 *
 * @Bean public PasswordEncoder passwordEncoder() { return new
 * BCryptPasswordEncoder(); }
 *
 * @Bean public AuthenticationManager
 * authenticationManager(AuthenticationConfiguration config) throws Exception {
 * return config.getAuthenticationManager(); } }
 */
