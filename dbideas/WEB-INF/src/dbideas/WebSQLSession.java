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

package dbideas;


import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import dbideas.dbtree.SQLSession;
import dbideas.utils.SQLExecutor;


@SuppressWarnings("serial")
public class WebSQLSession implements Serializable{
	
	
	private transient ArrayList<SQLSession> sqlsessions;
	private transient ArrayList<SQLExecutor> executors;
	private transient IDManager idmanager;
	private String username;
	public WebSQLSession(){
		this.idmanager=new IDManager();
		this.sqlsessions=new ArrayList<SQLSession>();
		executors=new ArrayList<SQLExecutor>();
	}
	public void closeSessions() {
		for (SQLExecutor exec : getExecutors()) {
			try {
				exec.close();
			} catch (Exception e) {
			}
		}
		for (SQLSession conn : getSqlsessions()) {
			try {
				conn.getConn().close();
			} catch (SQLException e) {
			}
		}
		
	}
	
	public ArrayList<SQLExecutor> getExecutors() {
		if( executors==null){
			executors=new ArrayList<SQLExecutor>();	
		}
		return executors;
		
	}
	public void setExecutors(ArrayList<SQLExecutor> executors) {
		this.executors = executors;
	}
	public void closeSession(SQLSession sqlsession) {
		try {
			sqlsession.getConn().close();
		} catch (SQLException e) {
		}
		getSqlsessions().remove(sqlsession);
		
	}
	public void setSqlsessions(ArrayList<SQLSession> sqlsessions) {
		this.sqlsessions = sqlsessions;
	}
	public ArrayList<SQLSession> getSqlsessions() {
		if(sqlsessions==null){
			sqlsessions=new ArrayList<SQLSession>();
		}
		return sqlsessions;
	}
	public IDManager getIDManager() {
		if(idmanager==null){
			idmanager=new IDManager();
		}
		return idmanager;
	}
	public void setUser(String username) {
		this.username=username;
		
	}
	public String getUser() {
		return username;
	}
}
