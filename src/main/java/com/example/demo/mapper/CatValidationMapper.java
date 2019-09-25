package com.example.demo.mapper;

import com.example.demo.domain.CatValidation;
import org.apache.ibatis.annotations.Param;

/**
 * @Interface CatValidationMapper
 * @Description TODO
 * @Author gaomingyaoxuan
 * @Date 2019-08-01 18:22
 * @Version 1.0
 */
public interface CatValidationMapper {

	Integer save(@Param("entity") CatValidation entity);

	CatValidation queryByName(@Param("name") String name);

	void updValidation(@Param("entity") CatValidation entity);

    CatValidation queryId();
}
