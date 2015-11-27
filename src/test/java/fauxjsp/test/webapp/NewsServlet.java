package fauxjsp.test.webapp;

import java.io.IOException;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fauxjsp.api.renderer.RenderSession;
import fauxjsp.test.webapp.dto.NavigationItem;
import fauxjsp.test.webapp.dto.News;
import fauxjsp.test.webapp.dto.Stock;


/**
 * Renders stocks, news and a fixed current date
 * @author George Georgovassilis
 */
public class NewsServlet extends HttpServlet{

	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		request.setAttribute("navigation", Arrays.asList(
				new NavigationItem("path 1", "label 1"), new NavigationItem(
						"path 2", "label 2")));
		request
				.setAttribute("listOfStocks", Arrays.asList(new Stock("S1",
						"Stock one", 10, 20),
						new Stock("S2", "Stock 2", -9, 88)));
		request.setAttribute("listOfNews", Arrays.asList(new News("1",
				"headline 1", "description 1", "full text of news 1", false),
				new News("2", "headline 2", "description 2",
						"full text of news 2", false)));

		GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		cal.set(GregorianCalendar.YEAR, 2000);
		cal.set(GregorianCalendar.MONTH, 2);
		cal.set(GregorianCalendar.DAY_OF_MONTH, 2);
		cal.set(GregorianCalendar.SECOND, 0);
		cal.set(GregorianCalendar.MINUTE, 0);
		cal.set(GregorianCalendar.MILLISECOND, 0);
		cal.set(GregorianCalendar.HOUR, 0);
		request.setAttribute("date", cal.getTime());
		request.setAttribute(RenderSession.ATTR_TIMEZONE, TimeZone.getTimeZone("UTC"));
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/news.jsp");
		dispatcher.forward(request, resp);
	}
}
