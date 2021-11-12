package com.jambit.project.service;

import com.jambit.project.domain.entity.Member;
import com.jambit.project.domain.repository.MemberRepository;
import com.jambit.project.dto.OAuthAttributes;
import com.jambit.project.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@SuppressWarnings("unchecked")
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.
                of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(registrationId, attributes);
        httpSession.setAttribute("user", new SessionUser(member));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private Member saveOrUpdate(String registrationId, OAuthAttributes attributes) {
        if (registrationId.equals("google")) {
            Member member = memberRepository.findByUserId(attributes.getEmail())
                    .map(entity -> entity.updateGoogle(attributes.getName(), attributes.getPicture()))
                    .orElse(attributes.toEntityOfGoogle());
            return memberRepository.save(member);
        }
        else {
            Member member = memberRepository.findByUserId(attributes.getName())
                    .map(entity -> entity.updateGithub(attributes.getName()))
                    .orElse(attributes.toEntityOfGithub());
            return memberRepository.save(member);
        }
    }

    public Member findMemberByName(String name) {
        Optional<Member> byNickname = memberRepository.findByNickname(name);
        return byNickname.orElse(null);
    }
}