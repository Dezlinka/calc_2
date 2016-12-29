package ru.itis2016;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


@WebServlet({"/history"})
public class CalculatorHistoryServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        PrintWriter pw = response.getWriter();
        pw.write("<table>\n" +
                "<tbody>" +
                "<thead>" +
                "<th>ID</th>" +
                "<th>MathAction</th>" +
                "</thead>");
        CalculatorHistoryManager chm = CalculatorHistoryManager.getManager();
        try {
            chm.addRecord(CalculatorHistoryManager.getSavedMathaction());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Statement st = chm.getConnection().createStatement();
            ResultSet rs = st.executeQuery("select * from history");
            while(rs.next()) {
                pw.write("<tr>" +
                        "<td>" + rs.getInt("id") + "</td>" +
                        "<td>" + rs.getString("mathaction") + "</td>" +
                        "</tr>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pw.write("</tbody>" +
                "</table>");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
