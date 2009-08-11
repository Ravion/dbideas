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

import java.sql.DatabaseMetaData;
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




public class GetIndexes  implements JSONAction {
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
			String []strs={"Index Name","Column Name","Unique","Type","Ordinal Position","Asc/Desc","Cardinality","Pages"};
			
			for(int i=0;i<strs.length;i++){
				meta.put(strs[i]);	
			}
			TableNode table=((TableNode)obj);
			ResultSet rs=null;
			try{
				rs=table.getIndexes();
				while(rs.next()){
					boolean nonUnique=rs.getBoolean("NON_UNIQUE");
					String str=rs.getString("INDEX_NAME"); 
					short type=rs.getShort("TYPE"); 
					short order=rs.getShort("ORDINAL_POSITION");  
					String name=rs.getString("COLUMN_NAME");
					String asc_or_des=rs.getString("ASC_OR_DESC");  
					String cardinality=rs.getString("CARDINALITY");
					String pages=rs.getString("PAGES");
					if(str!=null){
						JSONArray record=new JSONArray();
						record.put(str);
						record.put(name);
						record.put(!nonUnique);
						
						if(type==DatabaseMetaData.tableIndexClustered)
							record.put("Clustered");
						else if(type==DatabaseMetaData.tableIndexHashed)
							record.put("Hashed"); 
						else if(type==DatabaseMetaData.tableIndexStatistic)
							record.put("Statistics"); 
						else record.put("Other");
						
						record.put(order);
						record.put(asc_or_des);
						record.put(cardinality);
						record.put(pages);
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
