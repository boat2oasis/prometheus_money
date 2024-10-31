package com.prometheus.money.service.impl;

import com.prometheus.money.entity.Users;
import com.prometheus.money.mapper.UsersMapper;
import com.prometheus.money.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Heisenberg
 * @since 2024-10-27
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

}
