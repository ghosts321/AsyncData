package com.example.demo.mapper;

import com.example.demo.domain.CatValidation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Interface CatValidationMapper
 * @Description TODO
 * @Author gaomingyaoxuan
 * @Date 2019-08-01 18:22
 * @Version 1.0
 */
public interface CatValidationMapper {

	Integer save(@Param("entity") CatValidation entity);

	List<CatValidation> queryByName(@Param("name") String name, @Param("valType") String valType);

	void updValidation(@Param("entity") CatValidation entity);

	void updPidById(@Param("pid") int pid, @Param("id") int id);

    CatValidation queryId();
}
