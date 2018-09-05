package com.xull.web.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @description:
 * @author: xull
 * @date: 2018-09-05 10:04
 */
@MappedSuperclass //@MappedSuperclass为引用类型
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -285987672365358802L;
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @Column(nullable = false,length = 100)
    private String fid;

    @CreatedDate
    private Long fCreateTime;

    @LastModifiedDate
    private Long fUpdateTime;
}
