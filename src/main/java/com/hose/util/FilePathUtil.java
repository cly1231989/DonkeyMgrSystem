package com.hose.util;

import java.io.File;

public class FilePathUtil {
	
	private static String appName = "DonkeyMgrSystem";
	
	public static String getWebRootPath(){

		String path = FilePathUtil.class.getResource("/").getFile().toString();//new File("/").getAbsolutePath();//getClass().getResource("/").getFile().toString();
		//path = path.substring(0, path.indexOf("WEB-INF"));
		return path.substring(0, path.indexOf(appName) + appName.length()) + "\\";
	}
	
	public static String getImgPathWriteToDB(Integer sn){
		String imgPathWriteToDB = "assets\\images\\";

		Integer subDir = sn / 1000;
		if(sn % 1000 != 0){
			subDir++;
		}
		imgPathWriteToDB += subDir * 1000;
		imgPathWriteToDB += "\\";
		imgPathWriteToDB += sn;
		return imgPathWriteToDB;
	}
}
