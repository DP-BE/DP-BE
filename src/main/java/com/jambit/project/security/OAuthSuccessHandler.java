package com.jambit.project.security;

import com.jambit.project.domain.entity.Member;
import com.jambit.project.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        DefaultOAuth2User principal = (DefaultOAuth2User)authentication.getPrincipal();
        String currentUser = null;
        String attribute = principal.getAttributes().toString();
        if (attribute.contains("login")) {
            Member findMember = customOAuth2UserService.findMemberByName(principal.getAttributes().get("login").toString());
            currentUser = findMember.getNickname();
        }
        else {
            Member findMember = customOAuth2UserService.findMemberByName(principal.getAttributes().get("name").toString());
            currentUser = findMember.getNickname();
        }
        if (currentUser != null) {
            String accessToken = jwtTokenProvider.createToken(currentUser);
            Cookie accessTokenCookie = new Cookie("access-token", accessToken);
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(accessTokenCookie);
            try {
                //response.sendRedirect("http://localhost:3000");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
