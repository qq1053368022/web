package com.xull.web.service.impl;

import com.xull.web.entity.BaseEntity;
import com.xull.web.repository.BaseRepository;
import com.xull.web.service.BaseService;
import java.io.Serializable;
import java.util.List;

/**
 * 通用Service实现类
 *
 * @author XuLL
 * @Time 2018-03-22  9:44
 */
public abstract class BaseServiceImpl<T extends BaseEntity, ID extends Serializable> implements BaseService<T, ID> {

    public abstract BaseRepository<T, ID> getBaseRepository();

    @Override
    public <S extends T> void save(S entity) {
        getBaseRepository().save(entity);
    }

    @Override
    public List<T> findAll() {
        return getBaseRepository().findAll();
    }

    @Override
    public T findById(ID id) {
        return getBaseRepository().getOne(id);
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        return getBaseRepository().saveAndFlush(entity);
    }

    @Override
    public void deleteById(ID id) {
        getBaseRepository().deleteById(id);
    }

    @Override
    public <S extends T> void delete(S entity) {
        getBaseRepository().delete(entity);
    }
}
