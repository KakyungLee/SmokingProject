package com.example.kakyunglee.smokingproject.activity.dto;

import java.math.BigDecimal;
import java.util.Date;
// 1022 디비와 연동 완료
public class ReportDTO {


	int no;
	BigDecimal latitude;
	BigDecimal longitude;
	Date created_at=new Date();

	int report_detail_id;
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public BigDecimal getLatitude() {
		return latitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	public BigDecimal getLongitude() {
		return longitude;
	}
	public void setLongititude(BigDecimal longitude) {
		this.longitude = longitude;
	}
	public Date getCreated_at() {
		return created_at;
	}

	public int getReport_detail_id() {
		return report_detail_id;
	}
	public void setReport_detail_id(int report_detail_id) {
		this.report_detail_id = report_detail_id;
	}
	public void setLongtitude(BigDecimal longtitude) {
		this.longitude = longtitude;
	}
	
	public String toString() {
		return this.getNo()+","+this.getLatitude()+","+this.getLongitude()+","+this.getCreated_at()+","+this.getReport_detail_id();
	}
	
}
