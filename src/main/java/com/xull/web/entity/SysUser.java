package com.xull.web.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class SysUser implements Serializable {

    private static final long serialVersionUID = 3764455704256550218L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String status;
    private String salt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "SysUserRole",joinColumns = {@JoinColumn(name = "userId")},inverseJoinColumns = {@JoinColumn(name = "roleId")})
    private List<SysRole> roleList;

    public String getCredentialsSalt() {
        return username+salt;
    }
    @JsonBackReference
    public List<SysRole> getRoleList(){
        return roleList;
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj){
            return true;
        }
        if (obj==null||getClass()!=obj.getClass()){
            return false;
        }
        SysUser user=(SysUser) obj;
        if (id!=null?id.equals(user.id):user.id!=null){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return id!=null?id.hashCode():0;
    }

}
