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

import java.sql.ResultSet;
import java.sql.Statement;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import dbideas.IDManager;
import dbideas.JSONAction;
import dbideas.dbtree.TableNode;




public class GetPK  implements JSONAction {
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
		
		
		
		//String id=request.getParameter("id");
		Object obj=IDManager.get().get(id);
		if(obj!=null && obj instanceof TableNode){
			String []strs={"Primary Key","Column Name","Order"};
			
			for(int i=0;i<strs.length;i++){
				meta.put(strs[i]);	
			}
			TableNode table=((TableNode)obj);
			ResultSet rs=null;
			try{
				rs=table.getPK();
				while(rs.next()){
					String colName=rs.getString("COLUMN_NAME"); 
					short order=rs.getShort("KEY_SEQ");
					String pkName=rs.getString("PK_NAME");  
					if(pkName!=null){
						JSONArray record=new JSONArray();
						record.put(pkName);
						record.put(colName);
						record.put(order);
						data.put(record);
					}
				}
				
			}
			finally{
				try {
					if(rs!=null){
						Statement st=rs.getStatement();
						rs.close();
						if(st!=null)
							st.close();
					}
				} catch (Exception e) {
					
				}
			}
		}
		results.put("meta",meta);
		results.put("data",data);
		return results;
	}
	

}
