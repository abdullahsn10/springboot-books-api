package com.abdul.postdb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// tells spring to look in this configuration class for web security
@EnableWebSecurity
public class SecurityConfig {

    // tells spring to look for these particular values in the configuration files/environment
//    @Value("${devtiro.books.api-key.key}")
//    private String principalRequestHeader; // the key of the Header
//
//    @Value("${devtiro.books.api-key.value}")
//    private String principalRequestValue; // the value of that header


    // create a user detail service, which is the piece of code
    // we would typically need to tell spring about users
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        // we can create as many users as we need
        manager.createUser(User.withDefaultPasswordEncoder()
                .username("user1")
                .password("password")
                .roles("USER")
                .build());

        manager.createUser(User.withDefaultPasswordEncoder()
                .username("user2")
                .password("password")
                .roles("MANAGER")
                .build());

        manager.createUser(User.withDefaultPasswordEncoder()
                .username("user3")
                .password("password")
                .roles("USER", "MANAGER")
                .build());

        manager.createUser(User.withDefaultPasswordEncoder()
                .username("user4")
                .password("password")
                .roles("ADMIN")
                .build());

        return manager;
    }

    // our Api filter chain, which will take the value of the header in the request,
    // and then match it with the expected value of it, if they are equals then the user
    // is authenticated, otherwise, the user is not authenticated and can't hit that API
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // create an instance of the ApiKeyAuthFilter, and pass the key Header

        /* API Key Authentication Code */
        // -------------------------------------------------------------------
//        final ApiKeyAuthFilter filter = new ApiKeyAuthFilter(principalRequestHeader);
//
//        // Functional Interface pattern
//        // Implement authenticate method in this way which will implement the filtering process
//        // checking the value of that particular header in the request
//        filter.setAuthenticationManager((Authentication authentication) -> {
//            final String principal = (String) authentication.getPrincipal();
//
//            // if the value does not match the expected one, then throw exception
//            // and that user is not authenticated
//            if(!principalRequestValue.equals(principal)) {
//                // spring knows how to handle this exception
//                throw new BadCredentialsException("Invalid API Key");
//            }
//            // user authenticated
//            authentication.setAuthenticated(true);
//            return authentication;
//        });
//
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .csrf().disable()
//                .addFilter(filter).authorizeHttpRequests()
//                .anyRequest().authenticated();

        // -----------------------------------------------------------------------

        // 1) Restrict URLs based on Roles (authorize Http Requests)
        http.authorizeHttpRequests( configurer ->
                configurer
                        // request matchers for Read Many/One endpoints
                        .requestMatchers(HttpMethod.GET, "/authors").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/authors/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/books").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/books/**").hasRole("USER")
                        // request matchers for Create/Update endpoints
                        .requestMatchers(HttpMethod.POST, "/authors").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/books/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/authors/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PATCH, "/books/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PATCH, "/authors/**").hasRole("MANAGER")
                        // request matchers for Delete endpoints
                        .requestMatchers(HttpMethod.DELETE, "/books/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/authors/**").hasRole("ADMIN")
        );

        // use https Basic auth
        http.httpBasic(Customizer.withDefaults());

        // disable csrf
        http.csrf().disable();

        return http.build();

    }



}
