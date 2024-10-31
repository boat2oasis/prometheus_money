package com.prometheus.money.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prometheus.money.auth.CustomUserDetailsService;
import com.prometheus.money.config.JwtUtils;
import com.prometheus.money.entity.transfer.vo.UserVo;
import com.prometheus.money.res.Res;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Heisenberg
 * @since 2024-10-27
 */
@RestController
@RequestMapping("/users")
public class UsersController {
	@Autowired
    private  PasswordEncoder passwordEncoder;
	@Autowired
    private AuthenticationManager authenticationManager;
	@Autowired
    private CustomUserDetailsService customUserDetailsService;
	@Autowired
    private  JwtUtils jwtUtils;
    
    @PostMapping("/login")
    public Res<String> login(@RequestBody UserVo user) throws AuthenticationException {
    	System.out.println(passwordEncoder.encode("admind"));
    	String username = user.getUsername();
    	String password = user.getPassword();
    	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        System.out.println(jwtUtils.generateToken(userDetails.getUsername()));
        return Res.success(jwtUtils.generateToken(userDetails.getUsername()));
        //return jwtUtils.generateToken(userDetails.getUsername());
    }
    
}
