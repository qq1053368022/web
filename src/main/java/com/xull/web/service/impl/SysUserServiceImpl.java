package com.xull.web.service.impl;

import com.xull.web.entity.SysUser;
import com.xull.web.repository.BaseRepository;
import com.xull.web.repository.UserRepository;
import com.xull.web.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service( "sysUserService")
public class SysUserServiceImpl extends BaseServiceImpl<SysUser,String> implements SysUserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository sysUserRepository;
    @Autowired
    private PasswordHelper passwordHelper;

    @Override
    public SysUser createSysUser(SysUser user) {
        passwordHelper.encryptPassword(user);
        return sysUserRepository.saveAndFlush(user);
    }

    @Override
    public void changePassword(String  userId, String newPassword) {
        Optional<SysUser> optional = sysUserRepository.findById(userId);
        if (optional.isPresent()){
            SysUser user=optional.get();
            user.setPassword(newPassword);
            passwordHelper.encryptPassword(user);
            sysUserRepository.saveAndFlush(user);
        }else {
            logger.warn("## Not find User by Id ##: {}",userId);
        }

    }


    @Override
    public SysUser findByUsername(String username) {
        Optional<SysUser> optional = sysUserRepository.findByUsername(username);
        if (optional.isPresent()){
            return optional.get();
        }else {
            return null;
        }
    }


    @Override
    @PostConstruct
    public BaseRepository<SysUser, String> getBaseRepository() {
        return this.sysUserRepository;
    }
}
