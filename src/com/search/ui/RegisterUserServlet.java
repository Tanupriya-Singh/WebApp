package com.search.ui;

import com.search.service.JDBCService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

 @WebServlet("/RegisterUserServlet")
public class RegisterUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        String username = request.getParameter("userid");
        String password = request.getParameter("password");
        HttpSession session = request.getSession(false);

        JDBCService jdbcService = new JDBCService(session, out);

        //TODO:Perform search word case sensitive

        boolean isUserExist = jdbcService.checkIfUserExists(username);
        if (isUserExist) {
//            JOptionPane.showMessageDialog(null, "My Goodness, this is so concise");

            out.println("User Already Exists!");
            return;
        }
        boolean flag = jdbcService.insertIntoDB(username, password);
        if (flag)
            out.println("Insertion into database is successful");
    }
}
