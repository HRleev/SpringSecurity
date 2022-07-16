package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


//시큐리티 설정에서 loginProcessingUrl("/login);
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행
@Service
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;


    //시큐리티 session(내부Authentication(내부UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity=userRepository.findByUsername(username);
        if(userEntity!=null){
            return new PrincipalDetails(userEntity);//PrincipalDetails 안에 꼭 userObject 를 넣어줘야 활용편의
        }
        return null;
    }

    //로그인 버튼을 클릭하면 /login이 발동이 되고 스프링은 IoC컨테이너에서 UserDetailsService로 등록되어 있는 타입을 찾음
    //찾아지면 바로 loadUserByUsername 함수를 호출
    //그때 넘어온 파라미터 username 가져옴
}
