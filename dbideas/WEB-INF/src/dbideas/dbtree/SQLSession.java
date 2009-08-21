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

package dbideas.dbtree;


import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;
import dbideas.IDManager;

public class SQLSession {
	private String id;
	private SQLConnection conn;
	private String sessionName;
	DatabaseNode dn;
	private int sourceid;
	public SQLSession(int sourceid,String sessionName,SQLConnection conn){
		this.sourceid=sourceid;
		this.id=IDManager.get().nextID();
		IDManager.get().put(id,this);
		this.conn=conn;
		this.sessionName=sessionName;
		dn = new DatabaseNode(conn,sessionName);
	}
	public DatabaseNode getDatabaseNode(){
		return dn;
	}

	public int getSourceid() {
		return sourceid;
	}
	public SQLConnection getConn() {
		return conn;
	}

	public void setConn(SQLConnection conn) {
		this.conn = conn;
	}

	public String getId() {
		return id;
	}
	public String getSessionName() {
		return sessionName;
	}
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
}
