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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Show {
	private SQLConnection conn;
	public Show(SQLConnection conn) {
		this.conn=conn;
	}
	public abstract String getShowString();
	public final JSONObject execute() throws Exception{
		
		JSONObject results=new JSONObject();
		JSONArray meta=new JSONArray();
		JSONArray data=new JSONArray();

		String sql=getShowString();
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			ResultSetMetaData metars=rs.getMetaData();
			int icount=metars.getColumnCount();
			for(int i=0;i<icount;i++){
				meta.put(metars.getColumnLabel(i+1));
			}
			
			while(rs.next()){
				JSONArray record=new JSONArray();
				for(int i=0;i<icount;i++){
					record.put(rs.getString(i+1));
				}
				data.put(record);
			}
		} catch (SQLException e) {
			//e.printStackTrace();
		}finally{
			try {if(rs!=null)
				rs.close();
			} catch (SQLException e) {
			}
			try {
				if(ps!=null)
					ps.close();
			} catch (SQLException e) {
			}
		}
		
		results.put("meta",meta);
		results.put("data",data);
		return results;
	}
}
