package servicos.gratitude.be_gratitude_capacita.JWTImplement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import servicos.gratitude.be_gratitude_capacita.JWTImplement.AutenticacaoService;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguracao {

    @Autowired
    private AutenticacaoEntryPoint autenticacaoEntryPoint;

    private final AutenticacaoService autenticacaoService;

    public SecurityConfiguracao(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    // CSV de origens permitidas; use "*" para permitir todas (sem credenciais)
    @Value("${app.cors.allowed-origins:*}")
    private String allowedOriginsCsv;

    private static final String[] URLS_PERMITIDAS = {
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/api/public/**",
            "/api/public/authenticate",
            "/webjars/**",
            "/v3/api-docs/**",
            "/actuator/*",
        "/usuarios/login/**",
            "/cargos/**",
            "/h2-console/**",
            "/api/cursos",
            "/error",
            "/uploads/**",
            "/proxy/image",
            "/matriculas/curso/*/participantes",
            "/relatorios/curso/*/engajamento",
            "/matriculas/curso/*/participantes",
            "/cursos/*/materiais",
            "/cursos/*/materiais/*",
            "/cursos/*/detalhes",
            "/cursos/*/publicar",
            "/materiais",
            "/materiais/**",
            "/debug/notificacao/**",
            "/materiais-alunos/finalizar-por-material/video/**",
            "/tentativas/*/*",
            "/participantes/*/avaliacoes",
            "/answersheet/**"
        ,"/exams/**",
        "/avaliacoes/**",
        "/notificacoes/**",
        "/api/email-notifications/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .cors(Customizer.withDefaults())
                .csrf(CsrfConfigurer<HttpSecurity>::disable)
        .authorizeHttpRequests(authorize -> authorize
            // URLs gerais permitidas (documentação, públicos, etc.)
            .requestMatchers(URLS_PERMITIDAS).permitAll()
            // Registro de usuário e login devem ser públicos para obter o primeiro token
            .requestMatchers(org.springframework.http.HttpMethod.POST, "/usuarios", "/usuarios/login").permitAll()
            // Permite CORS preflight (OPTIONS) para endpoints de auth sem exigir token
            .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/usuarios", "/usuarios/login").permitAll()
            // Demais endpoints exigem autenticação
            .anyRequest().authenticated())
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(autenticacaoEntryPoint))
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilterBean(), UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(
                new AutenticacaoProvider(autenticacaoService, passwordEncoder()));

        return authenticationManagerBuilder.build();
    }

    @Bean
    public AutenticacaoEntryPoint jwtAuthenticationEntryPointBean() {
        return new AutenticacaoEntryPoint();
    }

    @Bean
    public AutenticacaoFilter jwtAuthenticationFilterBean() {
        return new AutenticacaoFilter(autenticacaoService, jwtAuthenticationUtilBean());
    }

    // @Bean
    // public GerenciadorTokenJwt gerenciadorTokenJwt() {
    // return new GerenciadorTokenJwt();
    // }
    @Bean
    public GerenciadorTokenJwt jwtAuthenticationUtilBean() {
        return new GerenciadorTokenJwt();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracao = new CorsConfiguration();
        configuracao.applyPermitDefaultValues();
        // Origens permitidas (de propriedade app.cors.allowed-origins)
        if ("*".equals(allowedOriginsCsv.trim())) {
            // Usa padrões para permitir qualquer origem (sem credenciais)
            configuracao.setAllowedOriginPatterns(List.of("*"));
        } else {
            configuracao.setAllowedOrigins(Arrays.stream(allowedOriginsCsv.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList());
        }
        // Cabeçalhos comuns usados por Axios e JWT
        configuracao.setAllowedHeaders(List.of("*"));
        configuracao.setAllowedMethods(
                Arrays.asList(
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.OPTIONS.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.TRACE.name()));
        configuracao.setExposedHeaders(List.of(HttpHeaders.CONTENT_DISPOSITION, HttpHeaders.AUTHORIZATION));

        UrlBasedCorsConfigurationSource origem = new UrlBasedCorsConfigurationSource();
        origem.registerCorsConfiguration("/**", configuracao);
        return origem;
    }

        // Customize the HttpFirewall to allow some percent-encoded characters (e.g. %0A) that
        // StrictHttpFirewall blocks by default. Use with caution: permitting encoded control
        // characters can widen the attack surface if unvalidated URLs are forwarded to
        // downstream systems. Prefer sanitizing inputs when possible.
        @Bean
        public HttpFirewall allowUrlEncodedCharsHttpFirewall() {
            // Use DefaultHttpFirewall which is more permissive than StrictHttpFirewall.
            // This avoids rejecting requests that contain encoded control characters like %0A.
            return new DefaultHttpFirewall();
        }

        // Apply the custom firewall to WebSecurity so Spring Security uses it when building filter chains
        @Bean
        public WebSecurityCustomizer applyCustomHttpFirewall(HttpFirewall httpFirewall) {
            return web -> web.httpFirewall(httpFirewall);
        }

}
