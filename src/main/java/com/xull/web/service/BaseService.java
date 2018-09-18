package com.xull.web.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;

/**
 * 通用Service接口
 * @author XuLL
 * @Time  2018-03-22  9:42
 */
public interface BaseService<T,ID extends Serializable> {


    <S extends T>void save(S entity);

    List<T> findAll();

    T findById(ID id);

    <S extends T> S saveAndFlush(S entity);

    void deleteById(ID id);

    <S extends T> void delete(S entity);

    Page<T> findAll(Pageable pageable);

    Page<T> findAll(Specification<T> spec, Pageable pageable);
}
