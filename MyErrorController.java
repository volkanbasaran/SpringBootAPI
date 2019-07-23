package de.unserewebseite.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyErrorController implements ErrorController{

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return "/error";
	}
	
	@RequestMapping(path = "/error", method = RequestMethod.GET)
	@ResponseBody
	public String get(HttpServletRequest request) {
		
		return "<!DOCTYPE html>\r\n" + 
				"<html lang=\"de\">\r\n" + 
				"<head>\r\n" + 
				"<link rel=\"stylesheet\" href=\"CSS/style.css\">\r\n" + 
				"<meta charset=\"utf-8\">\r\n" + 
				"<title>Error</title>\r\n" + 
				"</head>\r\n" + 
				"<body align=\"center\">\r\n" + 
				"	<h1>Uups da ist was schief gelaufen :(</h1>\r\n" + 
				"	<h2>Die Seite existiert nicht</h2>\r\n" +
				"<img src=\"Pics/Einstein.jpg\" alt=\"Einstein\">" +
				"</body>\r\n" + 
				"</html>";
	}

}
