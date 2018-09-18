package com.xull.file.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @description:
 * @author: xull
 * @date: 2018-09-07 9:09
 */
@MappedSuperclass //@MappedSuperclass为引用类型
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class BaseModel implements Serializable {
    private static final long serialVersionUID = -2335142100397670710L;
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @Column(nullable = false,length = 100,name = "fid")
    private String id;

    @Column(name = "fname")
    private String name;

    @Column(name = "furl")
    private String url;
}
