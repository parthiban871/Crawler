package com.example.crawler.entity;

import java.util.List;

public class CrawlerEntity {
	
	
	String urlAddress;
	private List<String> listOfStringsV2;
	/*
	 * public FoundUrl(String currentUrl) { // TODO Auto-generated constructor stub
	 * this.urlAddress=currentUrl; }
	 */

	public List<String> getListOfStringsV2() {
		return listOfStringsV2;
	}

	public void setListOfStringsV2(List<String> listOfStringsV2) {
		this.listOfStringsV2 = listOfStringsV2;
	}

	public String getUrlAddress() {
		return urlAddress;
	}

	public void setUrlAddress(String urlAddress) {
		this.urlAddress = urlAddress;
	}

	

}
