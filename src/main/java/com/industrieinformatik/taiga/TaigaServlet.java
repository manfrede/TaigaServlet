package com.industrieinformatik.taiga;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class TaigaServlet extends HttpServlet{

  String taigaBaseUrl;
  String authToken;
  
  public TaigaServlet() {
    taigaBaseUrl = getInitParameter("taigaBaseUrl");
    authToken = getInitParameter("authToken");
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }
}

