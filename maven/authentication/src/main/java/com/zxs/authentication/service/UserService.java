package com.zxs.authentication.service;

import com.zxs.authentication.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity user = new UserEntity();// TODO DAO层 通过用户名查询 用户
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }
        return user;
    }

}
