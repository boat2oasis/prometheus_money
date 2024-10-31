package com.prometheus.money.auth;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.prometheus.money.entity.Users;
import com.prometheus.money.mapper.UsersMapper;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
    private UsersMapper userRepository;
	

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load the user from the database
    	LambdaQueryWrapper<Users> lambdaQueryWrapper = new LambdaQueryWrapper();
    	lambdaQueryWrapper.eq(Users::getUsername, username);
        Users user = userRepository.selectOne(lambdaQueryWrapper);
        if(user == null) {
        	new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }
}