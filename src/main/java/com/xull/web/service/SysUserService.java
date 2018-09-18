package com.xull.web.service;


import com.xull.web.entity.SysUser;

public interface SysUserService  extends BaseService<SysUser,String >{

    public SysUser createSysUser(SysUser user);

    public void changePassword(String  userId, String newPassword);

    public SysUser findByUsername(String username);

    public SysUser userLogin(String name);

}
