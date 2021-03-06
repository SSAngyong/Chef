package com.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.model.Model;

public class DispatcherServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map clsMap=new HashMap();
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		try{
			String path=config.getInitParameter("contextConfigLocation");
			System.out.println(path+"@@");
			
			DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
			DocumentBuilder db=dbf.newDocumentBuilder();
			//파서기
			Document doc=db.parse(new File(path));
			Element root=doc.getDocumentElement();
			NodeList list=root.getElementsByTagName("bean");
			for(int i=0 ; i<list.getLength() ; i++){
				Element elem=(Element)list.item(i);
				String id=elem.getAttribute("id");
				String cls=elem.getAttribute("class");
				Class clsName=Class.forName(cls); // = 클래스의 정보를 읽어온다
												  //  => 메소드,멤버변수,생성자 제어가 가능
				//메모리 할당
				Object obj=clsName.newInstance();
				System.out.println(obj);
				
				clsMap.put(id, obj);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			//list.do
			//content.do ==> DispatcherServlet => service수행 => <url-pattern>*.do</>
			
			//사용자 요청 URL
			String cmd=request.getRequestURI();
			cmd=cmd.substring(request.getContextPath().length()+1, cmd.lastIndexOf('.'));
			
			Model model=(Model)clsMap.get(cmd);
			String jsp=model.handlerRequest(request, response);
			String temp=jsp.substring(jsp.lastIndexOf('.')+1);
			System.out.println(temp);
			if(temp.equals("do")){
				response.sendRedirect(jsp);
			}else{
				RequestDispatcher rd=request.getRequestDispatcher(jsp);
				rd.forward(request, response);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
	}

}