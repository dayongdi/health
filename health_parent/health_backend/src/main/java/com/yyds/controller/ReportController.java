package com.yyds.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.javamenc.constant.MessageConstant;
import com.javamenc.entity.Result;
import com.javamenc.utils.DateUtils;
import com.yyds.Service.MemberService;
import com.yyds.Service.ReportService;
import com.yyds.Service.SetMealService;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")

@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_HEALTH_MANAGER')")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetMealService setMealService;

    @Reference
    private ReportService reportService;

    //获取前12个月 每个月底之前的会员数,并返回给折线图数据展示
    @RequestMapping("/getMemberReport")
    public Result GetMemberReport(){
        try {
            Map<String,Object> map= null;
            map = new HashMap<>();
            //获取前十二个月的的所有月份
            List<String> months=new ArrayList<>();
            //获取实例对象
            Calendar calendar=Calendar.getInstance();
            //往前推12个月
            calendar.add(Calendar.MONTH,-12);

            Date date=null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONTH, 1);
                date = calendar.getTime();
                months.add(simpleDateFormat.format(date));
            }
            map.put("months", months);
            // 获取十二个月中每个月之前的所有会员数量
            List<Integer> memberCount = memberService.findMemberCountByMonths(months);
            map.put("memberCount", memberCount);

            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    //获取套餐预约占比
    @RequestMapping("/getSetmealReport")
    public Result GetSetmealReport(){
        try{
            Map<String,Object> data= new HashMap<>();
            // 查询每个套餐对应的数量(占比数据)
            List<Map<String, Object>> setmealCount = setMealService.findSetmealCount();
            data.put("setmealCount",setmealCount);
            // 获取所有的套餐名称
            List<String> setmealNames = new ArrayList<>();
            for (Map<String,Object> map: setmealCount) {
                //将名称由对象转为String格式
                String name = (String)map.get("name");
                setmealNames.add(name);
            }
            data.put("setmealNames",setmealNames);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    //获取所有的运营数据
    @RequestMapping("/getBusinessReportData")
    public Result GetBusinessReportData(){
        try {
            Map<String,Object> data=new HashMap<>();
            data=reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,data);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    // 编辑xlsx模板文件, 然后下载
    @GetMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String, Object> result = reportService.getBusinessReportData();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = DateUtils.parseDate2String((Date) result.get("reportDate"));
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            // 获取模板文件的绝对路径
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            // 基于提供的excel模板文件在内存中创建一个excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            // 读取第一个工作表
            XSSFSheet sheet = excel.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);    //日期
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);        //新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);           //总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);     //本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);    //本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);      //今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);     //今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);   //本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);  //本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);  //本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber); //本月到诊数

            int rowNum = 12;

            for (Map map : hotSetmeal) {
                // 获取每一个套餐的运营数据
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");

                // 每一个套餐对应着一行单元格
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);                      //套餐名称
                row.getCell(5).setCellValue(setmeal_count);             //预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());  //占比
            }

            // 使用输出流进行表格下载,基于浏览器作为客户端下载
            // 将该流返回到前端  前端完成自动下载
            ServletOutputStream out = response.getOutputStream();
            //代表的是Excel文件类型
            response.setContentType("application/vnd.ms-excel");
            //指定以附件形式进行下载
            response.setHeader("Content-Disposition","attachment;filename=report.xlsx");

            excel.write(out);

            out.flush();
            out.close();
            excel.close();

            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    // 编辑PDF模板文件, 然后下载
    @GetMapping("/exportBusinessReport4PDF")
    public Result exportBusinessReport4PDF(HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String, Object> result = reportService.getBusinessReportData();

            Date reportDate = (Date)result.get("reportDate");
            String s = DateUtils.parseDate2String(reportDate);
            result.put("reportDate",s);

            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");    // 将集合数据 单独取出来使用
            // 模板文件与编译后的"字节码"文件的路径
            //与系统有关的默认名称分隔符。此字段被初始化为包含系统属性
            //file.separator 值的第一个字符。在 UNIX 系统上，此字段的值为 '/'；在 Microsoft Windows 系统上，它为 '\'。
            String jrxmlPath = request.getSession().getServletContext().getRealPath("template") +
                    File.separator + "health_business3.jrxml";
            String jasperPath = request.getSession().getServletContext().getRealPath("template") +
                    File.separator + "health_business3.jasper";

            // 编译模板
            JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);

            // 填充数据  使用 JavaBean数据源的方式填充
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, result, new JRBeanCollectionDataSource(hotSetmeal));

            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/pdf");   //代表的是pdf文件类型
            response.setHeader("Content-Disposition","attachment;filename=report.pdf");    //指定以附件形式进行下载

            JasperExportManager.exportReportToPdfStream(jasperPrint, out);  // 输出文件


            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }


}
