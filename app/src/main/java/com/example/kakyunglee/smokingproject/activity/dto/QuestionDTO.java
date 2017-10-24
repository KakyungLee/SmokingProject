package com.example.kakyunglee.smokingproject.activity.dto;

// 향후 추가될 가능성이 있음 > db구조와 맞춤 1022
public class QuestionDTO {
	//public int id;
	public int report_category_id;
	public String email;
	public String contents;
	public String image_url;
	
	public int getReport_category_id() {
		return report_category_id;
	}
	public void setReport_category_id(int report_category_id) {
		this.report_category_id = report_category_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	
	
}
