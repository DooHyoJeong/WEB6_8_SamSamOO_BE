package com.ai.lawyer.global.oauth;

import com.ai.lawyer.global.jwt.CookieUtil;
import com.ai.lawyer.global.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final CookieUtil cookieUtil;

    @Value("${custom.oauth2.redirect-url}")
    private String redirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        com.ai.lawyer.domain.member.entity.MemberAdapter member = principalDetails.getMember();

        log.info("OAuth2 로그인 성공: memberId={}, email={}",
                member.getMemberId(), member.getLoginId());

        // JWT 토큰 생성 (Redis 저장 포함)
        String accessToken = tokenProvider.generateAccessToken(member);
        String refreshToken = tokenProvider.generateRefreshToken(member);

        // 쿠키에 토큰 설정
        cookieUtil.setTokenCookies(response, accessToken, refreshToken);

        log.info("JWT 토큰 생성 완료 및 쿠키 설정 완료");

        // 프론트엔드 성공 페이지로 리다이렉트
        log.info("OAuth2 로그인 완료, 프론트엔드 성공 페이지로 리다이렉트: {}", redirectUrl);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
