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

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import dbideas.JSONAction;
import dbideas.plugin.PluginManager;





public class GetDetails  implements JSONAction {
	String nodeType,sessionid;
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		JSONArray array=new JSONArray();
		if("tb".equals(nodeType)){
			JSONArray record=new JSONArray();
			record.put("Columns Detail");
			record.put("do?action=getMeta");
			array.put(record);
			record=new JSONArray();
			record.put("Preview");
			record.put("do?action=preview");
			array.put(record);
			
			record=new JSONArray();
			record.put("Indexes");
			record.put("do?action=getIndexes");
			array.put(record);
			record=new JSONArray();
			record.put("Primary Key");
			record.put("do?action=getPK");
			array.put(record);
			record=new JSONArray();
			record.put("Foreign Keys");
			record.put("do?action=getFK");
			array.put(record);
			record=new JSONArray();
			record.put("Grants");
			record.put("do?action=getGrants");
			array.put(record);
			record=new JSONArray();
			record.put("Exported Keys");
			record.put("do?action=getExportedKeys");
			array.put(record);
			
		}else if ("view".equals(nodeType)){
			JSONArray record=new JSONArray();
			record.put("Columns Detail");
			record.put("do?action=getMeta");
			array.put(record);
			record=new JSONArray();
			record.put("Preview");
			record.put("do?action=preview");
			array.put(record);
		}
		else if("tbs".equals(nodeType)||"views".equals(nodeType)){
			JSONArray record=new JSONArray();
			record.put("Objects");
			record.put("do?action=getMeta");
			array.put(record);
		}else if("dtbs".equals(nodeType)){
			JSONArray record=new JSONArray();
			record.put("Database Metadata");
			record.put("do?action=getDatabaseMetadata");
			array.put(record);
			record=new JSONArray();
			record.put("Connection Status");
			record.put("do?action=getConnectionStatus");
			array.put(record);
		}
		PluginManager.getInstance().loadAddedDetails(array,sessionid,nodeType);
		JSONObject ret = new JSONObject();
		ret.put("details", array);
		return ret;
	}

}
