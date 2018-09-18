package com.xull.web.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 3764455704256550218L;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Column(name = "fusername")
    @NotBlank
    @Length(min = 5,max = 20)
    private String username;
    @NotBlank
    @Column(name = "fpassword")
    private String password;
    @Column(name = "fstatus")
    private String status;
    @Column(name = "fsalt")
    private String salt;

    @Column(name = "femail")
    @Email
    private String email;

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

//    @Override
//    public boolean equals(Object obj) {
//        if (this==obj){
//            return true;
//        }
//        if (obj==null||getClass()!=obj.getClass()){
//            return false;
//        }
//        SysUser user=(SysUser) obj;
//        if (this.getFid()!=null?this.getFid().equals(user.getFid()):user.getFid()!=null){
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        return id!=null?id.hashCode():0;
//    }

}
