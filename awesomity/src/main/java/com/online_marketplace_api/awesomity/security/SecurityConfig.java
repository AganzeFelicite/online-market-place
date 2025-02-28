package com.online_marketplace_api.awesomity.security;

import com.online_marketplace_api.awesomity.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityUtils securityUtils() {
        return new SecurityUtils();
    }

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    private static final String[] PUBLIC_URLS = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/api/v1/users/verify",
            "/api/v1/users/register"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // Public URLs
                        .requestMatchers(PUBLIC_URLS).permitAll()

                        // Specific Role-based access for GET requests
                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/**").hasAnyRole(Role.SELLER.name(), Role.ADMIN.name(), Role.BUYER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").hasAnyRole(Role.SELLER.name(), Role.ADMIN.name(), Role.BUYER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/reviews/**").hasAnyRole(Role.SELLER.name(), Role.ADMIN.name(), Role.BUYER.name())


                        // Specific Role-based access for POST requests
                        .requestMatchers(HttpMethod.POST, "/api/v1/orders/**").hasAnyRole(Role.BUYER.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasAnyRole(Role.SELLER.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/reviews/**").hasAnyRole(Role.BUYER.name(), Role.ADMIN.name())

                        // Specific Role-based access for PUT requests
                        .requestMatchers(HttpMethod.PUT, "/api/v1/orders/**").hasAnyRole(Role.BUYER.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasAnyRole(Role.SELLER.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/reviews/**").hasAnyRole(Role.BUYER.name(), Role.ADMIN.name())

                        // Specific Role-based access for DELETE requests
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/orders/**").hasAnyRole(Role.BUYER.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasAnyRole(Role.SELLER.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/reviews/**").hasAnyRole(Role.BUYER.name(), Role.ADMIN.name())



                        // Roles that can perform GET, POST, PUT, DELETE (e.g., sellers and admins)
                        .requestMatchers("/api/v1/orders/**", "/api/v1/products/**", "/api/v1/reviews/**")
                        .hasAnyRole(Role.SELLER.name(), Role.ADMIN.name())

                        // Restrict certain endpoints to admins
                        .requestMatchers("/api/v1/users/**").hasRole(Role.ADMIN.name())

                        // Restrict all other requests to authenticated users
                        .anyRequest().authenticated())

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
