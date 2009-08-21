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

import java.sql.SQLException;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import dbideas.JSONAction;
import dbideas.WebSQLSession;
import dbideas.dao.DriversDAO;
import dbideas.dao.SourcesDAO;
import dbideas.dbtree.SQLSession;
import dbideas.entities.Driver;


public class GetDatabases implements JSONAction {

	
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		JSONArray arr=new JSONArray();
		WebSQLSession sessions=(WebSQLSession)request.getSession(true).getAttribute("sessions");
		for (SQLSession sqlsession : sessions.getSqlsessions()) {
			
			Driver driver=DriversDAO.getDriver(em, SourcesDAO.getSource(em, sqlsession.getSourceid()).getDriverid());
			String iconurl=driver.getIconurl();
			JSONObject obj=new JSONObject();
			obj.put("id",sqlsession.getId());
			obj.put("iconurl",iconurl);
			obj.put("name",sqlsession.getSessionName());
			try {
				obj.put("autocommit",sqlsession.getConn().getAutoCommit());
			} catch (SQLException e) {
				obj.put("autocommit",false);
			}
			boolean hasCatalogs=sqlsession.getDatabaseNode().supportsCatalogs();
			if(hasCatalogs){
				String catalogs[]=sqlsession.getDatabaseNode().getCatalogs();
				obj.put("catalogs", Arrays.asList(catalogs));
				obj.put("catalog",sqlsession.getConn().getCatalog());
			}
			obj.put("hasCatalogs", hasCatalogs);
			arr.put(obj);
		}
		
		JSONObject ret = new JSONObject();
		ret.put("databases", arr);
		return ret;
	}

}
