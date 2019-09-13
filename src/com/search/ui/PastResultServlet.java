package com.search.ui;

import com.search.service.SearchService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/PastResultServlet")
public class PastResultServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        SearchService searchService = new SearchService(out);
        String str = searchService.readFileWithAllResults();
        request.setAttribute("stringWithAllResults",str);

//        String numberOfResult = request.getParameter("numberOfResult");
//        if(searchService.checkBadFormatInput(numberOfResult,out) == false){
//            return;
//        }


        String[] arrSplit = str.split(",");
        response.setContentType("text/html");

        for (String s:arrSplit) {
            out.println("<center>" + s + "</center>");
        }
    }

    public void destroy() {
        // do nothing
    }
}
