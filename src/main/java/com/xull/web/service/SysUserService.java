package com.xull.web.service;


import com.xull.web.entity.SysUser;

public interface SysUserService  extends BaseService<SysUser,Long>{

    public SysUser createSysUser(SysUser user);

    public void changePassword(Long userId, String newPassword);

    public SysUser findByUsername(String username);

}
