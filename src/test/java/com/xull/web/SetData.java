package com.xull.web;

import com.xull.web.entity.SysPermission;
import com.xull.web.entity.SysRole;
import com.xull.web.entity.SysUser;
import com.xull.web.service.SysPermissionService;
import com.xull.web.service.SysRoleService;
import com.xull.web.service.SysUserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class SetData extends BasicUtClass{
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysPermissionService sysPermissionService;

    @Test
    public void setSysUserService() {
        SysUser sysUser=new SysUser();
        sysUser.setUsername("admin");
        sysUser.setPassword("123");
        sysUser.setStatus("1");
        sysUserService.createSysUser(sysUser);
    }
    @Test
    public void setSysUserService1() {
        SysUser sysUser=new SysUser();
        sysUser.setUsername("user");
        sysUser.setPassword("123");
        sysUser.setStatus("1");
        sysUserService.createSysUser(sysUser);
    }
    @Test
    public void setSysUserService2() {
        SysUser sysUser=new SysUser();
        sysUser.setUsername("test");
        sysUser.setPassword("123");
        sysUser.setStatus("1");
        sysUserService.createSysUser(sysUser);
    }
    @Test
    public void setSysUserService3() {
        SysUser sysUser=sysUserService.findByUsername("admin");
        SysRole sysRole = sysRoleService.findByName("admin");
        List roles = new ArrayList();
        roles.add(sysRole);
        sysUser.setRoleList(roles);
        sysUserService.saveAndFlush(sysUser);
    }

    @Test
    public void setSysRoleService1() {
        SysRole sysRole = new SysRole();
        sysRole.setName("user");
        sysRole.setDescription("用户");
        SysUser user = sysUserService.findByUsername("user");
        List users = new ArrayList();
        users.add(user);
        sysRole.setUserList(users);
        sysRoleService.saveAndFlush(sysRole);
    }
    @Test
    public void setSysRoleService() {
        SysRole sysRole=new SysRole();
        sysRole.setName("admin");
        sysRole.setDescription("管理员");
        SysUser user = sysUserService.findByUsername("admin");
        List users = new ArrayList();
        users.add(user);
        sysRole.setUserList(users);
        sysRoleService.saveAndFlush(sysRole);
    }

    @Test
    public void setSysPermissionService() {
        SysPermission sysPermission=new SysPermission();
        sysPermission.setPermission("user:*");
        sysPermission=sysPermissionService.saveAndFlush(sysPermission);
        SysRole sysRole = sysRoleService.findByName("admin");
        List sysPermissions = new ArrayList();
        sysPermissions.add(sysPermission);
        sysRole.setPermissionList(sysPermissions);
        sysPermission.setRoleList(sysPermissions);
        sysRoleService.saveAndFlush(sysRole);
    }

    @Test
    public void setSysPermissionService1() {
        SysPermission sysPermission=new SysPermission();
        sysPermission.setPermission("user:user:*");
        SysRole sysRole = sysRoleService.findByName("user");
        List roles = new ArrayList();
        roles.add(sysRole);
        sysPermission.setRoleList(roles);
        sysPermissionService.saveAndFlush(sysPermission);
    }


}
