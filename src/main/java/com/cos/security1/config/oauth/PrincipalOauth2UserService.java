package com.cos.security1.config.oauth;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Data
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    //구글로부터 받은 userRequest 데이터에 대한 후처리 되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest:"+userRequest);
        System.out.println("getClientRegistration:"+userRequest.getClientRegistration());
        //registrationId로 어떠누 Oauth 로 로그인 했는지 확인 가능


        OAuth2User oAuth2User =super.loadUser(userRequest);
        System.out.println("getAccessToken:"+userRequest.getAccessToken());
        //구글 로그인 버튼 클릭->구글 로그인창->로그인 완료->code 를 리턴(Oauth-Client)->AccessToken 요청
        //userRequest 정보->loadUser 함수->구글로부터 회원 프로필 받음
        System.out.println("getAttributes:"+oAuth2User.getAttributes());

        //강제회원가입
        String provider = userRequest.getClientRegistration().getClientId();//google
        String providerId = oAuth2User.getAttribute("sub");//google providerId
        String username = provider+"_"+providerId;//google_106966002766987749159
        String password = oAuth2User.getAttribute("password");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userEntity=userRepository.findByUsername(username);
        if(userEntity==null){
            userEntity=User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }
        return new PrincipalDetails(userEntity,oAuth2User.getAttributes());
    }
}
