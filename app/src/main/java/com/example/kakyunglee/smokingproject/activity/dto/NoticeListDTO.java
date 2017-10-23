package com.example.kakyunglee.smokingproject.activity.dto;

import java.util.ArrayList;
import java.util.List;

public class NoticeListDTO {
	public List<NoticeDTO> noticeLists=new ArrayList<NoticeDTO>();
	public void getName() {
		for(NoticeDTO notice : noticeLists) {
			System.out.println(notice.getContents());
		}
	}

}
