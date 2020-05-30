package com.hgh.domain;

public class Test {

	int id;
	int questionid;
	String questionname;
	String questionmatter;
	String coursename;
	String answer;
	int level;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuestionid() {
		return questionid;
	}
	public void setQuestionid(int questionid) {
		this.questionid = questionid;
	}
	@Override
	public String toString() {
		return "Test [id=" + id + ", questionid=" + questionid + ", questionname=" + questionname + ", questionmatter="
				+ questionmatter + ", coursename=" + coursename + ", answer=" + answer + ", level=" + level + "]";
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
	public String getCoursename() {
		return coursename;
	}
	public void setCoursename(String coursename) {
		this.coursename = coursename;
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
}
