package com.cos.security1.controller;


import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    //Authentication
    //UserDetail -> 일반로그인
    //UAuth2User -> 구글 로그인



    @GetMapping({"/", ""})
    public String index() {
        return "index";
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails:"+principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);//회원 가입 잘됨. 비밀번호 1234 ->암호화가 안되었기 때문에 시큐리티로 로그인 불가
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")//특정 메서드에 간단히 권한을 주고 싶을때 사용하면 딱조아!
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }//하나만 걸고싶으면Secured 여러개를 걸고 싶으면 hasRole

    @PreAuthorize("hasRole('ROLE_MANAGER')or hasRole('ROLE_ADMIN')")
    @Secured("ROLE_ADMIN")//특정 메서드에 간단히 권한을 주고 싶을때 사용하면 딱조아!
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}
