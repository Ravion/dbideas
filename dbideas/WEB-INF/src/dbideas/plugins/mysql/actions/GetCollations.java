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
import java.sql.SQLException;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetCollations {

	private SQLConnection conn;

	public GetCollations(SQLConnection conn) {
		this.conn=conn;
	}


	public JSONObject execute() throws JSONException {
		JSONArray arr=new JSONArray();
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			ps=conn.prepareStatement("select collation_name,character_set_name from information_schema.collations order by character_set_name asc, collation_name asc");
			rs=ps.executeQuery();
			
			
			JSONObject record=new JSONObject(); 
			
			record.put("collation_name","");
			record.put("character_set_name","");
			arr.put(record);
			
			while(rs.next()){
				record=new JSONObject(); 
				
				record.put("collation_name",rs.getString(1));
				record.put("character_set_name",rs.getString(2));
				arr.put(record);
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
		
		JSONObject ret = new JSONObject();
		ret.put("collations", arr);
		return ret;
	}

}
