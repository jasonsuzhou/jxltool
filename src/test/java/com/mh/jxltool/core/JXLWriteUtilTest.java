package com.mh.jxltool.core;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.mh.jxltool.config.ExportExcelConfig;

public class JXLWriteUtilTest {
	
	@Test
	public void generateExcel() {
		List<User> list = prepareUserList();
		try {
			File configFile = new File("./src/test/resources/exportConfig-test.xml");
			ExportExcelConfig.initConfig(configFile);
			File outputFile = new File("/Users/jasonyao/git/jxltool/test.xls");
			ExcelWriteUtil<User> writeUtil = new JXLWriteUtil<User>(new FileOutputStream(outputFile));
			writeUtil.generateExcel(list, ExportExcelConfig.getExportBean("demo"), "testExport");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	class User {
		private String username;
		private int age;
		private Date birthday;
		private double balance;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public Date getBirthday() {
			return birthday;
		}
		public void setBirthday(Date birthday) {
			this.birthday = birthday;
		}
		public double getBalance() {
			return balance;
		}
		public void setBalance(double balance) {
			this.balance = balance;
		}
	}
	
	private List<User> prepareUserList() {
		User user1 = new User();
		user1.setAge(23);
		user1.setBalance(12313132);
		user1.setBirthday(new Date());
		user1.setUsername("salk");
		
		User user2 = new User();
		user2.setAge(23);
		user2.setBalance(12313132);
		user2.setBirthday(new Date());
		user2.setUsername("jasonyao");
		List<User> list = new ArrayList<User>();
		list.add(user1);
		list.add(user2);
		return list;
	}

}
