package com.study.javastudy;

public class Programmer {
	private String name;
	private int level;
	private int salary;
	private int output;//from 1-10
	private String language;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	
	public boolean canManage(Programmer p) {
		return this.level >= p.level;
	}
	
	public int getOutput() {
		return output;
	}
	
	public void setOutput(int output) {
		this.output = output;
	}
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	@Override
	public String toString() {
		return "{ name=" + name + ", level=" + level + ", salary=" + salary + ", output=" + output + ", language="
				+ language + " }";
	}
	
}
