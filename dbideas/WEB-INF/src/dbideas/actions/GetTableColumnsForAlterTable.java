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

import net.sourceforge.squirrel_sql.fw.sql.TableColumnInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import dbideas.IDManager;
import dbideas.JSONAction;
import dbideas.dbtree.TableNode;


public class GetTableColumnsForAlterTable implements JSONAction {
	String id=null;
	public void setId(String id) {
		this.id = id;
	}

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		TableNode tn=(TableNode)IDManager.get().get(id);
		TableColumnInfo colsInfo[] = tn.getConn().getSQLMetaData().getColumnInfo(tn.getITableInfo());
		JSONObject ret=new JSONObject();
		JSONArray arr=new JSONArray();
		for(int i=0;i<colsInfo.length;i++){
			JSONObject obj=new JSONObject();
			obj.put("cname",colsInfo[i].getColumnName());
			obj.put("cnameold",colsInfo[i].getColumnName());
			obj.put("ctype", colsInfo[i].getTypeName().toUpperCase());
			obj.put("csize",colsInfo[i].getColumnSize());
			obj.put("cdecdig",colsInfo[i].getDecimalDigits());
			String remarks=colsInfo[i].getRemarks();
			if(remarks==null)
				remarks="";
			obj.put("ccom",remarks);
			String def=colsInfo[i].getDefaultValue();
			if(def==null)
				def="";
			obj.put("cdefault",def);
			String nullable=colsInfo[i].isNullable();
			boolean bnull=true;
			if(nullable!=null)
				bnull=!nullable.toUpperCase().equals("NO");
			obj.put("cacceptnull",bnull);
			obj.put("cacceptnullold",bnull);
			arr.put(obj);
		}
		ret.put("columns",arr);
		return ret;
	}

}
