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
import dbideas.dbtree.TableNode;





public class GetPKForAlterTable implements JSONAction {
	String id=null;
	public void setId(String id) {
		this.id = id;
	}

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		TableNode tn=(TableNode)IDManager.get().get(id);
		String pkName=tn.getPkName();
		List<String> pkColumnList=tn.getPrimaryKeyColumns();
		boolean hasPK=pkColumnList.size()>0;
		JSONArray arr=new JSONArray();
		JSONObject ret=new JSONObject();
		//for (Iterator<String> iterator = mp.keySet().iterator(); iterator.hasNext();) {
		if(hasPK){
			JSONObject obj=new JSONObject();
			obj.put("pkname", pkName);
			JSONArray colArray=new JSONArray();
			for(int i=0;i<pkColumnList.size();i++){
				colArray.put(pkColumnList.get(i));
			}
			obj.put("cols", colArray);
			arr.put(obj);
			ret.put("pk",arr);
		}
		//}
		return ret;
	}

}
