package com.hgh.test;

import com.hgh.dao.SecDao;
import com.hgh.domain.Class1;
import com.hgh.domain.User;
import com.hgh.service.SecService;
import com.hgh.service.UserService;
import com.hgh.servlet.SecServlet;

public class testaddclass {
	 public static void main(String[] args) {
	    	
	    SecService secService=new SecService();
	  /*  Class1 class1=new Class1();
	    class1.setClassname("计科172班");
	    class1.setDepart("计算机科学与技术");
	    class1.setManager("");
	    class1.setPeople(41);
	    	secService.addclass(class1);
		}*/
	    secService.find("计算机科学与技术", "", 1);
	   
}
}
