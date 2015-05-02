package singleNode;

import java.io.IOException;

import indexDB.*;

import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class SingleNodeServlet extends HttpServlet{
	String DBdirec = "";
	IndexDBWrapper db;
	JSONParser parser;
	
	@Override
	public void init(ServletConfig config) {
		db = new IndexDBWrapper(DBdirec);
		db.setup();
		parser = new JSONParser();
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/html");
		String word = request.getParameter("word");
		WordOccurence wordOccurence = db.getWordIndex(word);
		JSONObject obj = new JSONObject();
		obj.put("word", word);
		
		if (wordOccurence != null) {
			obj.put("idf", wordOccurence.getIdf());
			JSONArray list = new JSONArray();
			for (UrlOccurence url: wordOccurence.getUrlOccurences()) {
				list.add(url);
			}
			obj.put("doclist", list);
		}
		
		PrintWriter out = response.getWriter();
		out.println(obj.toJSONString());
	}
	
	@Override
	public void destroy() {
		db.close();
	}
}
