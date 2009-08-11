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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import dbideas.IDManager;
import dbideas.JSONAction;
import dbideas.dbtree.ColumnModel;
import dbideas.dbtree.IStructureNode;
import dbideas.dbtree.TableNode;
import dbideas.dbtree.TablesNode;




public class GetMeta implements JSONAction {
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
			String []strs={"Column Name","Data Type","Type Name","Size","Decimal Digits","Default Value","Accept Null Value","Remarks"};
			
			for(int i=0;i<strs.length;i++){
				meta.put(strs[i]);	
			}
			TableNode table=((TableNode)obj);
			try {
				List<ColumnModel>listCols=table.getColumns();
				for (ColumnModel columnModel : listCols) {
					JSONArray record=new JSONArray();
					for(int j=0;j<8;j++){
						record.put(columnModel.getValue(j));
					}
					
					data.put(record);
				}
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}else if(obj!=null && obj instanceof TablesNode){
			meta.put("Name");
			meta.put("Remarks");
			TablesNode tables=((TablesNode)obj);
			List<IStructureNode> ls=tables.getChildren();
			for (IStructureNode tableNode : ls) {
				JSONArray record=new JSONArray();
				record.put(tableNode.getName());
				String remarks = ((TableNode)tableNode).getRemarks();
				if(remarks==null)
					remarks="";
				record.put(remarks);
				data.put(record);
			}
		}
		results.put("meta",meta);
		results.put("data",data);
		return results;
	}

}
