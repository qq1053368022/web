package com.xull.web.service.impl;

import com.xull.web.service.BaseService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 通用Service实现类
 * @author XuLL
 * @Time  2018-03-22  9:44
 */
public abstract class BaseServiceImpl<T,ID> implements BaseService<T,ID> {
    private JpaRepository<T, ID> repository;


    public void setBaseMapper(JpaRepository<T,ID> repository) {
        this.repository=repository;
    }

    @Override
    public <S extends T> void save(S entity) {
         repository.save(entity);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public T findById(ID id) {
        return   repository.getOne(id);
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }
    @Override
    public <S extends T> void delete(S entity) {
        repository.delete(entity);
    }
}
