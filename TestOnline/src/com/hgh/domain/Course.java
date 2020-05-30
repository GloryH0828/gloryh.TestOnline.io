package com.hgh.domain;

public class Course {

	int id;
	String coursename;
	String courseteacher;
	int state;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCoursename() {
		return coursename;
	}
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	public String getCourseteacher() {
		return courseteacher;
	}
	public void setCourseteacher(String courseteacher) {
		this.courseteacher = courseteacher;
	}
	@Override
	public String toString() {
		return "Course [id=" + id + ", coursename=" + coursename + ", courseteacher=" + courseteacher + ", state="
				+ state + "]";
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
}
