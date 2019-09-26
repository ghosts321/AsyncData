package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.Constant;
import com.example.demo.config.ExcelUtil;
import com.example.demo.config.MyBatisConfig;
import com.example.demo.config.ResponseMap;
import com.example.demo.domain.*;
import com.example.demo.mapper.CasUserExtMapper;
import com.example.demo.mapper.CasValAssocMapper;
import com.example.demo.mapper.CatValidationMapper;
import com.example.demo.mapper.ReportFormMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.*;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName SyncController
 * @Description TODO
 * @Author gaomingyaoxuan
 * @Date 2019-07-26 10:56
 * @Version 1.0
 */
@RestController
@RequestMapping("sync")
@CrossOrigin
@EnableScheduling
public class SyncController {

    @RequestMapping("hi")
    public String getName() {

        return "welcome !!!";
    }

    /**
     * @param : formData
     * @return : SUCCESS_STATE
     * @throws : e
     * @MethodName : 测试连接
     * @Description : 方法功能描述
     * @author : JiangJunpeng
     * @date : 2019/9/6 12:29
     */
    @RequestMapping("testConnection")
    public ResponseEntity<Map<String, Object>> testConnection(@RequestBody String formData) {
        Connection mainConnection = null;

        if (formData == null || "".equals(formData)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMap(Constant.PARAMETE_ERROR, "表单参数无效！", formData).getResponseMap());
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(formData);
            String mainUrl = jsonObject.getString("mainUrl");
            if (mainUrl == null || "".equals(mainUrl)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "数据库地址不能为空！", "").getResponseMap());
            }
            String mainDataBaseName = jsonObject.getString("mainDataBaseName");
            if (mainDataBaseName == null || "".equals(mainDataBaseName)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "数据库名称不能为空！", "").getResponseMap());
            }
            String mainUserName = jsonObject.getString("mainUserName");
            if (mainUserName == null || "".equals(mainUserName)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "数据库登录账号不能为空！", "").getResponseMap());
            }
            String mainPassWord = jsonObject.getString("mainPassWord");
            if (mainUrl == null || "".equals(mainUrl)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "数据库登录密码不能为空！", "").getResponseMap());
            }
            String syncUrl = jsonObject.getString("syncUrl");
            if (syncUrl == null || "".equals(syncUrl)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "AD域地址不能为空！", "").getResponseMap());
            }
            String syncUserName = jsonObject.getString("syncUserName");
            if (syncUserName == null || "".equals(syncUserName)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "AD域登录账号不能为空！", "").getResponseMap());
            }
            String syncPassWord = jsonObject.getString("syncPassWord");
            if (syncPassWord == null || "".equals(syncPassWord)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "AD域登录密码不能为空！", "").getResponseMap());
            }

            DataBaseDomain dataBaseDomain = JSONObject.parseObject(formData, DataBaseDomain.class);
            DataBaseConnection connection = connection(dataBaseDomain);
            DataSource mainDataSource = connection.getMainDataSource();
            LdapContext ldapContext = connection.getLdapContext();
            mainConnection = mainDataSource.getConnection();
            if (mainConnection == null || ldapContext == null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.UNKNOWN_ERROR, "连接失败！", "").getResponseMap());
            }
            this.outPutProperties(dataBaseDomain);
            mainConnection.close();
            ldapContext.close();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMap(Constant.SUCCESS_STATE, "连接成功！", "").getResponseMap());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMap(Constant.UNKNOWN_ERROR, "连接失败！", "").getResponseMap());
        }
    }

    /**
     * @param : forData
     * @return : list
     * @throws : e
     * @MethodName : 域同步
     * @Description : 方法功能描述
     * @author : JiangJunpeng
     * @date : 2019/9/6 12:30
     */
    @RequestMapping("/syncDataBase")
    public ResponseEntity<Map<String, Object>> syncDataBase(@RequestBody String formData) {
        Connection mainConnection = null;

        if (formData == null || "".equals(formData)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMap(Constant.PARAMETE_ERROR, "表单参数无效！", formData).getResponseMap());
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(formData);
            String mainUrl = jsonObject.getString("mainUrl");
            if (mainUrl == null || "".equals(mainUrl)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "数据库地址不能为空！", "").getResponseMap());
            }
            String mainDataBaseName = jsonObject.getString("mainDataBaseName");
            if (mainDataBaseName == null || "".equals(mainDataBaseName)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "数据库名称不能为空！", "").getResponseMap());
            }
            String mainUserName = jsonObject.getString("mainUserName");
            if (mainUserName == null || "".equals(mainUserName)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "数据库登录账号不能为空！", "").getResponseMap());
            }
            String mainPassWord = jsonObject.getString("mainPassWord");
            if (mainUrl == null || "".equals(mainUrl)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "数据库登录密码不能为空！", "").getResponseMap());
            }
            String syncUrl = jsonObject.getString("syncUrl");
            if (syncUrl == null || "".equals(syncUrl)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "AD域地址不能为空！", "").getResponseMap());
            }
            String syncUserName = jsonObject.getString("syncUserName");
            if (syncUserName == null || "".equals(syncUserName)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "AD域登录账号不能为空！", "").getResponseMap());
            }
            String syncPassWord = jsonObject.getString("syncPassWord");
            if (syncPassWord == null || "".equals(syncPassWord)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "AD域登录密码不能为空！", "").getResponseMap());
            }

            DataBaseDomain dataBaseDomain = JSONObject.parseObject(formData, DataBaseDomain.class);
            DataBaseConnection connection = connection(dataBaseDomain);
            DataSource mainDataSource = connection.getMainDataSource();
            SqlSession mainSqlSession = MyBatisConfig.buildSqlSession(mainDataSource);
            mainConnection = mainSqlSession.getConnection();
            mainConnection.setAutoCommit(true);
            LdapContext ldapContext = connection.getLdapContext();
            List<CasUserExt> allAdUser = getAllAdUser(ldapContext);
            if (allAdUser.size() == 0) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "同步失败。未获取AD域用户信息！", formData).getResponseMap());
            }
            CasUserExtMapper userExtMapper = mainSqlSession.getMapper(CasUserExtMapper.class);
            CatValidationMapper validationMapper = mainSqlSession.getMapper(CatValidationMapper.class);
            CasValAssocMapper assocMapper = mainSqlSession.getMapper(CasValAssocMapper.class);
            for (CasUserExt casUserExt : allAdUser) {
                int userId = saveUserData(casUserExt, userExtMapper, validationMapper);
                int depId = saveDepData(casUserExt, validationMapper);
                saveAssoc(userId, depId, assocMapper);
                validationMapper.updPidById(depId, userId);
            }
            this.outPutProperties(dataBaseDomain);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMap(Constant.SUCCESS_STATE, "成功同步！", formData).getResponseMap());
        } catch (Exception e) {
            System.out.println(e);
            try {
                assert mainConnection != null;
                mainConnection.rollback();
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.UNKNOWN_ERROR, "同步失败，数据错误！", e).getResponseMap());

            } catch (SQLException ex) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.UNKNOWN_ERROR, "同步失败，数据库错误！", ex).getResponseMap());
            }
        }
    }

    /**
     * @param : formData
     * @return : list
     * @throws : e
     * @MethodName : 查看报表
     * @Description : 方法功能描述
     * @author : JiangJunpeng
     * @date : 2019/9/7 11:36
     */
    @RequestMapping("/queryReportForm")
    public ResponseEntity<Map<String, Object>> queryReportForm(@RequestBody String formData) {

        try {
            JSONObject jsonObject = JSONObject.parseObject(formData);
            String beginTime = jsonObject.getString("beginTime");
            String endTime = jsonObject.getString("endTime");

            Connection mainConnection = null;
            DataBaseDomain dataBaseDomain = null;
            ApplicationHome home = new ApplicationHome(SyncController.class);
            File jarFile = home.getSource();
            String parent = jarFile.getParent();
            String fileName = parent + File.separator + "syncData.properties";
            if (!Files.exists(Paths.get(fileName))) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "找不到数据库配置文件！", "").getResponseMap());
            }
            FileInputStream fileInputStream = new FileInputStream(fileName);
            Properties properties = new Properties();
            properties.load(fileInputStream);
            InetAddress addr = InetAddress.getLocalHost();
            String localIp = addr.toString().substring(addr.toString().indexOf("/") + 1, addr.toString().length());
            String json = properties.getProperty(localIp);
            JSONObject jsonObjects = JSON.parseObject(json);
            if (jsonObjects == null) {
                fileInputStream.close();
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "找不到配置文件内对应配置项！", "").getResponseMap());
            }
            dataBaseDomain = jsonObjects.toJavaObject(DataBaseDomain.class);
            fileInputStream.close();
            DataBaseConnection connection = connectionReport(dataBaseDomain);
            DataSource mainDataSource = connection.getMainDataSource();
            SqlSession mainSqlSession = MyBatisConfig.buildSqlSession(mainDataSource);
            mainConnection = mainSqlSession.getConnection();
            mainConnection.setAutoCommit(true);

            ReportFormMapper reportFormMapper = mainSqlSession.getMapper(ReportFormMapper.class);

            List<ReportForm> reportFormList = reportFormMapper.queryReportForm(beginTime, endTime);

            List<ReportForm> reportFormLists = queryForm(reportFormMapper, reportFormList);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMap(Constant.SUCCESS_STATE, "success！", reportFormLists).getResponseMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMap(Constant.UNKNOWN_ERROR, "error！", "").getResponseMap());
    }

    /**
     * @param : formData
     * @return : list
     * @throws : e
     * @MethodName : 导出报表
     * @Description : 方法功能描述
     * @author : JiangJunpeng
     * @date : 2019/9/7 11:37
     */
    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> exportExcel(HttpServletRequest request, HttpServletResponse response) {

        try {
            String beginTime = request.getParameter("beginTime");
            String endTime = request.getParameter("endTime");

            Connection mainConnection = null;
            DataBaseDomain dataBaseDomain = null;
            ApplicationHome home = new ApplicationHome(SyncController.class);
            File jarFile = home.getSource();
            String parent = jarFile.getParent();
            String fileName = parent + File.separator + "syncData.properties";
            if (!Files.exists(Paths.get(fileName))) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "找不到数据库配置文件！", "").getResponseMap());
            }
            FileInputStream fileInputStream = new FileInputStream(fileName);
            Properties properties = new Properties();
            properties.load(fileInputStream);
            InetAddress addr = InetAddress.getLocalHost();
            String localIp = addr.toString().substring(addr.toString().indexOf("/") + 1, addr.toString().length());
            String json = properties.getProperty(localIp);
            JSONObject jsonObjects = JSON.parseObject(json);
            if (jsonObjects == null) {
                fileInputStream.close();
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMap(Constant.PARAMETE_ERROR, "找不到配置文件内对应配置项！", "").getResponseMap());
            }
            dataBaseDomain = jsonObjects.toJavaObject(DataBaseDomain.class);
            fileInputStream.close();
            DataBaseConnection connection = connectionReport(dataBaseDomain);
            DataSource mainDataSource = connection.getMainDataSource();
            SqlSession mainSqlSession = MyBatisConfig.buildSqlSession(mainDataSource);
            mainConnection = mainSqlSession.getConnection();
            mainConnection.setAutoCommit(true);
            ReportFormMapper reportFormMapper = mainSqlSession.getMapper(ReportFormMapper.class);
            List<ReportForm> reportFormList = reportFormMapper.queryReportForm(beginTime, endTime);
            List<ReportForm> reportFormLists = queryForm(reportFormMapper, reportFormList);

            ExcelUtil eu = new ExcelUtil();
            // 1、sheet表中的标题行内容，需要输入excel的汇总数据
            String[] unitHeader = {"序号", "部门", "账号", "姓名", "A3复印(黑白)", "A3复印(彩色)", "A3打印(黑白)", "A3打印(彩色)", "小于A3复印(黑白)", "小于A3复印(彩色)", "小于A3打印(黑白)", "小于A3打印(彩色)", "总张数(复印黑白)", "总张数(复印彩色)", "总张数(打印黑白)", "总张数(打印彩色)"};
            // 2、对应不同Sheet表格建立相应List
            List<List<String>> excelList = new ArrayList<List<String>>();
            int j = 1;
            if (reportFormLists != null && reportFormLists.size() > 0) {
                for (ReportForm entity : reportFormLists) {
                    List<String> rowData = new ArrayList<String>();
                    rowData.add(String.valueOf(j));
                    rowData.add(String.valueOf(entity.getDepartment()));
                    rowData.add(String.valueOf(entity.getAccountNumber()));
                    rowData.add(String.valueOf(entity.getAccountName()));
                    rowData.add(String.valueOf(entity.getCopyThreeBw()));
                    rowData.add(String.valueOf(entity.getCopyThreeColor()));
                    rowData.add(String.valueOf(entity.getPrintThreeBw()));
                    rowData.add(String.valueOf(entity.getPrintThreeColor()));
                    rowData.add(String.valueOf(entity.getLowCopyThreeBw()));
                    rowData.add(String.valueOf(entity.getLowCopyThreeColor()));
                    rowData.add(String.valueOf(entity.getLowPrintThreeBw()));
                    rowData.add(String.valueOf(entity.getLowPrintThreeColor()));
                    rowData.add(String.valueOf(entity.getTotalCopyBw()));
                    rowData.add(String.valueOf(entity.getTotalCopyColor()));
                    rowData.add(String.valueOf(entity.getTotalPrintBw()));
                    rowData.add(String.valueOf(entity.getTotalPrintColor()));
                    excelList.add(rowData);
                    j++;
                }
            } else {
                List<String> rowData = new ArrayList<String>();
                rowData.add("");
                rowData.add("");
                rowData.add("");
                rowData.add("");
                rowData.add("");
                rowData.add("");
                rowData.add("");
                rowData.add("");
                rowData.add("");
                rowData.add("");
                rowData.add("");
                rowData.add("");
                rowData.add("");
                rowData.add("");
                rowData.add("");
                rowData.add("");
                excelList.add(rowData);
            }

            //总计行
            Integer copyThreeBw = reportFormList.stream().collect(Collectors.summingInt(ReportForm::getCopyThreeBw));
            Integer copyThreeColor = reportFormList.stream().collect(Collectors.summingInt(ReportForm::getCopyThreeColor));
            Integer printThreeBw = reportFormList.stream().collect(Collectors.summingInt(ReportForm::getPrintThreeBw));
            Integer printThreeColor = reportFormList.stream().collect(Collectors.summingInt(ReportForm::getPrintThreeColor));
            Integer lowCopyThreeBw = reportFormList.stream().collect(Collectors.summingInt(ReportForm::getLowCopyThreeBw));
            Integer lowCopyThreeColor = reportFormList.stream().collect(Collectors.summingInt(ReportForm::getLowCopyThreeColor));
            Integer lowPrintThreeBw = reportFormList.stream().collect(Collectors.summingInt(ReportForm::getLowPrintThreeBw));
            Integer lowPrintThreeColor = reportFormList.stream().collect(Collectors.summingInt(ReportForm::getLowPrintThreeColor));
            Integer totalCopyBw = reportFormList.stream().collect(Collectors.summingInt(ReportForm::getTotalCopyBw));
            Integer totalCopyColor = reportFormList.stream().collect(Collectors.summingInt(ReportForm::getTotalCopyColor));
            Integer totalPrintBw = reportFormList.stream().collect(Collectors.summingInt(ReportForm::getTotalPrintBw));
            Integer totalPrintColor = reportFormList.stream().collect(Collectors.summingInt(ReportForm::getTotalPrintColor));
            List<String> rowData = new ArrayList<String>();
            rowData.add("总计");
            rowData.add("");
            rowData.add("");
            rowData.add("");
            rowData.add(String.valueOf(copyThreeBw));
            rowData.add(String.valueOf(copyThreeColor));
            rowData.add(String.valueOf(printThreeBw));
            rowData.add(String.valueOf(printThreeColor));
            rowData.add(String.valueOf(lowCopyThreeBw));
            rowData.add(String.valueOf(lowCopyThreeColor));
            rowData.add(String.valueOf(lowPrintThreeBw));
            rowData.add(String.valueOf(lowPrintThreeColor));
            rowData.add(String.valueOf(totalCopyBw));
            rowData.add(String.valueOf(totalCopyColor));
            rowData.add(String.valueOf(totalPrintBw));
            rowData.add(String.valueOf(totalPrintColor));
            excelList.add(rowData);

            XSSFWorkbook workbook = new XSSFWorkbook();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            String finalFileName = null;
            String userAgent = request.getHeader("USER-AGENT").toLowerCase();
            if (userAgent.contains("firefox")) {
                finalFileName = new String("报表.xlsx".getBytes(), "ISO8859-1");
            } else {
                finalFileName = URLEncoder.encode("报表.xlsx", "UTF8");// 其他浏览器
            }
            response.setHeader("Content-disposition",
                    "attzchment;filename=\"" + finalFileName + "\"");
            OutputStream out = response.getOutputStream();
            // 4、将数据插入表格中
            int i = 0;
            // 第一个表格内容
            if (excelList != null && excelList.size() != 0) {
                eu.exportExcel(workbook, i, "报表", unitHeader, excelList);
                i++;
            }
            // 将所有的数据一起写入，然后再关闭输入流。
            workbook.write(out);
            out.flush();
            out.close();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMap(Constant.SUCCESS_STATE, "导出成功！", excelList).getResponseMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseMap(Constant.UNKNOWN_ERROR, "error！", "").getResponseMap());
    }


    public List<ReportForm> queryForm(ReportFormMapper reportFormMapper, List<ReportForm> reportFormList){
        List<ReportForm> reportFormLists = new ArrayList<>();
        List<ReportForm> reportFormListCopy = new ArrayList<>();
        List<ReportForm> reportFormListPrint = new ArrayList<>();

        if (reportFormList != null && reportFormList.size() > 0) {
            for (ReportForm reportForm : reportFormList) {
                //查询个人复印数量
                reportFormListCopy = reportFormMapper.queryCopyTotal(reportForm.getChargeId());
                List<ReportForm> reportFormCopys = new ArrayList<>();
                if (reportFormListCopy != null && reportFormListCopy.size() > 0) {
                    for (ReportForm reportFormCopy : reportFormListCopy) {

                        String copyTotal = reportFormCopy.getCopyTotal().trim();

                        ReportForm reportForm1 = new ReportForm();

                        //是否含有多组数据
                        if (copyTotal.contains(";")) {
                            List<ReportForm> reportFormList1 = new ArrayList<>();
                            String[]  strs=copyTotal.split(";");

                            for(int i = 0; i < strs.length; i++){
                                ReportForm reportForm2 = new ReportForm();
                                String str = strs[i].toString().trim();
                                String a = str.substring(0, str.indexOf(" "));
                                int b = Integer.parseInt(a);
                                //纸张大小是否为A3
                                if (str.contains("A3")) {
                                    //打印颜色是否为彩色
                                    if (str.contains("/C")) {
                                        reportForm2.setCopyThreeColor(b);
                                    } else {
                                        reportForm2.setCopyThreeBw(b);
                                    }
                                } else {
                                    if (str.contains("/C")) {
                                        reportForm2.setLowCopyThreeColor(b);
                                    } else {
                                        reportForm2.setLowCopyThreeBw(b);
                                    }
                                }
                                reportFormList1.add(reportForm2);
                            }
                            int a = reportFormList1.stream().collect(Collectors.summingInt(ReportForm::getCopyThreeBw));
                            int b = reportFormList1.stream().collect(Collectors.summingInt(ReportForm::getCopyThreeColor));
                            int c = reportFormList1.stream().collect(Collectors.summingInt(ReportForm::getLowCopyThreeBw));
                            int d = reportFormList1.stream().collect(Collectors.summingInt(ReportForm::getLowCopyThreeColor));
                            reportForm1.setCopyThreeBw(a);
                            reportForm1.setCopyThreeColor(b);
                            reportForm1.setLowCopyThreeBw(c);
                            reportForm1.setLowCopyThreeColor(d);
                            reportFormCopys.add(reportForm1);
                        } else {
                            String a = copyTotal.substring(0, copyTotal.indexOf(" "));
                            int b = Integer.parseInt(a);
                            if (copyTotal.contains("A3")) {
                                //打印颜色是否为彩色
                                if (copyTotal.contains("/C")) {
                                    reportForm1.setCopyThreeColor(b);
                                } else {
                                    reportForm1.setCopyThreeBw(b);
                                }
                            } else {
                                if (copyTotal.contains("/C")) {
                                    reportForm1.setLowCopyThreeColor(b);
                                } else {
                                    reportForm1.setLowCopyThreeBw(b);
                                }
                            }

                            reportFormCopys.add(reportForm1);
                        }
                    }
                }

                //查询个人打印数量
                reportFormListPrint = reportFormMapper.queryPrintTotal(reportForm.getChargeId());
                List<ReportForm> reportFormPrints = new ArrayList<>();
                if (reportFormListPrint != null && reportFormListPrint.size() > 0) {
                    for (ReportForm reportFormPrint : reportFormListPrint) {

                        String printTotal = reportFormPrint.getPrintTotal().trim();

                        ReportForm reportForm1 = new ReportForm();

                        if(printTotal.contains(":")){

                            String a = printTotal.substring(printTotal.indexOf(":") + 1, printTotal.length());

                            printTotal = a.trim();

                        }

                        //是否含有多组数据
                        if (printTotal.contains(";")) {
                            List<ReportForm> reportFormList1 = new ArrayList<>();
                            String[] strs=printTotal.split(";");

                            for(int i = 0; i < strs.length; i++){
                                ReportForm reportForm2 = new ReportForm();
                                String str = strs[i].toString().trim();
                                String a = str.substring(0, str.indexOf(" "));
                                int b = Integer.parseInt(a);
                                //纸张大小是否为A3
                                if (str.contains("A3")) {
                                    //打印颜色是否为彩色
                                    if (str.contains("/C")) {
                                        reportForm2.setPrintThreeColor(b);
                                    } else {
                                        reportForm2.setPrintThreeBw(b);
                                    }
                                } else {
                                    if (str.contains("/C")) {
                                        reportForm2.setLowPrintThreeColor(b);
                                    } else {
                                        reportForm2.setLowPrintThreeBw(b);
                                    }
                                }
                                reportFormList1.add(reportForm2);
                            }
                            int a = reportFormList1.stream().collect(Collectors.summingInt(ReportForm::getPrintThreeBw));
                            int b = reportFormList1.stream().collect(Collectors.summingInt(ReportForm::getPrintThreeColor));
                            int c = reportFormList1.stream().collect(Collectors.summingInt(ReportForm::getLowPrintThreeBw));
                            int d = reportFormList1.stream().collect(Collectors.summingInt(ReportForm::getLowPrintThreeColor));
                            reportForm1.setPrintThreeBw(a);
                            reportForm1.setPrintThreeColor(b);
                            reportForm1.setLowPrintThreeBw(c);
                            reportForm1.setLowPrintThreeColor(d);
                            reportFormPrints.add(reportForm1);
                        } else {
                            String a = printTotal.substring(0, printTotal.indexOf(" "));
                            int b = Integer.parseInt(a);
                            if (printTotal.contains("A3")) {
                                //打印颜色是否为彩色
                                if (printTotal.contains("/C")) {
                                    reportForm1.setPrintThreeColor(b);
                                } else {
                                    reportForm1.setPrintThreeBw(b);
                                }
                            } else {
                                if (printTotal.contains("/C")) {
                                    reportForm1.setLowPrintThreeColor(b);
                                } else {
                                    reportForm1.setLowPrintThreeBw(b);
                                }
                            }

                            reportFormPrints.add(reportForm1);
                        }
                    }
                }

                int a = reportFormCopys.stream().collect(Collectors.summingInt(ReportForm::getCopyThreeBw));
                int b = reportFormCopys.stream().collect(Collectors.summingInt(ReportForm::getCopyThreeColor));
                int c = reportFormCopys.stream().collect(Collectors.summingInt(ReportForm::getLowCopyThreeBw));
                int d = reportFormCopys.stream().collect(Collectors.summingInt(ReportForm::getLowCopyThreeColor));
                int e = reportFormPrints.stream().collect(Collectors.summingInt(ReportForm::getPrintThreeBw));
                int f = reportFormPrints.stream().collect(Collectors.summingInt(ReportForm::getPrintThreeColor));
                int g = reportFormPrints.stream().collect(Collectors.summingInt(ReportForm::getLowPrintThreeBw));
                int h = reportFormPrints.stream().collect(Collectors.summingInt(ReportForm::getLowPrintThreeColor));
                reportForm.setCopyThreeBw(a);
                reportForm.setCopyThreeColor(b);
                reportForm.setLowCopyThreeBw(c);
                reportForm.setLowCopyThreeColor(d);
                reportForm.setTotalCopyBw(a*2+c);
                reportForm.setTotalCopyColor(b*2+d);
                reportForm.setPrintThreeBw(e);
                reportForm.setPrintThreeColor(f);
                reportForm.setLowPrintThreeBw(g);
                reportForm.setLowPrintThreeColor(h);
                reportForm.setTotalPrintBw(e*2+g);
                reportForm.setTotalPrintColor(f*2+h);
                reportFormLists.add(reportForm);
            }
        }
        return reportFormLists;
    }

    public int saveUserData(CasUserExt casUserExt, CasUserExtMapper userExtMapper, CatValidationMapper validationMapper) throws Exception {
        String userName = casUserExt.getSamAccountName();
        CatValidation catValidation = new CatValidation();
        List<CatValidation> catValidationList = validationMapper.queryByName(userName, "usr");
        if (catValidationList != null && catValidationList.size() > 0) {
            catValidation = catValidationList.get(0);
            catValidation.setName(casUserExt.getSamAccountName());
            catValidation.setDescription(casUserExt.getFullName());
            catValidation.setState(1);
            catValidation.setSecondaryPin("3CE59CD2B1F5525CFB84E3B1C10F8942");
            validationMapper.updValidation(catValidation);
            casUserExt.setX_id(catValidation.getId());
            userExtMapper.updUser(casUserExt);
            return catValidation.getId();
        }
        CatValidation maxId = validationMapper.queryId();
        catValidation.setName(casUserExt.getSamAccountName());
        catValidation.setValType("usr");
        catValidation.setDescription(casUserExt.getFullName());
        catValidation.setId(maxId.getId()+1);
        validationMapper.save(catValidation);
        int id = catValidation.getId();
        casUserExt.setX_id(id);
        casUserExt.setClassId(304);
        casUserExt.setColorQuota(-1);
        casUserExt.setColorPageCount(0);
        casUserExt.setPotenTialDre("");
        casUserExt.setAddiTionlInfo("");
        userExtMapper.saveUser(casUserExt);
        return catValidation.getId();
    }

    public int saveDepData(CasUserExt casUserExt, CatValidationMapper validationMapper) throws Exception {
        String department = casUserExt.getDepartment();
        if (department == null || "".equals(department)) {
            return 0;
        }
        CatValidation catValidation = new CatValidation();
        List<CatValidation> catValidationList = validationMapper.queryByName(department, "dpt");
        if (catValidationList != null && catValidationList.size() > 0) {
            catValidation = catValidationList.get(0);
            return catValidation.getId();
        }
        CatValidation maxId = validationMapper.queryId();
        catValidation.setName(department);
        catValidation.setValType("dpt");
        catValidation.setId(maxId.getId()+1);
        validationMapper.save(catValidation);
        return catValidation.getId();
    }

    public void saveAssoc(int userId, int depId, CasValAssocMapper assocMapper) {
        if (depId == 0) {
            return;
        }
        CasValAssoc casValAssocs = assocMapper.querLinkByUserId(userId);
        if (casValAssocs != null) {
            int mainId = casValAssocs.getMainId();
            if (mainId == depId) {
                return;
            }
            assocMapper.delAssocLink(userId);
        } else {
            casValAssocs = new CasValAssoc();
        }
        casValAssocs.setMainId(depId);
        casValAssocs.setAssocId(userId);
        assocMapper.saveAssocLink(casValAssocs);
    }

    public static List<CasUserExt> getAllAdUser(LdapContext ldapContext) throws NamingException {
        SearchControls searchControls = new SearchControls();
        Hashtable<?, ?> environment = ldapContext.getEnvironment();
        String userName = (String) environment.get(Context.SECURITY_PRINCIPAL);
        String substring = userName.substring(userName.lastIndexOf("@") + 1);
        String[] split = substring.split("\\.");
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : split) {
            stringBuilder.append("dc=");
            stringBuilder.append(s);
            stringBuilder.append(",");
        }
        String baseDc = stringBuilder.substring(0, stringBuilder.lastIndexOf(","));
        String filter = "(&(objectcategory=person)(objectclass=user)(!(department=离职员工)))";
        String[] returnAttr = new String[]{"cn", "ou", "mail", "department", "mobile", "sAMAccountName"};
        searchControls.setReturningAttributes(returnAttr);
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> search = ldapContext.search(baseDc, filter, searchControls);
        return parseUser(search);
    }

    public static List<CasUserExt> parseUser(NamingEnumeration<SearchResult> namingEnumeration) throws NamingException {
        List<CasUserExt> list = new ArrayList<>();
        while (namingEnumeration.hasMoreElements()) {
            SearchResult next = namingEnumeration.next();
            System.out.println(next);
            CasUserExt casUserExt = new CasUserExt();
            Attributes attributes = next.getAttributes();
            if (attributes.get("cn") != null) {
                String cn = attributes.get("cn").toString();
                casUserExt.setFullName(cn.substring(cn.lastIndexOf(":") + 1).trim());
            }
            if (attributes.get("mail") != null) {
                String mail = attributes.get("mail").toString();
                casUserExt.setEmail(mail.substring(mail.lastIndexOf(":") + 1).trim());
            }
//            if (attributes.get("department") != null) {
//                String department = attributes.get("department").toString();
//                casUserExt.setDepartment(department.substring(department.lastIndexOf(":") + 1).trim());
//            }
            if(next != null){
                String department = null;
                String str = null;
                String ou = next.toString();
                if(ou.contains(",") && ou.contains(":")){
                    str = ou.substring(ou.indexOf(",") + 1, ou.indexOf(":"));
                    if(str != null && !str.equals("") && str.contains("OU")){
                        if (str.contains(",")){
                            department = str.substring(str.indexOf("=") + 1, str.indexOf(",")).trim();
                        }else {
                            department = str.substring(str.indexOf("=") + 1, str.length()).trim();
                        }
                    }
                }
                casUserExt.setDepartment(department);
            }


            if (attributes.get("mobile") != null) {
                String mobile = attributes.get("mobile").toString();
                casUserExt.setMobile(mobile.substring(mobile.lastIndexOf(":") + 1).trim());
            }
            if (attributes.get("samaccountName") != null) {
                String samaccountName = attributes.get("samaccountName").toString();
                casUserExt.setSamAccountName(samaccountName.substring(samaccountName.lastIndexOf(":") + 1).trim());
            }
            list.add(casUserExt);
        }
        return list;
    }

    @RequestMapping("index")
    public String index() {
        return "SyncData.html";
    }

    public void outPutProperties(DataBaseDomain dataBaseDomain) throws IOException {
        System.out.println("输出");
        ApplicationHome home = new ApplicationHome(SyncController.class);
        File jarFile = home.getSource();
        String parent = jarFile.getParent();
        String fileName = parent + File.separator + "syncData.properties";
        if (Files.exists(Paths.get(fileName))) {
            Files.delete(Paths.get(fileName));
        }
        Files.createFile(Paths.get(fileName));
        FileOutputStream fileOutputStream = new FileOutputStream(fileName, true);
        Properties properties = new Properties();
        InetAddress addr = InetAddress.getLocalHost();
        String ip = addr.toString().substring(addr.toString().indexOf("/") + 1, addr.toString().length());
        properties.setProperty(ip, JSON.toJSONString(dataBaseDomain));
        properties.store(fileOutputStream, "");
        fileOutputStream.close();
    }

    @Scheduled(cron = "0 0 0,23 * * ? ")
    public void syncData() throws IOException {
        ApplicationHome home = new ApplicationHome(SyncController.class);
        File jarFile = home.getSource();
        String parent = jarFile.getParent();
        String fileName = parent + File.separator + "syncData.properties";
        if (!Files.exists(Paths.get(fileName))) {
            return;
        }
        FileInputStream fileInputStream = new FileInputStream(fileName);
        Properties properties = new Properties();
        properties.load(fileInputStream);
        Set<Object> objects = properties.keySet();
        for (Object object : objects) {
            String key = (String) object;
            String json = properties.getProperty(key);
            JSONObject jsonObject = JSON.parseObject(json);
            DataBaseDomain dataBaseDomain = jsonObject.toJavaObject(DataBaseDomain.class);
            this.syncDataBase(json);
        }
    }

    public static DataBaseConnection connection(DataBaseDomain dataBaseDomain) throws Exception {
        MyBatisConfigDomain mainMyBatisConfigDomain = new MyBatisConfigDomain();

        String driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        mainMyBatisConfigDomain.setDriverClassName(driverClassName);
        mainMyBatisConfigDomain.setUserName(dataBaseDomain.getMainUserName());
        mainMyBatisConfigDomain.setPassWord(dataBaseDomain.getMainPassWord());
        mainMyBatisConfigDomain.setUrl("jdbc:sqlserver://" + dataBaseDomain.getMainUrl() + ";DatabaseName=" + dataBaseDomain.getMainDataBaseName());

        DataSource mainDataSource = MyBatisConfig.buildDataSource(mainMyBatisConfigDomain);
        LdapContext ldapContext = MyBatisConfig.buildContextSource(dataBaseDomain);
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        dataBaseConnection.setMainDataSource(mainDataSource);
        dataBaseConnection.setLdapContext(ldapContext);
        return dataBaseConnection;
    }

    public static DataBaseConnection connectionReport(DataBaseDomain dataBaseDomain) throws Exception {
        MyBatisConfigDomain mainMyBatisConfigDomain = new MyBatisConfigDomain();

        String driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        mainMyBatisConfigDomain.setDriverClassName(driverClassName);
        mainMyBatisConfigDomain.setUserName(dataBaseDomain.getMainUserName());
        mainMyBatisConfigDomain.setPassWord(dataBaseDomain.getMainPassWord());
        mainMyBatisConfigDomain.setUrl("jdbc:sqlserver://" + dataBaseDomain.getMainUrl() + ";DatabaseName=" + dataBaseDomain.getMainDataBaseName());

        DataSource mainDataSource = MyBatisConfig.buildDataSource(mainMyBatisConfigDomain);
        DataBaseConnection dataBaseConnection = new DataBaseConnection();
        dataBaseConnection.setMainDataSource(mainDataSource);
        return dataBaseConnection;
    }


}
