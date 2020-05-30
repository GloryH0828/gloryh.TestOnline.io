package com.hgh.domain;

import java.util.List;
import java.util.Map;

public class Page {
	private int pageSize=4;//ÿҳ16��
	private int currentPage;//��ǰҳ��ҳ�洫��
	private int totalSize;//����������ѯ���ݿ����
//	private int totalPage;//������ҳ
//	private int startIndex;//��ʼ��¼����
	private List<Map<String, Object>> list;//��ǰҳ�������б���ȡ���ݿ��ȡ
	private int num=6;//ҳ����ʾ��ҳ�����
//	private int start;//ҳ����ʾҳ�����ʼֵ
//	private int end;//ҳ����ʾҳ�����ֵֹ
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
	//��iҳ   ��i-1��*x����ҳ��С��
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

