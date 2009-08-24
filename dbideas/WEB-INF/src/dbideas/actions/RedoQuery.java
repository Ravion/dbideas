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

import dbideas.IDManager;
import dbideas.JSONAction;
import dbideas.utils.SQLExecutor;

public class RedoQuery implements JSONAction {
	String queryID=null;
	public void setQueryID(String queryID) {
		this.queryID = queryID;
	}
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		SQLExecutor executor=(SQLExecutor)IDManager.get().get(queryID);
		JSONObject resultSet=new JSONObject();
		JSONArray meta=new JSONArray(); 
		JSONArray data=new JSONArray();
		JSONArray info2=new JSONArray();
		
		
		try{
			executor.redoQuery( data, meta,info2);
			info2.put(true);
		}catch(Exception e){
			info2.put(false);
			info2.put(e.getMessage());
		}
		resultSet.put("meta", meta);
		resultSet.put("data", data);
		resultSet.put("info", info2);
		return resultSet;
	}

}
