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

import java.util.Iterator;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.squirrel_sql.fw.sql.DataTypeInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import dbideas.IDManager;
import dbideas.JSONAction;
import dbideas.dbtree.TableNode;


public class GetTypesAlterTable implements JSONAction {
	String id=null;
	public void setId(String id) {
		this.id = id;
	}
	

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		TableNode tn=(TableNode)IDManager.get().get(id);
		DataTypeInfo[] dataTypeInfos = tn.getConn().getSQLMetaData().getDataTypes();
		JSONArray arr=new JSONArray();
		TreeSet<String> set=new TreeSet<String>();
		for(int i=0;i<dataTypeInfos.length;i++){
			//JSONObject obj=new JSONObject();
			set.add(dataTypeInfos[i].getSimpleName());
			//arr.put(obj);
		}
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String type = iterator.next();
			JSONObject obj=new JSONObject();
			obj.put("tp",type);
			arr.put(obj);
		}
		JSONObject ret=new JSONObject();
		ret.put("types",arr);
		return ret;
	}

}
