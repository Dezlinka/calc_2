package ru.itis2016;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import static ru.itis2016.CalculatorHistoryRecord.cHR;

public class CalculatorServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        HttpSession session = request.getSession();
        //обнуление сессионных полей
        session.setAttribute("digit", null);
        session.setAttribute("oper", null);
        request.setAttribute("digit", null);
        request.setAttribute("mathaction", null);
        getServletContext().getRequestDispatcher("/calculator.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        String digit = request.getParameter("digit");
        String oper = request.getParameter("mathaction");
        HttpSession ses = request.getSession();
        String ssDigit = (String) ses.getAttribute("digit");
        String ssOper = (String) ses.getAttribute("oper");
        boolean numberError = false;
        if(digit.equals("")) {
            ses.setAttribute("digit", null);
            ses.setAttribute("oper", null);
            ssDigit = "";
            ssOper = "";
        } else {
            try {
                if(digit.contains(",")) {
                    digit = digit.replace(",",".");
                }
                Double check = Double.valueOf(digit);
            } catch (NumberFormatException nfexc) {
                numberError = true;
                ssDigit = "<span style=\"color: red\">Допустимы только числа!</span>";
                ses.setAttribute("digit", null);
                ses.setAttribute("oper", null);
            }
            if(ssDigit == null && ssOper == null && !numberError) {
                ses.setAttribute("digit", digit);
                ses.setAttribute("oper", oper);
                ssDigit = (String) ses.getAttribute("digit");
                ssOper = (String) ses.getAttribute("oper");
            } else if(!numberError) {
                String digitTemp = null;
                boolean error = false;
                if(ssOper.equals("+")) {
                    digitTemp = String.valueOf(Double.valueOf(ssDigit) + Double.valueOf(digit));
                } else if(ssOper.equals("-")) {
                    digitTemp = String.valueOf(Double.valueOf(ssDigit) - Double.valueOf(digit));
                } else if(ssOper.equals("*")) {
                    digitTemp = String.valueOf(Double.valueOf(ssDigit) * Double.valueOf(digit));
                } else if(ssOper.equals("/")) {
                    try {
                        digitTemp = String.valueOf(Double.valueOf(ssDigit) / Double.valueOf(digit));
                        if (Double.valueOf(digitTemp) == Double.POSITIVE_INFINITY ||
                                Double.valueOf(digitTemp) == Double.NEGATIVE_INFINITY)
                            throw new ArithmeticException();
                    } catch (ArithmeticException aexc) {
                        digitTemp = "<span style=\"color:red\">Недопустимое арифметическое действие!</span>";
                        error = true;
                        ses.setAttribute("digit", null);
                        ses.setAttribute("oper", null);
                    }
                } else if(ssOper.equals("=")) {
                    digitTemp = ssDigit;
                }
                ses.setAttribute("digit", digitTemp);
                ses.setAttribute("oper", oper);
                ssDigit = (String) ses.getAttribute("digit");
                ssOper = (String) ses.getAttribute("oper");
                if(ssOper.equals("=") || error) {
                    ses.setAttribute("digit", null);
                    ses.setAttribute("oper", null);
                }
            }
        }

        CalculatorHistoryManager.setSavedMathaction(ssOper);
        request.setAttribute("digit", ssDigit);
        request.setAttribute("mathaction", ssOper);
        getServletContext().getRequestDispatcher("/calculator.jsp").forward(request, response);
    }

}
