package com.hgh.domain;

import java.util.List;
import java.util.Map;

public class Page {
	private int pageSize=4;//每页16条
	private int currentPage;//当前页，页面传递
	private int totalSize;//总条数，查询数据库得来
//	private int totalPage;//共多少页
//	private int startIndex;//起始记录索引
	private List<Map<String, Object>> list;//当前页包含的列表，读取数据库获取
	private int num=6;//页面显示的页码个数
//	private int start;//页面显示页码的起始值
//	private int end;//页面显示页码的终止值
	public Page(int currentPage,int totalSize){
		this.totalSize=totalSize;
		setCurrentPage(currentPage);
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		int totalPage=getCurrentPage();
		if(currentPage<1){
			currentPage=1;
		}
		if (currentPage>totalPage) {
			this.currentPage=totalPage;
		}
		this.currentPage = currentPage;
	}
	public int getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	//第i页   （i-1）*x（分页大小）
	public int getStartIndex() {
		return (currentPage-1)*pageSize;
	}
	public List<Map<String, Object>> getList() {
		return list;
	}
	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getStart() {
		int start=currentPage-num/2;
		if(start<1){
			start=1;
		}
		return start;
	}
	
	public int getEnd() {
		int end=getStart()+num-1;
		int totalPage =getTotalPage();
		if(end>totalPage){
			end=totalPage;
		}
		return end;
	}
	
	public int getTotalPage() {
		return (totalSize%pageSize==0)?(totalSize/pageSize):(totalSize/pageSize+1);
	}
	@Override
	public String toString() {
		return "Page [pageSize=" + pageSize + ", currentPage=" + currentPage + ", totalSize=" + totalSize + ", list="
				+ list + ", num=" + num + ", start=" + getStart() + ", end=" + getEnd() + ", totalPage="
				+ getTotalPage() + "]";
	}
	
}

