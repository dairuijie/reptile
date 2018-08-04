package com.drj.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @ClassName: ExcelUtil
 * @Description:TODO(处理Excel 的工具类)
 * @author: drj
 * @date: 2018年8月4日 下午2:29:58
 * 
 * @Copyright: 2018
 *
 */
public class ExcelUtil {
    public static String DEFAULT_DATE_PATTERN = "yyyy年MM月dd日";// 默认日期格式
    public static String NO_DEFINE = "no_define";// 未定义的字段
    public static int DEFAULT_COLOUMN_WIDTH = 17;// 默认宽度
    public static final String DIR_PATH = "D://temp//drjyy.xlsx";
    public static String arry[] = { "21", "121", "23", "83", "25", "27", "41", "43", "45", "47", "61", "81", "666",
            "999", "101", "103", "111", "113", "141", "143", "151", "153" };

    /**
     * 不带表头的Excel
     * 
     * @param title
     * @param headMap
     * @param jsonArray
     * @param datePattern
     * @param colWidth
     * @param out
     */
    public static void exportToExcel(Map<String, String> headMap, JSONArray jsonArray, String datePattern, int colWidth,
            OutputStream out) {
        if (datePattern == null)
            datePattern = DEFAULT_DATE_PATTERN;
        // 声明一个工作薄
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);// 缓存
        workbook.setCompressTempFiles(true);

        // 生成一个表格
        SXSSFSheet sheet = workbook.createSheet();
        // 设置列宽
        int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;// 至少字节数
        int[] arrColWidth = new int[headMap.size()];
        // 产生表格标题行,以及设置列宽
        String[] properties = new String[headMap.size()];
        String[] headers = new String[headMap.size()];
        int ii = 0;
        for (Iterator<String> iter = headMap.keySet().iterator(); iter.hasNext();) {
            String fieldName = iter.next();
            properties[ii] = fieldName;
            headers[ii] = headMap.get(fieldName);
            int bytes = fieldName.getBytes().length;
            arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
            sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
            ii++;
        }
        // 遍历集合数据，产生数据行
        int rowIndex = 0;
        for (Object obj : jsonArray) {
            if (rowIndex == 65535 || rowIndex == 0) {
                if (rowIndex != 0)
                    sheet = workbook.createSheet();// 如果数据超过了，则在第二页显示

                SXSSFRow headerRow = sheet.createRow(0); // 列头 rowIndex =1
                for (int i = 0; i < headers.length; i++) {
                    headerRow.createCell(i).setCellValue(headers[i]);
                }
                rowIndex = 1;// 数据内容从 rowIndex=1开始
            }
            JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
            SXSSFRow dataRow = sheet.createRow(rowIndex);
            for (int i = 0; i < properties.length; i++) {
                SXSSFCell newCell = dataRow.createCell(i);

                Object o = jo.get(properties[i]);
                String cellValue = "";
                if (o == null)
                    cellValue = "";
                else if (o instanceof Date)
                    cellValue = new SimpleDateFormat(datePattern).format(o);
                else if (o instanceof Float || o instanceof Double)
                    cellValue = new BigDecimal(o.toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                else
                    cellValue = o.toString();

                newCell.setCellValue(cellValue);
            }
            rowIndex++;
        }
        // 自动调整宽度
        sheet.trackAllColumnsForAutoSizing();
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        try {
            workbook.write(out);
            workbook.close();
            // boolean flag = workbook.dispose();//释放磁盘空间。处理在磁盘上支持这个工作簿的临时文件。调用该方法将使工作簿不可用。
            // System.out.println(flag);//如果所有临时文件都被成功删除，则为真。
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将数据导出追加到已存在excel中
     * 
     * @param jsonArray
     */
    public static void DealExcel(JSONArray jsonArray) {
        XSSFWorkbook workBook = null;
        FileOutputStream out = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(DIR_PATH); // 获取d://test.xls,建立数据的输入通道
            System.out.println(fileInputStream);
            workBook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workBook.getSheet("Sheet0"); // 根据name获取sheet表
            XSSFRow row = sheet.getRow(0); // 获取第一行
            System.out.println(sheet.getLastRowNum() + " " + row.getLastCellNum()); // 分别得到最后一行的行号，和一条记录的最后一个单元格
            out = new FileOutputStream(DIR_PATH);
            row = sheet.createRow((short) (sheet.getLastRowNum() + 1)); // 对总行数减4，就是倒数行数加数据
            for (Object obj : jsonArray) {// 这里是我的需求 大家根据自己的需求重新写
                JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
                for (int i = 0; i < 22; i++) {
                    row.createCell(i).setCellValue(jo.get(arry[i]) == null ? "" : jo.get(arry[i]).toString());
                }
            }
            out.flush();
            workBook.write(out);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (workBook != null) {
                try {
                    workBook.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
