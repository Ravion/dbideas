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

import java.sql.Connection;
import java.sql.DriverManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.squirrel_sql.fw.sql.ISQLDriver;
import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import dbideas.IDManager;
import dbideas.JSONAction;
import dbideas.WebSQLSession;
import dbideas.dao.DriversDAO;
import dbideas.dao.SourcesDAO;
import dbideas.dbtree.SQLSession;
import dbideas.entities.Driver;
import dbideas.entities.Source;
import dbideas.plugin.PluginManager;


public class Connect implements JSONAction {
	String user,password;
	int driverid,sourceid;
	String autocommit;
	public void setAutocommit(String autocommit) {
		this.autocommit = autocommit;
	}

	public void setUser(String user) {
		this.user = user;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setDriverid(int driverid) {
		this.driverid = driverid;
	}
	public void setSourceid(int sourceid) {
		this.sourceid = sourceid;
	}
	
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		Driver driver=DriversDAO.getDriver(em,driverid);
		Source source=SourcesDAO.getSource(em,sourceid);
		
		ISQLDriver idriver=new net.sourceforge.squirrel_sql.fw.sql.SQLDriver();
		
		
		JSONObject obj=new JSONObject();
		
			Class.forName(driver.getDriverClassName());
			idriver.setDriverClassName(driver.getDriverClassName());
			Connection _conn = DriverManager.getConnection(source.getJdbcUrl(),user,password);
			if(autocommit!=null){
				_conn.setAutoCommit(true);
			}else{
				_conn.setAutoCommit(false);
			}
			SQLConnection conn=new SQLConnection(_conn,null,idriver);
			//sessions.getSqlsessions().add(new SQLSession(source.getSourceName(),conn));
			WebSQLSession sessions=(WebSQLSession)request.getSession(true).getAttribute("sessions");
			sessions.getSqlsessions().add(new SQLSession(sourceid,source.getSourceName()+" ("+IDManager.get().nextSessionID()+")",conn));
			obj.put("success",true);
			JSONArray arr=new JSONArray();
			PluginManager.getInstance().dynamicPluginScripts(arr, conn);
			obj.put("pluginScripts", arr);
		
		return obj;
	}

}
