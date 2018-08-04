package com.drj.reptile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.drj.util.ExcelUtil;
import com.drj.util.HttpClient4Utils;
/**
 * 
 * @ClassName:  Reptile   
 * @Description:TODO(获取网页数据导出到excel)   
 * @author: drj 
 * @date:   2018年8月4日 下午3:03:35   
 *     
 * @Copyright: 2018 
 *
 */
public class Reptile {
    static String uri = "http://cmispub.cicpa.org.cn/cicpa2_web/07/000000F2D6275597F15EB070CCF21646.shtml";
    public static StringBuilder URL = new StringBuilder(uri);// http://www.itcast.cn/
    public static Integer len = URL.length() - 5;
    public static Integer count = 1;
    public static String reps = null;
    public static String DEFAULT_DATE_PATTERN = "yyyy年MM月dd日";// 默认日期格式

    public static void main(String[] args) {
        try {
            List<Map<String, Object>> info = new ArrayList<Map<String, Object>>();
            info.add(Reptile.selectInfo());
            exportExcle(info);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 通过Url 抓取需要的网页数据
     * @return
     */
    public static Map<String, Object> selectInfo() {
        Map<String, Object> map = new HashMap<String, Object>();
        reps = HttpClient4Utils.sendPost(URL.toString(), null);
        Document doc = Jsoup.parse(reps);
        Elements links = doc.select("table").eq(3);
        for (int i = 2; i <= 8; i = i + 2) {
            Elements trs = links.select("tr").eq(i);
            Elements tds = trs.select("td").attr("class", "data_tb_content");
            for (int j = 0; j < tds.size(); j++) {
                if (j % 2 == 1) {
                    System.out.println(tds.get(j).text());
                    map.put(String.valueOf(i) + String.valueOf(j), tds.get(j).text());
                }
            }
        }
        for (int i = 10; i <= 12; i++) {
            Elements trs = links.select("tr").eq(i);
            Elements tds = trs.select("td").attr("class", "data_tb_content");
            for (int j = 0; j < tds.size(); j++) {
                if (j % 2 == 1) {
                    System.err.println(tds.get(j).text());
                    map.put(String.valueOf(i) + String.valueOf(j), tds.get(j).text());
                }
            }
        }
        for (int i = 14; i <= 15; i++) {
            Elements trs = links.select("tr").eq(i);
            Elements tds = trs.select("td").attr("class", "data_tb_content");
            for (int j = 0; j < tds.size(); j++) {
                if (j % 2 == 1) {
                    System.err.println(tds.get(j).text());
                    map.put(String.valueOf(i) + String.valueOf(j), tds.get(j).text());
                }
            }
        }
        return map;
    }

    /**
     * 
     * @param userApplyMap
     */
    public static void exportExcle(List<Map<String, Object>> userApplyMap) {
        ServletOutputStream outputStream = null;
        FileInputStream fileInputStream = null;
        try {
            JSONArray userApplyInfos = new JSONArray();
            if (!userApplyMap.isEmpty()) {// 数据集
                for (Map<String, Object> map : userApplyMap) {
                    com.alibaba.fastjson.JSONObject userApply = new com.alibaba.fastjson.JSONObject();
                    userApply.put("21", map.get("21"));
                    userApply.put("121", map.get("121"));
                    userApply.put("23", map.get("23"));
                    userApply.put("83", map.get("83"));
                    userApply.put("25", map.get("25"));
                    userApply.put("27", map.get("27"));
                    userApply.put("41", map.get("41"));
                    userApply.put("43", map.get("43"));
                    userApply.put("45", map.get("45"));
                    userApply.put("47", map.get("47"));
                    userApply.put("61", map.get("61"));
                    userApply.put("81", map.get("81"));
                    userApply.put("666", map.get("666"));
                    userApply.put("999", map.get("999"));
                    userApply.put("101", map.get("101"));
                    userApply.put("103", map.get("103"));
                    userApply.put("111", map.get("111"));
                    userApply.put("113", map.get("113"));
                    userApply.put("141", map.get("141"));
                    userApply.put("143", map.get("143"));
                    userApply.put("151", map.get("151"));
                    userApply.put("153", map.get("153"));
                    userApplyInfos.add(userApply);
                }
                /**
                 * 重新生成需要的头部信息
                 */
                /*
                 * Map<String, String> headMap = new LinkedHashMap<String, String>();// 存放表头部信息
                 * headMap.put("21", "性名 "); headMap.put("121", "所在事务所"); headMap.put("23",
                 * "性别 "); headMap.put("83", "全科合格年份"); headMap.put("25", "所内职务 ");
                 * headMap.put("27", "是否党员"); headMap.put("41", "学历"); headMap.put("43", "学位");
                 * headMap.put("45", "所学专业"); headMap.put("47", "毕业学校"); headMap.put("61",
                 * "资格取得方式（考试/考核)"); headMap.put("81", "全科合格证书号"); headMap.put("666", "考核批准文号");
                 * headMap.put("999", "批准时间"); headMap.put("101", "注册会计师证书编号");
                 * headMap.put("103", "是否合伙人（股东)"); headMap.put("111", "批准注册文件号");
                 * headMap.put("113", "批准注册时间"); headMap.put("141", "本年度应完成学时");
                 * headMap.put("143", "本年度已完成学时"); headMap.put("151", "惩戒及处罚信息(披露时限:自2014年至今)");
                 * headMap.put("153", "参加公益活动");
                 */
                // 生成文件临时存放目录
                /**
                 * 重新生成新的excel
                 */
                /*
                 * OutputStream outXlsx = new FileOutputStream(ExcelUtil.DIR_PATH);
                 * ExcelUtil.exportToExcel(headMap, userApplyInfos, null,
                 * ExcelUtil.DEFAULT_COLOUMN_WIDTH, outXlsx); outXlsx.close();
                 */
                ExcelUtil.DealExcel(userApplyInfos);// 追加excel
            } else {
                System.out.println("ERRO");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (outputStream != null)
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
    }
}
