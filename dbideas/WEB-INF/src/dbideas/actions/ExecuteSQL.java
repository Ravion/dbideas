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
import javax.servlet.http.HttpSession;

import net.sourceforge.squirrel_sql.fw.sql.QueryTokenizer;
import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import dbideas.IDManager;
import dbideas.JSONAction;
import dbideas.WebSQLSession;
import dbideas.dbtree.SQLSession;

public class ExecuteSQL implements JSONAction {
	String sql, sessionid;
	public void setSql(String sql) {
		this.sql = sql;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		SQLConnection conn=null;
		if(sessionid!=null){
			SQLSession sqlsession=(SQLSession)IDManager.get().get(sessionid);
			if(sqlsession==null){
				
				sqlsession = null;//GetTree.createSQLSession(request, em,sessionid);
			}
			if(sqlsession!=null)
				conn=sqlsession.getConn();
		}

		
		
		
		JSONArray info=new JSONArray();
		
		long init=0;
		
		init=System.nanoTime();
		
		JSONArray resultSets=new JSONArray();
		
		QueryTokenizer qt=new QueryTokenizer(";","--",false);
		qt.setScriptToTokenize(sql);
		String nextQuery=null;
		int limit=10;
		HttpSession session = request.getSession(true);
		WebSQLSession sessions=(WebSQLSession)session.getAttribute("sessions");
		
		while(qt.hasQuery()){
			SQLExecutor he = new SQLExecutor(conn,limit);
			sessions.getExecutors().add(he);
			JSONObject resultSet=new JSONObject();
			JSONArray meta=new JSONArray(); 
			JSONArray data=new JSONArray();
			JSONArray info2=new JSONArray();
			nextQuery=qt.nextQuery();
			
			try{
				he.executeQuery(nextQuery, data, meta,info2);
				info2.put(true);
			}catch(Exception e){
				info2.put(false);
				info2.put(e.getMessage());
			}
			resultSet.put("meta", meta);
			resultSet.put("data", data);
			resultSet.put("info", info2);
			resultSets.put(resultSet);
		}

		long end=System.nanoTime();
		
			
		info.put((end-init)/1000000);		
		
		JSONObject ret = new JSONObject();
	
		
		ret.put("info", info);
		ret.put("resultSets",resultSets);
		return ret;
	}

}
