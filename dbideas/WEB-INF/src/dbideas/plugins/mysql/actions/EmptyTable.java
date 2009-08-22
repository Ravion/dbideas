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
package dbideas.plugins.mysql.actions;

import java.sql.PreparedStatement;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.json.JSONObject;


public class EmptyTable  {

	private final SQLConnection conn;
	private final String tableName;

	public EmptyTable(SQLConnection conn, String tableName) {
		this.conn = conn;
		this.tableName = tableName;
		
	}
	public JSONObject execute()throws Exception {
		String sql="TRUNCATE TABLE "+tableName;
		PreparedStatement ps=null;
		try{
			ps=conn.prepareStatement(sql);
			ps.execute();
			
		}finally{
			if(ps!=null){
				try {
					ps.close();
				} catch (Exception e) {
				}
			}
		}
		return new JSONObject();
	}


	

}
