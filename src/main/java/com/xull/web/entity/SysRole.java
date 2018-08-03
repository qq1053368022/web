package com.xull.web.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class SysRole implements Serializable {

    private static final long serialVersionUID = -5608514271095262480L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "SysRolePermission", joinColumns = {@JoinColumn(name = "roleId")}, inverseJoinColumns = {@JoinColumn(name = "permissionId")})
    private List<SysPermission> permissionList ;

    @ManyToMany
    @JoinTable(name = "sysUserRole", joinColumns = {@JoinColumn(name = "roleId")}, inverseJoinColumns = {@JoinColumn(name = "userId")})
    private List<SysUser> userList;

    @JsonBackReference
    public List<SysPermission> getPermissionList(){
        return permissionList;
    }

    @JsonBackReference
    public List<SysUser> getUserList() {
        return userList;
    }

    @Transient
    public List<String> getPermissionsName() {
        List<String> list = new ArrayList<String>();
        List<SysPermission> permissions = getPermissionList();
        for (SysPermission permission : permissions) {
            list.add(permission.getPermission());
        }
        return list;
    }
}
