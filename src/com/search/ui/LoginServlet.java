package com.search.ui;

import com.search.service.JDBCService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/LoginAction")
public class LoginServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher view;
        String userid = request.getParameter("username");
        String password = request.getParameter("password");

        HttpSession session = request.getSession(); /* Creating a new session*/
        session.setAttribute("username", userid);
        session.setAttribute("password", password);
        /*
        Check for the username and password
         */

        PrintWriter out = response.getWriter();

        JDBCService jdbcService = new JDBCService(session, out);
        boolean isUserPresent = jdbcService.checkAuthorizedUsers(userid, password,out);
        if (isUserPresent == false) {
            out.println("Incorrect details!");
        }
        else {
            view = request.getRequestDispatcher("menu.jsp");
            view.forward(request, response);
        }
    }

    public void destroy() {
        // do nothing.
    }
}
