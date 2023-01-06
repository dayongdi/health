package com.javamenc.utils;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//poi工具类解析xlsx文件
public class POIUtils {
    //不可更改常量,防串改
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    private final static String DATE_FORMAT = "yyyy/MM/dd";


    public static List<String[]> readExcel(MultipartFile file) throws Exception {
        //检测文件是否合法
        checkFile(file);
        //获取工作簿
        Workbook workbook=getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<String[]> list=new ArrayList<String[]>();
        if (workbook!=null){
            //遍历工作簿里的工作表
            for (int sheetNum=0;sheetNum<workbook.getNumberOfSheets();sheetNum++){
                Sheet sheet=workbook.getSheetAt(sheetNum);
                //如果工作表为空则跳过
                if (sheet==null){
                    continue;
                }
                int firstRowNum = sheet.getFirstRowNum();
                int lastRowNum = sheet.getLastRowNum();
                //遍历除了第一行的所有行
                for (int rowNum=firstRowNum+1;rowNum<=lastRowNum;rowNum++){
                    Row row = sheet.getRow(rowNum);
                    if (row==null){
                        continue;
                    }
                    //getPhysicalNumberOfCells 是获取不为空的列个数。

                    short firstCellNum = row.getFirstCellNum();
                    short lastCellNum = row.getLastCellNum();
                    String[] cells = new String[row.getPhysicalNumberOfCells()];

                    for (int cellNum=firstCellNum;cellNum<lastCellNum;cellNum++){
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum]= getCellValue(cell);
                    }

                    list.add(cells);
                }
            }

        }



        return list;
    }


    //检查文件格式是否合法
    public static void checkFile(MultipartFile file) throws Exception {
        if (file==null){
            throw new FileNotFoundException("文件不存在");
        }
        //获取文件名并判断
        String filename = file.getOriginalFilename();
        if(!filename.endsWith(xls)&&!filename.endsWith(xlsx)){
            throw new IOException("文件格式不正确");
        }
    }

    //根据版本创建workbook
    public static Workbook getWorkBook(MultipartFile file){
        String fileName= file.getOriginalFilename();
        //创建工作簿
        Workbook workbook=null;
        try {
            //获取文件输入流
            InputStream inputStream = file.getInputStream();

            if (fileName.endsWith(xls)){
                //兼容旧版xls
                workbook =new HSSFWorkbook(inputStream);
            }else if (fileName.endsWith(xlsx)){
                //新版的xlsx
                workbook=new XSSFWorkbook(inputStream);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return workbook;
    }

    public static String getCellValue(Cell cell){
        String cellValue="";
        if(cell == null){
            return cellValue;
        }

        //如果当前单元格内容为日期类型，需要特殊处理
        String dataFormatString = cell.getCellStyle().getDataFormatString();
        if(dataFormatString.equals("m/d/yy")){
            cellValue = new SimpleDateFormat(DATE_FORMAT).format(cell.getDateCellValue());
            return cellValue;
        }
        //把数字当成String来读，避免出现1读成1.0的情况
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }

        //判断数据的类型
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }



        return cellValue;
    }
}
