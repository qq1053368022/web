package com.xull.web.repository;

import com.xull.web.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * @description:
 * @author: xull
 * @date: 2018-09-05 10:23
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity,ID extends Serializable> extends JpaRepository<T,ID>,JpaSpecificationExecutor<T> {
}
