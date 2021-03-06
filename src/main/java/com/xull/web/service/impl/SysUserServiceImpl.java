package com.xull.web.service.impl;

import com.xull.web.entity.SysUser;
import com.xull.web.repository.BaseRepository;
import com.xull.web.repository.UserRepository;
import com.xull.web.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service( "sysUserService")
@Slf4j
public class SysUserServiceImpl extends BaseServiceImpl<SysUser,String> implements SysUserService {

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
            log.warn("## Not find User by Id ##: {}",userId);
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
    public SysUser userLogin(String name) {
        Optional<SysUser> optional = sysUserRepository.userLogin(name);
        if (optional.isPresent()) {
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
