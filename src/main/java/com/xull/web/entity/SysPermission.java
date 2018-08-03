package com.xull.web.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class SysPermission implements Serializable {
    private static final long serialVersionUID = -5291735635085754910L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String permission;

    @ManyToMany
    @JoinTable(name = "sysRolePermission",joinColumns = {@JoinColumn(name = "permissionId")},inverseJoinColumns = {@JoinColumn(name = "roleId")})
    private List<SysRole> roleList;

    @JsonBackReference
    public List<SysRole> getRoleList() {
        return roleList;
    }
}
