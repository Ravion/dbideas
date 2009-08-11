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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import dbideas.IDManager;
import dbideas.JSONAction;
import dbideas.dbtree.TableNode;
import dbideas.utils.ResultSetReader;


public class Preview  implements JSONAction {
	
	String id;
	
	public void setId(String id) {
		this.id = id;
	}

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		JSONObject results=new JSONObject();
		JSONArray meta=new JSONArray();
		JSONArray data=new JSONArray();
		
		Object obj=IDManager.get().get(id);
		if(obj!=null && obj instanceof TableNode){
			TableNode table=((TableNode)obj);
			Connection conn=table.getConn().getConnection();
			try{
				String sql="select * from "+table.getITableInfo().getQualifiedName();
				PreparedStatement ps=conn.prepareStatement(sql);
				ps.setMaxRows(20);
				ResultSet rs=ps.executeQuery();
				if(rs!=null){
					ResultSetMetaData metadata=rs.getMetaData();
					int columncount=metadata.getColumnCount();
					
					for(int i=1;i<=columncount;i++){
						
						String label=metadata.getColumnLabel(i);
						meta.put(label);
					}
					ResultSetReader reader=new ResultSetReader(rs);
					//dataset.setResultSet(rs);
					Object[]row;
					while((row=reader.readRow())!=null){
						JSONArray record=new JSONArray();
						for(int i=0;i<columncount;i++){
							Object obj1=row[i];
							record.put(obj1);
						}
						data.put(record);
					}
					rs.close();
				}
				ps.close();
			}catch(Exception e){
				
			}
		}
		results.put("meta",meta);
		results.put("data",data);
		return results;
	}

	

}
