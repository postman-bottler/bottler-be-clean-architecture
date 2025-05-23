package online.bottler.config;

import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import online.bottler.user.adapter.in.web.auth.JwtAccessDeniedHandler;
import online.bottler.user.adapter.in.web.auth.JwtAuthenticationEntryPoint;
import online.bottler.user.adapter.in.web.auth.JwtFilter;
import online.bottler.user.adapter.in.web.auth.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/.well-known/acme-challenge/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/oauth/**").permitAll()
                        .requestMatchers("/user/signup").permitAll()
                        .requestMatchers("/user/developer").permitAll()
                        .requestMatchers("/user/duplicate-check/**").permitAll()
                        .requestMatchers("/user/email/**").permitAll()
                        .requestMatchers("/map/guest/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/swagger-ui/index.html").permitAll()
                        .requestMatchers("/v3/api-docs").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/v3/**").permitAll()
                        .requestMatchers("/healthcheck").permitAll()
                        .requestMatchers(HttpMethod.GET, "/labels/first-come").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/labels/first-come").permitAll()
                        .requestMatchers(HttpMethod.POST, "/labels").permitAll()
                        .requestMatchers("/monitor/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/letters").hasAnyAuthority("USER", "DEVELOPER")
                        .requestMatchers(HttpMethod.POST, "/map/public").hasAnyAuthority("USER", "DEVELOPER")
                        .requestMatchers(HttpMethod.POST, "/map/target").hasAnyAuthority("USER", "DEVELOPER")
                        .requestMatchers(HttpMethod.POST, "/letters/replies/{letterId}").hasAnyAuthority("USER", "DEVELOPER")
                        .requestMatchers(HttpMethod.POST, "/map/reply").hasAnyAuthority("USER", "DEVELOPER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); // 모든 도메인 허용
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Collections.singletonList("*")); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 인증 정보를 포함한 요청 허용
        configuration.setMaxAge(3600L); // 캐싱 시간 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
