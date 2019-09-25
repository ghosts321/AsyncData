package com.example.demo.mapper;

import com.example.demo.domain.CasValAssoc;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Interface CasValAssocMapper
 * @Description TODO
 * @Author gaomingyaoxuan
 * @Date 2019-08-01 18:21
 * @Version 1.0
 */
public interface CasValAssocMapper {

	CasValAssoc querLinkByUserId(@Param("userId") Integer userId);

	void saveAssocLink(@Param("entity") CasValAssoc entity);

	void updAssocLink(@Param("entity") CasValAssoc entity);

	void delAssocLink(@Param("userId") Integer userId);
}
