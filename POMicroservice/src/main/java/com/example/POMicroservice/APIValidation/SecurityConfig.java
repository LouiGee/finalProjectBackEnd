package com.example.POMicroservice.APIValidation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final FilterConfig filterConfig;

    // Inject filterConfig bean
    public SecurityConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }


    // Checks that the http request is authenticated before allowing access to endpoints
    // Http request travels through filterConfig beforehand to determine if request is authenticated
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enables CORS (cross-origin requests) with default settings
                .csrf(AbstractHttpConfigurer::disable) // Disables CSRF protection (common for APIs)
                .authorizeHttpRequests(req -> // Begin setting up route security rules
                        req
                                .requestMatchers("/api/po/**") // Apply the rule to any URL starting with /auth/po/
                                .authenticated() // Require authentication for those URLs
                                .requestMatchers("/api/temppo/**")
                                        .authenticated()
                )
                                .addFilterBefore(filterConfig, UsernamePasswordAuthenticationFilter.class); // run the filter before checking for authentication
        return http.build();
    }


    // To protect users, browsers block web pages from making requests to a different origin (domain) than the one that
    // served the web page, unless the server allows it.

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow only your frontend origin here
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // This will need to be changed in production

        // Allow credentials (cookies)
        configuration.setAllowCredentials(true);

        // Allowed HTTP methods from client
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allowed headers sent by the client
        // configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        // Optional: expose headers to client
        // configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Apply to all endpoints
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}




