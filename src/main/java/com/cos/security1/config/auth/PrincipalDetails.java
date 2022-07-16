package com.cos.security1.config.auth;

//시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행 시킨다.
//로그인 진행 완료가 되면 session 을 만들어줍니다.(Security ContextHolder)
//오브젝트 =>Authentication 타입 객체
//Authentication 안에 user 정보가 있어야함.
//User 오브젝트 타입=>UserDetails 타입 객체

//Security Session -> Authentication 객체=>UserDetails 타입
//Security Session get >Authentication get=>UserDetails get 하면 userObject 에 접근 가능
import com.cos.security1.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private User user;//콤포지션

    public PrincipalDetails(User user) {
        this.user = user;
    }
    //해당 User의 권한을 리턴 하는곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority>collect=new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override //니 계정이 만료되었니 true=아니오
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override //니 계정이 잠겨있니
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override //니 계정의 비밀번호가 기간이 지났니
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override //니 계정이 활성화 되었니
    public boolean isEnabled() {
        return true;
    }
}
