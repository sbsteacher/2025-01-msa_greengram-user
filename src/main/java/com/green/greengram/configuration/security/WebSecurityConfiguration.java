package com.green.greengram.configuration.security;

import com.green.greengram.configuration.constants.ConstOAuth2;
import com.green.greengram.configuration.security.oauth.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final UserHeaderAuthenticationFilter userHeaderAuthenticationFilter;

    private final Oauth2AuthenticationRequestBasedOnCookieRepository repository;
    private final Oauth2AuthenticationSuccessHandler authenticationSuccessHandler;
    private final Oauth2AuthenticationFailureHandler authenticationFailureHandler;
    private final MyOauth2UserService myOauth2UserService;
    private final ConstOAuth2 constOAuth2;

    @Bean //스프링이 메소드 호출을 하고 리턴한 객체의 주소값을 관리한다. (빈등록)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //시큐리티가 세션을 사용하지 않는다.
                .httpBasic(h -> h.disable()) //SSR(Server Side Rendering)이 아니다. 화면을 만들지 않기 때문에 비활성화 시킨다. 시큐리티 로그인창 나타나지 않을 것이다.
                .formLogin(form -> form.disable()) //SSR(Server Side Rendering)이 아니다. 폼로그인 기능 자체를 비활성화
                .csrf(csrf -> csrf.disable()) //SSR(Server Side Rendering)이 아니다. 보안관련 SSR 이 아니면 보안이슈가 없기 때문에 기능을 끈다.
                .addFilterBefore(userHeaderAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .oauth2Login(oauth2 -> oauth2.authorizationEndpoint( auth -> auth.baseUri(constOAuth2.baseUri)
                                             .authorizationRequestRepository(repository)
                                       )
                                       .redirectionEndpoint( redirection -> redirection.baseUri(constOAuth2.redirectionBaseUri) )
                                       .userInfoEndpoint( userInfo -> userInfo.userService(myOauth2UserService) )
                                       .successHandler( authenticationSuccessHandler )
                                       .failureHandler( authenticationFailureHandler )
                )
                .addFilterBefore(new Oauth2AuthenticationCheckRedirectUriFilter(constOAuth2), OAuth2AuthorizationRequestRedirectFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}