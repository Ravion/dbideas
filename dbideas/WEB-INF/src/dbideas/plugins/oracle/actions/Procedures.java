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

package dbideas.plugins.oracle.actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

import dbideas.IDManager;
import dbideas.plugins.oracle.ProcedureTypeNode;



public class Procedures {

	private String id;

	public Procedures(String id) {
		this.id=id;
	}

	public JSONObject execute()throws Exception {
		JSONObject results=new JSONObject();
		JSONArray meta=new JSONArray();
		JSONArray data=new JSONArray();
		Object obj=IDManager.get().get(id);
		ProcedureTypeNode procedureTypeNode=(ProcedureTypeNode)obj;
		ResultSet rs=null;
		PreparedStatement ps=null;

		
		String []strs={"Name","Status","Created","Last DDL Time","Timestamp"};
		
		for(int i=0;i<strs.length;i++){
			meta.put(strs[i]);	
		}
		
		try{
			final String sql="SELECT  object_name,status, created,last_ddl_time,timestamp "+  
				" FROM sys.all_objects where owner=? "+  
				" and object_type='PROCEDURE'";
			ps=procedureTypeNode.getConn().prepareStatement(sql);
			String owner=procedureTypeNode.getParent().getName();
			
			ps.setString(1,owner);
			rs=ps.executeQuery();
			while(rs.next()){
				JSONArray record=new JSONArray();
				record.put(rs.getString(1));
				record.put(rs.getString(2));
				record.put(rs.getString(3));
				record.put(rs.getString(4));
				record.put(rs.getString(5));
				
				data.put(record);
			}
				
		}catch(Exception e){
			
		}finally{
			try{if(rs!=null)
				rs.close();
			}catch(Exception e){
			}	
			try{if(ps!=null)
				ps.close();
			}catch(Exception e){
			}	
		}	
		results.put("meta",meta);
		results.put("data",data);
		return results;
	}

}
