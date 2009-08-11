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

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import dbideas.JSONAction;
import dbideas.dao.SourcesDAO;
import dbideas.entities.Source;


public class GetSources implements JSONAction {

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et) throws Exception {
		
		JSONArray arr=new JSONArray();
		
		for (Source source : SourcesDAO.getSources(em)) {
			JSONObject obj=new JSONObject();
			obj.put("id", source.getId());
			obj.put("sourceName",source.getSourceName());
			obj.put("creationDate",source.getCreationDate());
			obj.put("driverid",source.getDriverid());
			obj.put("jdbcUrl",source.getJdbcUrl());
			obj.put("userName",source.getUserName());
			arr.put(obj);
		}
			
		JSONObject ret = new JSONObject();
		ret.put("sources", arr);
		return ret;

	}

}
