package com.example.demo.mapper;

import com.example.demo.domain.CasUserExt;
import org.apache.ibatis.annotations.Param;

/**
 * @Interface CasUserExt
 * @Description TODO
 * @Author gaomingyaoxuan
 * @Date 2019-08-01 18:21
 * @Version 1.0
 */
public interface CasUserExtMapper {


	CasUserExt queryByUserName(@Param("userName") String userName);

	Integer saveUser(@Param("entity") CasUserExt entity);

	void updUser(@Param("entity") CasUserExt entity);
}
