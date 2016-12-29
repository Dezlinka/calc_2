package ru.itis2016;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Created by Ilya Evlampiev on 27.10.2015.
 */
@WebServlet({"/encode","/coden"})
public class TestEncodinServlet extends HttpServlet {
    static Logger log = Logger.getLogger(TestEncodinServlet.class);


    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String text=req.getParameter("text");
        System.out.println(text);
        //resp.setStatus(500);
        //resp.sendRedirect("/encode234");
        HttpSession session=req.getSession();
        String hist=(String)session.getAttribute("history");
        resp.getWriter().write("Введено было значение "+text+"<BR>"
                +"<form action=\"encode\" method=\"post\"><input type=\"text\" name=\"text\" ></input></form><br>Прошлое введеное значение как оно сохранено в сессии "+hist);
        session.setAttribute("history", text);
        //resp.addHeader("Own","Own header");
        //int a=2/0;
        // /getServletContext().getRequestDispatcher("/users.jsp").forward(req, resp);
        log.info("Text that was added to session: "+text);

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("ISO-8859-1");
        //req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String text=req.getParameter("text");
        System.out.println(text);
        String newText= new String(text.getBytes ("ISO-8859-1"), "UTF-8");//=URLDecoder.decode(text, "utf8");
        System.out.println(newText);
        Cookie[] cc=req.getCookies();
        String postHistory=null;
        for (Cookie ccc: cc)
        {
            if (ccc.getName().equals("histcookie")) {postHistory=ccc.getValue();}
        }
        resp.getWriter().write("Введено было значение " + text + "<BR>"
                + "<form action=\"encode\" method=\"post\"><input type=\"text\" name=\"text\" ></input></form> <br>Прошлое введеное значение как оно сохранено в куки "+postHistory);
        resp.addCookie(new Cookie("histcookie",text));
        log.debug("Cookies added "+text);
        // /getServletContext().getRequestDispatcher("/users.jsp").forward(req, resp);    }
        resp.sendRedirect("index.html");
    }
}

