/*
 * Copyright (c) 2009 mazzolini at gmail.com
 * This file is part of dbIdeas.
 * 
 * dbIdeas is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * dbIdeas is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with dbIdeas.  If not, see <http://www.gnu.org/licenses/>.
 * 
*/
package dbideas.actions;

import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbideas.IPageAction;
import dbideas.dao.SourcesDAO;
import dbideas.entities.Source;

public class SourcesPage implements IPageAction {

	String pageid;
	public void setPageid(String pageid) {
		this.pageid = pageid;
	}
	 
	public void execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		List<Source> ls = SourcesDAO.getSources(em);
		Random rn=new Random();
		request.setAttribute("rn", Math.abs(rn.nextInt()));
		request.setAttribute("pageid", pageid);
		request.setAttribute("ls", ls);
		request.getRequestDispatcher("sources.jsp").forward(request, response);

	}

}
