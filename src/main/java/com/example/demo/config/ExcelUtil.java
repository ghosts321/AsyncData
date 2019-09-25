package com.example.demo.config;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.*;

import java.util.List;

/**
 * @ Description   :  excel导出工具类
 * @ Author        :  JiangJunpeng
 * @ CreateDate    :  2019/9/7 11:41
 * @ UpdateUser    :  JiangJunpeng
 * @ UpdateDate    :  2019/9/7 11:41
 * @ UpdateRemark  :  修改内容
 * @ Version       :  1.0
 */
public class ExcelUtil {

   /**
    * Excel导出多个Sheet页
    *author Jiangjunpeng 2017年10月27日
    * @param workbook
    * @param sheetNum
    * @param sheetTitle
    * @param headers
    * @param result
    */
	public void exportExcel(XSSFWorkbook workbook, int sheetNum, String sheetTitle, String[] headers, List<List<String>> result) {
		// 第一步，创建一个webbook，对应一个Excel以xslx为扩展名文件
		XSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(sheetNum, sheetTitle);
		// 设置列宽度大小
		sheet.setDefaultColumnWidth((short) 14);
		// 第二步， 生成表格第一行的样式和字体
		XSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.ORANGE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		XSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		// 设置字体所在的行高度
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 指定当单元格内容显示不下时自动换行
		style.setWrapText(true);
		// 产生表格标题行
		XSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			XSSFCell cell = row.createCell((short) i);
			cell.setCellStyle(style);
			XSSFRichTextString text = new XSSFRichTextString(headers[i]);
			cell.setCellValue(text.toString());
		}
		// 第三步：遍历集合数据，产生数据行，开始插入数据
		if (result != null) {
			int index = 1;
			for (List<String> m : result) {
				row = sheet.createRow(index);
				int cellIndex = 0;
				for (String str : m) {
					XSSFCell cell = row.createCell(cellIndex);
					if(str != null && !"".equals(str)){
						cell.setCellValue(str);
						cellIndex++;
					}else{
						cell.setCellValue("");
						cellIndex++;
					}
				}
				index++;
			}
		}

	}
}