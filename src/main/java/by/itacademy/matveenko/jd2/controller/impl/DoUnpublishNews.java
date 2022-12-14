package by.itacademy.matveenko.jd2.controller.impl;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.itacademy.matveenko.jd2.controller.Command;
import by.itacademy.matveenko.jd2.service.INewsService;
import by.itacademy.matveenko.jd2.service.ServiceException;
import by.itacademy.matveenko.jd2.service.ServiceProvider;
import by.itacademy.matveenko.jd2.util.AttributsName;
import by.itacademy.matveenko.jd2.util.ConnectorStatus;
import by.itacademy.matveenko.jd2.util.JspPageName;
import by.itacademy.matveenko.jd2.util.NewsParameterName;
import by.itacademy.matveenko.jd2.util.PageUrl;
import by.itacademy.matveenko.jd2.util.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class DoUnpublishNews implements Command {
	private final INewsService newsService = ServiceProvider.getInstance().getNewsService();
	private static final Logger log = LogManager.getLogger(DoUnpublishNews.class);

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] idNews = request.getParameterValues(NewsParameterName.JSP_ID_NEWS);		
		
	    try {
			HttpSession getSession = request.getSession(false);
			if (getSession == null) {
				response.sendRedirect(JspPageName.INDEX_PAGE);
				} else {
					String role = (String) getSession.getAttribute(AttributsName.ROLE);
					if (!role.equals(UserRole.ADMIN.getName())) {
						response.sendRedirect(JspPageName.ERROR_PAGE);
						} else {
							if (newsService.unpublishNewsById(idNews)) {
								getSession.setAttribute(AttributsName.USER_STATUS, ConnectorStatus.ACTIVE);
								getSession.setAttribute(AttributsName.UNPUBLISH_NEWS, AttributsName.COMMAND_EXECUTED);
								getSession.setAttribute(AttributsName.NEWS, AttributsName.UNPUBLISH_NEWS);								
								response.sendRedirect(PageUrl.NEWS_LIST_PAGE);
								} else {
									response.sendRedirect(JspPageName.ERROR_PAGE);
									}
							}
					}
			} catch (ServiceException e) {
				log.error(e);
				response.sendRedirect(JspPageName.INDEX_PAGE);
				}
	    }
}