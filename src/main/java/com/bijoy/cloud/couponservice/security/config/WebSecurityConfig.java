package com.bijoy.cloud.couponservice.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class WebSecurityConfig {
    @Value("${coupons-ui.domain}")
    private String couponsUIDomain;
    @Value("${coupons-ui.method}")
    private String couponsUIMethod;
    @Value("${coupons-ui.header}")
    private String couponsUIHeader;
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.httpBasic(Customizer.withDefaults());
//        http.formLogin(Customizer.withDefaults());
        http.authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers(HttpMethod.GET, "/couponapi/coupons/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/showGetCoupon", "/getCoupon", "/couponDetails")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/getCoupon")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/showCreateCoupon", "/createCoupon", "/createResponse")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/couponapi/coupons", "/saveCoupon")
                        .hasRole("ADMIN")
                        .requestMatchers("/", "/login", "/showReg", "/registerUser")
                        .permitAll());
        http.logout(logout -> logout.logoutSuccessUrl("/"));
//        http.csrf(csrf -> csrf.disable());
        //enabling csrf
        http.csrf(csrf -> {
            csrf.ignoringRequestMatchers("/getCoupon", "/couponapi/coupons/**");
            RequestMatcher requestMatchers = new RegexRequestMatcher("/registerUser", "POST");
            csrf.ignoringRequestMatchers(requestMatchers);
        });
        http.securityContext(securityContext -> securityContext.requireExplicitSave(true));
        http.cors(corsCustomizer -> {
            CorsConfigurationSource configurationSource = (request) -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(List.of(couponsUIDomain));
                corsConfiguration.setAllowedMethods(List.of(couponsUIMethod
                        .replace(" ", "").split(",")));
                corsConfiguration.setAllowedHeaders(List.of("couponsUIHeader"));
                return corsConfiguration;
            };
            corsCustomizer.configurationSource(configurationSource);
        });
        return http.build();
    }
}
