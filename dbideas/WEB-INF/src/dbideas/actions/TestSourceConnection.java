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

import org.json.JSONObject;

import dbideas.JSONAction;
import dbideas.dao.SourcesDAO;
import dbideas.entities.Source;

public class TestSourceConnection implements JSONAction {

	int id;
	public void setId(int id) {
		this.id = id;
	}
	 
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		Source s=SourcesDAO.getSource(em, id);
		
//		String driverClass=s.getDriverName();
//		String jdbcUrl=s.getJdbcUrl();
//		String username=s.getUserName();
//		String pwd=s.getPassword();
//		//Driver drv=null;
//		Connection conn=null;
//		try{
//			@SuppressWarnings("unused")
//			Driver drv=(Driver)Class.forName(driverClass).newInstance();
//			conn=DriverManager.getConnection(jdbcUrl, username, pwd);
//		}
//		finally{
//			if(conn!=null){
//				try{conn.close();}catch(Exception e){}
//			}
//		}
		
		return new JSONObject();
	}

}
