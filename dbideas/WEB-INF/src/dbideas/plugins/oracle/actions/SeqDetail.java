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
import dbideas.plugin.BasePluginType;
import dbideas.plugins.oracle.SequenceNode;


public class SeqDetail {
	
	private String id;

	public SeqDetail(String id) {
		this.id=id;
	}

	public JSONObject execute()throws Exception {
		JSONObject results=new JSONObject();
		JSONArray meta=new JSONArray();
		JSONArray data=new JSONArray();
		String[]detailKeys={"Min Value","Max Value","Increment By","Cycle Flag", "Order","Cache Size","Last Number"};
		String []strs={"Property","Value"};
		for(int i=0;i<strs.length;i++){
			meta.put(strs[i]);	
		}
		Object obj=IDManager.get().get(id);
		SequenceNode seq=(SequenceNode)obj;
		final String sql="select min_value,max_value,increment_by,cycle_flag,order_flag, cache_size, last_number from sys.all_sequences "+
		"where sequence_owner=? and sequence_name=?";
		
		ResultSet rs=null;
		PreparedStatement ps=null;
  		try{
			ps=seq.getConn().prepareStatement(sql);
			BasePluginType tot=(BasePluginType)seq.getParent();
			String owner=tot.getParent().getName();
			ps.setString(1,owner);
			ps.setString(2,seq.getName());
			rs=ps.executeQuery();
			
			if(rs.next()){
				for(int i=0;i<detailKeys.length;i++){
					JSONArray record=new JSONArray();
					record.put(detailKeys[i]);
					record.put(rs.getString(i+1));
					data.put(record);
				}
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
