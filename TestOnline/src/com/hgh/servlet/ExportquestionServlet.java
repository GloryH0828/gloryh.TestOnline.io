package com.hgh.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;

import com.hgh.domain.Course;
import com.hgh.domain.Question;

public class ExportquestionServlet extends HttpServlet {

	//显示的导出表的标题
	private String title="课程列表";
	//导出表的列名
	private String[] rowsName= new String[]{"序号","题目ID","题目名称","题目内容","题目类型","题目答案","题目等级"};
//	rowsName[] = new String[]{"序号","货物运输批次号","提运单号"};
	
	public List<Question>  dataList = new ArrayList<Question>();
	
	HttpServletResponse  response;
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public void setRowsName(String[] rowsName) {
		this.rowsName = rowsName;
	}
	public String[] getRowsName() {
		return rowsName;
	}

	
public void doGet(HttpServletRequest request, HttpServletResponse response,List<Question> list) throws ServletException, IOException {

	//list数据
//	ExcelImportService ex=new ExcelImportService();
	
//	dataList=ex.find();
	dataList=list;
	System.out.println(dataList.size()+"赋值后大小");
	for (Question courseBean : dataList) {
		System.out.println(courseBean+"//");
	}
	ExportquestionServlet ex1 = new ExportquestionServlet();
	System.out.println(ex1);
	try {
		ex1.export(request,response,dataList);
	} catch (Exception e) {
		// TODO 自动生成的 catch 块
		e.printStackTrace();
	}
	
}
public void doPost(HttpServletRequest request, HttpServletResponse response,List<Question> list) throws ServletException, IOException {
	doGet(request,response,list);
}
	
/*
 * 导出数据
 * */
public void export(HttpServletRequest request, HttpServletResponse response,List<Question> dataList) throws Exception{
	try{
		HSSFWorkbook workbook = new HSSFWorkbook();						// 创建工作簿对象
		HSSFSheet sheet = workbook.createSheet(title);		 			// 创建工作表
		
		// 产生表格标题行
        HSSFRow rowm = sheet.createRow(0);
        HSSFCell cellTiltle = rowm.createCell(0);
        
        //sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】
        HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获取列头样式对象
        HSSFCellStyle style = this.getStyle(workbook);					//单元格样式对象
        
        //sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.length-1)));  
        cellTiltle.setCellStyle(columnTopStyle);
        cellTiltle.setCellValue(title);
		
		// 定义所需列数
		int columnNum = rowsName.length;
		HSSFRow rowRowName = sheet.createRow(1);				// 在索引1的位置创建行(最顶端的行开始的第二行)
		
		// 将列头设置到sheet的单元格中
		for(int n=0;n<columnNum;n++){
			HSSFCell  cellRowName = rowRowName.createCell(n);				//创建列头对应个数的单元格
			cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);				//设置列头单元格的数据类型
			HSSFRichTextString text = new HSSFRichTextString(rowsName[n]);
			cellRowName.setCellValue(text);									//设置列头单元格的值
			cellRowName.setCellStyle(columnTopStyle);						//设置列头单元格样式
		}
		
		//将查询出的数据设置到sheet对应的单元格中
		for(int i=0;i<dataList.size();i++){
			System.out.println(dataList.get(i)+"第i个数据 "+dataList.size());
			Question obj = dataList.get(i);//遍历每个对象
			HSSFRow row = sheet.createRow(i+2);//创建所需的行数
			int cellCount = row.getPhysicalNumberOfCells();

			for(int j=0; j<=6; j++){
				/*HSSFCell  cell = null;   //设置单元格的数据类型
				if(j == 0){
					cell = row.createCell(j,HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(i+1);	
				}else{
					cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
					if(!"".equals(obj[j]) && obj[j] != null){
						cell.setCellValue(obj[j].toString());						//设置单元格的值
					}
				}*/
				
				Cell cell = row.getCell(j);
				if(j==0){
					cell = row.createCell(j,HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(i+1);
				}
				if(j==1){
					cell = row.createCell(j,HSSFCell.CELL_TYPE_NUMERIC);
//					cell.setCellValue(obj.getClassname());
					cell.setCellValue(obj.getId());
				}
				if(j==2){
					cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
//					cell.setCellValue(obj.getTab());
					cell.setCellValue(obj.getQuestionname());
				}
				if(j==3){
					cell = row.createCell(j,HSSFCell.CELL_TYPE_NUMERIC);
//					cell.setCellValue(obj.getTab());
					cell.setCellValue(obj.getQuestionmatter());
				}
				if(j==4){
					cell = row.createCell(j,HSSFCell.CELL_TYPE_NUMERIC);
//					cell.setCellValue(obj.getTab());
					cell.setCellValue(obj.getCoursename());
				}
				if(j==5){
					cell = row.createCell(j,HSSFCell.CELL_TYPE_NUMERIC);
//					cell.setCellValue(obj.getTab());
					cell.setCellValue(obj.getAnswer());
				}
				if(j==6){
					cell = row.createCell(j,HSSFCell.CELL_TYPE_NUMERIC);
//					cell.setCellValue(obj.getTab());
					cell.setCellValue(obj.getLevel());
				}
			}
		}
		//让列宽随着导出的列长自动适应
		for (int colNum = 0; colNum < columnNum; colNum++) {
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
                if (currentRow.getCell(colNum) != null) {
                    HSSFCell currentCell = currentRow.getCell(colNum);
                    if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            if(colNum == 0){
            	sheet.setColumnWidth(colNum, (columnWidth-2) * 256);
            }else{
            	sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
            }
        }
		
		if(workbook !=null){
			try
		    {
		        String fileName = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";
		        String headStr = "attachment; filename=\"" + fileName + "\"";
		       // response = getResponse();
		        response.setContentType("APPLICATION/OCTET-STREAM");
		        response.setHeader("Content-Disposition", headStr);
		        OutputStream out = response.getOutputStream();
		        workbook.write(out);
		    }
		    catch (IOException e)
		    {
		        e.printStackTrace();
		    }
		}

	}catch(Exception e){
		e.printStackTrace();
	}
	
}

/* 
 * 列头单元格样式
 */    
	public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
		
		  // 设置字体
	  HSSFFont font = workbook.createFont();
	  //设置字体大小
	  font.setFontHeightInPoints((short)11);
	  //字体加粗
//	  font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	  //设置字体名字 
	  font.setFontName("Courier New");
	  //设置样式; 
	  HSSFCellStyle style = workbook.createCellStyle();
	  //设置底边框; 
// 	  style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	  //设置底边框颜色;  
	  style.setBottomBorderColor(HSSFColor.BLACK.index);
	  //设置左边框;   
//	  style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	  //设置左边框颜色; 
	  style.setLeftBorderColor(HSSFColor.BLACK.index);
	  //设置右边框; 
//	  style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	  //设置右边框颜色; 
	  style.setRightBorderColor(HSSFColor.BLACK.index);
	  //设置顶边框; 
// 	  style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	  //设置顶边框颜色;  
	  style.setTopBorderColor(HSSFColor.BLACK.index);
	  //在样式用应用设置的字体;  
	  style.setFont(font);
	  //设置自动换行; 
	  style.setWrapText(false);
	  //设置水平对齐的样式为居中对齐;  
//	  style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	  //设置垂直对齐的样式为居中对齐; 
//	  style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	  
	  return style;
	  
	}
	
	/*  
 * 列数据信息单元格样式
 */  
	public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
  	  // 设置字体
  	  HSSFFont font = workbook.createFont();
  	  //设置字体大小
  	  //font.setFontHeightInPoints((short)10);
  	  //字体加粗
  	  //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
  	  //设置字体名字 
  	  font.setFontName("Courier New");
  	  //设置样式; 
  	  HSSFCellStyle style = workbook.createCellStyle();
  	  //设置底边框; 
  //	  style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
  	  //设置底边框颜色;  
  	  style.setBottomBorderColor(HSSFColor.BLACK.index);
  	  //设置左边框;   
 // 	  style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
  	  //设置左边框颜色; 
  	  style.setLeftBorderColor(HSSFColor.BLACK.index);
  	  //设置右边框; 
  //	  style.setBorderRight(HSSFCellStyle.BORDER_THIN);
  	  //设置右边框颜色; 
  	  style.setRightBorderColor(HSSFColor.BLACK.index);
  	  //设置顶边框; 
 // 	  style.setBorderTop(HSSFCellStyle.BORDER_THIN);
  	  //设置顶边框颜色;  
  	  style.setTopBorderColor(HSSFColor.BLACK.index);
  	  //在样式用应用设置的字体;  
  	  style.setFont(font);
  	  //设置自动换行; 
  	  style.setWrapText(false);
  	  //设置水平对齐的样式为居中对齐;  
//  	  style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
  	  //设置垂直对齐的样式为居中对齐; 
 // 	  style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
  	 
  	  return style;
	}

}
