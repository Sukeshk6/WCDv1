/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.flipkart.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.Application;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.flipkart.DAO.CustomerDAO;
import org.flipkart.DAO.CustomerDAOImpl;
import org.flipkart.models.Customer;

/**
 *
 * @author 11502-CL02
 */
@WebServlet(name = "AuthenticationController", urlPatterns = {"/Authenticate"})
public class AuthenticationController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        try{
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out=response.getWriter();
        HttpSession session=request.getSession();
        String referer=request.getHeader("referer");
        String mailId=request.getParameter("mailId");
        String password=request.getParameter("password");
        CustomerDAO cd=new CustomerDAOImpl();
        Customer c;
        RequestDispatcher rd=request.getRequestDispatcher("index.html");
        if(referer.contains("login")){
            c=new Customer(mailId, password);
            c=cd.verify(c);
             if(cd.isUserValid()){
                 //out.println("Login Successfull!!!");
                 Cookie cookie=new Cookie("phone",c.getPhoneNumber());
                 response.addCookie(cookie);
                 session.setAttribute("user",c);
                 
                 rd=request.getRequestDispatcher("Welcome.jsp");
             }
        }
        else{
            String customerName=request.getParameter("userName");
            String phoneNumber=request.getParameter("phoneNo");
            String cPassword=request.getParameter("cpassword");
            if(password.equals(cPassword)){
                c=new Customer(customerName, mailId, phoneNumber, password);
                if(cd.register(c)>0){
                    out.println("Registration Successfull!!!");
                } 
            }
            else{
               out.println("Password does not match");
               rd=request.getRequestDispatcher("signUp.html");
           }
        }
        rd.include(request, response);
        }catch(Exception e){
            response.sendRedirect("index.jsp");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(AuthenticationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(AuthenticationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
