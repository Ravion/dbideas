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
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import dbideas.IDManager;
import dbideas.JSONAction;
import dbideas.dbtree.DatabaseNode;


public class GetConnectionStatus implements JSONAction {
	String id;
	public void setId(String id) {
		this.id = id;
	}

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		String catalog="";
		boolean isReadOnly=false;
		boolean isClosed=false;
		boolean autoCommit=false;
		Date timeOpened=null;
		String transIsol="";
		
		DatabaseNode dn=(DatabaseNode)IDManager.get().get(id);
		JSONObject results=new JSONObject();
		JSONArray meta=new JSONArray();
		JSONArray data=new JSONArray();
		
		String []strs={"Property","Value"};
		
		for(int i=0;i<strs.length;i++){
			meta.put(strs[i]);	
		}
		
		Connection conn=dn.getConn().getConnection();
		
		JSONArray record=new JSONArray();
		record.put("Is Closed");
		try	{
			isClosed = conn.isClosed();
		}
		catch (Throwable th){}
		record.put(isClosed);
		data.put(record);
		
		record=new JSONArray();
		record.put("Is Read Only");
		try	{
			isReadOnly = conn.isReadOnly();
		}
		catch (Throwable th){}
		record.put(isReadOnly);
		data.put(record);
		
		record=new JSONArray();
		record.put("Catalog");
		try	{
			catalog = conn.getCatalog();
		}
		catch (Throwable th){}
		record.put(catalog);
		data.put(record);
		
		record=new JSONArray();
		record.put("Auto Commit");
		try	{
			autoCommit = conn.getAutoCommit();
		}
		catch (Throwable th){}
		record.put(autoCommit);
		data.put(record);
		
		record=new JSONArray();
		record.put("Time Opened");
		try	{
			timeOpened = dn.getConn().getTimeOpened();
		}
		catch (Throwable th){}
		record.put(timeOpened);
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Transaction Isolation");
		
		try{
			final int isol = conn.getTransactionIsolation();
			switch(isol)
			{
				case Connection.TRANSACTION_NONE:
					transIsol = "TRANSACTION_NONE";
					break;
				case Connection.TRANSACTION_READ_COMMITTED:
					transIsol = "TRANSACTION_READ_COMMITTED";
					break;
				case Connection.TRANSACTION_READ_UNCOMMITTED:
					transIsol = "TRANSACTION_READ_UNCOMMITTED";
					break;
				case Connection.TRANSACTION_REPEATABLE_READ:
					transIsol = "TRANSACTION_REPEATABLE_READ";
					break;
				case Connection.TRANSACTION_SERIALIZABLE:
					transIsol = "TRANSACTION_SERIALIZABLE";
					break;
				default:
					transIsol = "Unknown: " + isol;
					break;
			}
		}catch(Throwable t){}
		record.put(transIsol);
		data.put(record);
		
		
		results.put("meta",meta);
		results.put("data",data);
		return results;
		
	}

}
