package com.hgh.domain;

public class Question {

	int id;
	String coursename;
	String questionname;
	String questionmatter;
	String answer;
	String type;
	String A;
	String B;
	String C;
	String D;
	int level;
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
	public String getQuestionname() {
		return questionname;
	}
	public void setQuestionname(String questionname) {
		this.questionname = questionname;
	}
	public String getQuestionmatter() {
		return questionmatter;
	}
	public void setQuestionmatter(String questionmatter) {
		this.questionmatter = questionmatter;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	@Override
	public String toString() {
		return "Question [id=" + id + ", coursename=" + coursename + ", questionname=" + questionname
				+ ", questionmatter=" + questionmatter + ", answer=" + answer + ", type=" + type + ", A=" + A + ", B="
				+ B + ", C=" + C + ", D=" + D + ", level=" + level + "]";
	}
	public String getA() {
		return A;
	}
	public void setA(String a) {
		A = a;
	}
	public String getB() {
		return B;
	}
	public void setB(String b) {
		B = b;
	}
	public String getC() {
		return C;
	}
	public void setC(String c) {
		C = c;
	}
	public String getD() {
		return D;
	}
	public void setD(String d) {
		D = d;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
