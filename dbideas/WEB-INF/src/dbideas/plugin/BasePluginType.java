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

package dbideas.plugin;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dbideas.IDManager;
import dbideas.dbtree.IStructureNode;




public abstract class BasePluginType implements IStructureNode {
	public String getQualifiedName() {return null;}
	protected final String id;
	protected final SQLConnection conn;
	protected final List<IStructureNode>list=new ArrayList<IStructureNode>();
	protected boolean loaded;
	protected IStructureNode parentNode;
	protected String name;
	public IStructureNode getParent(){
		return parentNode;
	}
	public BasePluginType(String name,IStructureNode parentNode, SQLConnection conn){
		this.conn=conn;
		this.id=IDManager.get().nextID();
		IDManager.get().put(id,this);
		this.parentNode=parentNode;
		this.name=name;
	}
	final public String getId() {
		
		return id;
	}
	public JSONObject getChildrenToJSon() {
		load();
		
		
		JSONObject js = new JSONObject();
		try{
			JSONArray arr=new JSONArray();
			for(int i=0;i<list.size();i++){
				JSONObject obj=new JSONObject();
				IStructureNode is = list.get(i);
				obj.put("text", is.getName() );
				obj.put("id",is.getId());
				obj.put("leaf",is.isLeaf());
				obj.put("type",is.getType());
				obj.put("cls",is.getCls());
				arr.put(obj);
			}
			js.put("nodes", arr);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return js;
	}

	public abstract void load();
	
	public  String getName(){return name;}

	public  JSONObject toJSON() throws JSONException {
		JSONObject obj=new JSONObject();
		obj.put("text", getName());
		obj.put("id",getId());
		obj.put("leaf",isLeaf());
		obj.put("type",getType());
		obj.put("cls",getCls());
		return obj;
	}
	public void refresh() {
		loaded=false;
		list.clear();
	}
	public SQLConnection getConn() {
		return conn;
	}
}
