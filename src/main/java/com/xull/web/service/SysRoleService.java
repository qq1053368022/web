package com.xull.web.service;


import com.xull.web.entity.SysRole;

public interface SysRoleService extends BaseService<SysRole,String > {
    public SysRole findByName(String name);
}
