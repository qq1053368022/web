package com.xull.web.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SysUrlFilter extends BaseEntity{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    @Column(name = "fname")
    private String name;
    @Column(name = "furl")
    private String url;
    @Column(name = "froles")
    private String roles;
    @Column(name = "fpermissions")
    private String permissions;
}
