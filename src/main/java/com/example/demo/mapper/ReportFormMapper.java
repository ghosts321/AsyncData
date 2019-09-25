package com.example.demo.mapper;

import com.example.demo.domain.ReportForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ Description   :  java类作用描述
 * @ Author        :  JiangJunpeng
 * @ CreateDate    :  2019/9/5 18:13
 * @ UpdateUser    :  JiangJunpeng
 * @ UpdateDate    :  2019/9/5 18:13
 * @ UpdateRemark  :  修改内容
 * @ Version       :  1.0
 */
public interface ReportFormMapper {

    List<ReportForm> queryReportForm(@Param("beginTime") String beginTime, @Param("endTime") String endTime);
    List<ReportForm> queryCopyTotal(@Param("chargeId") Integer chargeId);
    List<ReportForm> queryPrintTotal(@Param("chargeId") Integer chargeId);

}
