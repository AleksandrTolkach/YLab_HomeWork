package by.toukach.walletservice.config;

import by.toukach.walletservice.security.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурационный класс для настройки безопасности приложения.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private static final String LOG_IN_PATH = "/auth/login";
  private static final String SIGN_UP_PATH = "/auth/sign-up";
  private static final String[] OPEN_API_PATH = new String[]{
      "/v3/api-docs/**",
      "/swagger-ui/**",
      "/v2/api-docs/**",
      "/swagger-resources/**"
  };

  private final UserDetailsService userDetailsService;
  private final AuthenticationEntryPoint authenticationEntryPoint;
  private final JwtTokenFilter jwtTokenFilter;

  /**
   * Метод для создания бина AuthenticationProvider, который использует пользователей из БД для
   * аутентификации.
   *
   * @return запрашиваемый AuthenticationProvide.
   */
  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(userDetailsService);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    return daoAuthenticationProvider;
  }

  /**
   * Метод для создания бина AuthenticationManager, который упралвяет аутентикикациями.
   *
   * @param authConfig конфигурация.
   * @return запрашиваемый менеджер.
   * @throws Exception ошибка при загрузке менеджера.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }

  /**
   * Метод для создания бина PasswordEncoder, который используется для шифрования пароля.
   *
   * @return запрашиваемый PasswordEncoder.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Метод для создания бина SecurityFilterChain, в котором указаны параметры фильтрации запросов.
   *
   * @param http объект безопасности.
   * @return запрашиваемый SecurityFilterChain.
   * @throws Exception ошибка при создании фильтра.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(configurer ->
            configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(configurer ->
            configurer.authenticationEntryPoint(authenticationEntryPoint)
        )
        .authorizeHttpRequests(registry ->
                registry
                    .requestMatchers(OPEN_API_PATH).permitAll()
                    .requestMatchers(LOG_IN_PATH).permitAll()
                    .requestMatchers(SIGN_UP_PATH).permitAll()
                    .anyRequest().authenticated()
        )
        .headers(configurer -> configurer.frameOptions(FrameOptionsConfig::sameOrigin))
        .authenticationProvider(daoAuthenticationProvider())
        .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
