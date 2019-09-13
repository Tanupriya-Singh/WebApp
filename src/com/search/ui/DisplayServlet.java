package com.search.ui;

import com.search.service.JDBCService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/*

Get is used to get resources from the server. You cannot get anything until you post on the server.
Post is used to set resources on the server.
 */

@WebServlet("/ButtonAction")
public class DisplayServlet extends HttpServlet {
    private String searchWord;
    /*
    No init() required
     */

    /*
    If you remove this, error is thrown that get no supported
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        searchWord = request.getParameter("searchWord");
        request.setAttribute("searchWord", searchWord);
        //out.println("Display servlet is here "+searchWord);

        HttpSession session = request.getSession(false);

        JDBCService jdbcService = new JDBCService(session, out);
        /*
        Now, time to display the results
        */

        boolean badFormat = jdbcService.checkBadFormatInput(searchWord);
        if (badFormat == false)
            return;

        /*
        This is working fine
            new SearchService(out).checkIfSerialized(searchWord);
         */
        //Display past searches here
        jdbcService.getHistoryOfUser();
        jdbcService.checkIfSerialized(searchWord);
    }

    public void destroy() {
        // do nothing
    }
}
