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

	//��ʾ�ĵ�����ı���
	private String title="�γ��б�";
	//�����������
	private String[] rowsName= new String[]{"���","��ĿID","��Ŀ����","��Ŀ����","��Ŀ����","��Ŀ��","��Ŀ�ȼ�"};
//	rowsName[] = new String[]{"���","�����������κ�","���˵���"};
	
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

	//list����
//	ExcelImportService ex=new ExcelImportService();
	
//	dataList=ex.find();
	dataList=list;
	System.out.println(dataList.size()+"��ֵ���С");
	for (Question courseBean : dataList) {
		System.out.println(courseBean+"//");
	}
	ExportquestionServlet ex1 = new ExportquestionServlet();
	System.out.println(ex1);
	try {
		ex1.export(request,response,dataList);
	} catch (Exception e) {
		// TODO �Զ����ɵ� catch ��
		e.printStackTrace();
	}
	
}
public void doPost(HttpServletRequest request, HttpServletResponse response,List<Question> list) throws ServletException, IOException {
	doGet(request,response,list);
}
	
/*
 * ��������
 * */
public void export(HttpServletRequest request, HttpServletResponse response,List<Question> dataList) throws Exception{
	try{
		HSSFWorkbook workbook = new HSSFWorkbook();						// ��������������
		HSSFSheet sheet = workbook.createSheet(title);		 			// ����������
		
		// ������������
        HSSFRow rowm = sheet.createRow(0);
        HSSFCell cellTiltle = rowm.createCell(0);
        
        //sheet��ʽ���塾getColumnTopStyle()/getStyle()��Ϊ�Զ��巽�� - ������  - ����չ��
        HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//��ȡ��ͷ��ʽ����
        HSSFCellStyle style = this.getStyle(workbook);					//��Ԫ����ʽ����
        
        //sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.length-1)));  
        cellTiltle.setCellStyle(columnTopStyle);
        cellTiltle.setCellValue(title);
		
		// ������������
		int columnNum = rowsName.length;
		HSSFRow rowRowName = sheet.createRow(1);				// ������1��λ�ô�����(��˵��п�ʼ�ĵڶ���)
		
		// ����ͷ���õ�sheet�ĵ�Ԫ����
		for(int n=0;n<columnNum;n++){
			HSSFCell  cellRowName = rowRowName.createCell(n);				//������ͷ��Ӧ�����ĵ�Ԫ��
			cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);				//������ͷ��Ԫ�����������
			HSSFRichTextString text = new HSSFRichTextString(rowsName[n]);
			cellRowName.setCellValue(text);									//������ͷ��Ԫ���ֵ
			cellRowName.setCellStyle(columnTopStyle);						//������ͷ��Ԫ����ʽ
		}
		
		//����ѯ�����������õ�sheet��Ӧ�ĵ�Ԫ����
		for(int i=0;i<dataList.size();i++){
			System.out.println(dataList.get(i)+"��i������ "+dataList.size());
			Question obj = dataList.get(i);//����ÿ������
			HSSFRow row = sheet.createRow(i+2);//�������������
			int cellCount = row.getPhysicalNumberOfCells();

			for(int j=0; j<=6; j++){
				/*HSSFCell  cell = null;   //���õ�Ԫ�����������
				if(j == 0){
					cell = row.createCell(j,HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(i+1);	
				}else{
					cell = row.createCell(j,HSSFCell.CELL_TYPE_STRING);
					if(!"".equals(obj[j]) && obj[j] != null){
						cell.setCellValue(obj[j].toString());						//���õ�Ԫ���ֵ
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
		//���п����ŵ������г��Զ���Ӧ
		for (int colNum = 0; colNum < columnNum; colNum++) {
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                //��ǰ��δ��ʹ�ù�
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
 * ��ͷ��Ԫ����ʽ
 */    
	public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
		
		  // ��������
	  HSSFFont font = workbook.createFont();
	  //���������С
	  font.setFontHeightInPoints((short)11);
	  //����Ӵ�
//	  font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	  //������������ 
	  font.setFontName("Courier New");
	  //������ʽ; 
	  HSSFCellStyle style = workbook.createCellStyle();
	  //���õױ߿�; 
// 	  style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	  //���õױ߿���ɫ;  
	  style.setBottomBorderColor(HSSFColor.BLACK.index);
	  //������߿�;   
//	  style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	  //������߿���ɫ; 
	  style.setLeftBorderColor(HSSFColor.BLACK.index);
	  //�����ұ߿�; 
//	  style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	  //�����ұ߿���ɫ; 
	  style.setRightBorderColor(HSSFColor.BLACK.index);
	  //���ö��߿�; 
// 	  style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	  //���ö��߿���ɫ;  
	  style.setTopBorderColor(HSSFColor.BLACK.index);
	  //����ʽ��Ӧ�����õ�����;  
	  style.setFont(font);
	  //�����Զ�����; 
	  style.setWrapText(false);
	  //����ˮƽ�������ʽΪ���ж���;  
//	  style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	  //���ô�ֱ�������ʽΪ���ж���; 
//	  style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	  
	  return style;
	  
	}
	
	/*  
 * ��������Ϣ��Ԫ����ʽ
 */  
	public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
  	  // ��������
  	  HSSFFont font = workbook.createFont();
  	  //���������С
  	  //font.setFontHeightInPoints((short)10);
  	  //����Ӵ�
  	  //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
  	  //������������ 
  	  font.setFontName("Courier New");
  	  //������ʽ; 
  	  HSSFCellStyle style = workbook.createCellStyle();
  	  //���õױ߿�; 
  //	  style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
  	  //���õױ߿���ɫ;  
  	  style.setBottomBorderColor(HSSFColor.BLACK.index);
  	  //������߿�;   
 // 	  style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
  	  //������߿���ɫ; 
  	  style.setLeftBorderColor(HSSFColor.BLACK.index);
  	  //�����ұ߿�; 
  //	  style.setBorderRight(HSSFCellStyle.BORDER_THIN);
  	  //�����ұ߿���ɫ; 
  	  style.setRightBorderColor(HSSFColor.BLACK.index);
  	  //���ö��߿�; 
 // 	  style.setBorderTop(HSSFCellStyle.BORDER_THIN);
  	  //���ö��߿���ɫ;  
  	  style.setTopBorderColor(HSSFColor.BLACK.index);
  	  //����ʽ��Ӧ�����õ�����;  
  	  style.setFont(font);
  	  //�����Զ�����; 
  	  style.setWrapText(false);
  	  //����ˮƽ�������ʽΪ���ж���;  
//  	  style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
  	  //���ô�ֱ�������ʽΪ���ж���; 
 // 	  style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
  	 
  	  return style;
	}

}
