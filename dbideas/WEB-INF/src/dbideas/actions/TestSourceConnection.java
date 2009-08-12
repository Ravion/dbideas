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

import org.json.JSONObject;

import dbideas.JSONAction;
import dbideas.dao.DriversDAO;
import dbideas.dao.SourcesDAO;
import dbideas.entities.Driver;
import dbideas.entities.Source;

public class TestSourceConnection implements JSONAction {

	String password;
	public void setPassword(String password) {
		this.password = password;
	}

	int id;
	public void setId(int id) {
		this.id = id;
	}
	 
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		Source s=SourcesDAO.getSource(em, id);
		
		int driverid=s.getDriverid();
		Driver driver = DriversDAO.getDriver(em, driverid);
		String jdbcUrl=s.getJdbcUrl();
		String username=s.getUserName();
		//Driver drv=null;
		Connection conn=null;
		try{
			Class.forName(driver.getDriverClassName());
			conn=DriverManager.getConnection(jdbcUrl, username, password);
		}
		finally{
			if(conn!=null){
				try{conn.close();}catch(Exception e){}
			}
		}
		
		return new JSONObject();
	}

}
