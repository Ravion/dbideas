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


package dbideas.dbtree;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dbideas.IDManager;




public abstract class DBNode implements IStructureNode {
	protected List<IStructureNode> children=new ArrayList<IStructureNode>();
	protected String id;
	protected SQLConnection conn;
	private  boolean loaded;
	public DBNode(SQLConnection conn){
		this.id=IDManager.get().nextID();
		IDManager.get().put(id,this);
		this.conn=conn;
	}
	 public void refresh() {
		loaded=false;
		children.clear();
	}
	public String getId() {
		return id;
	}
	final public SQLConnection getConn() {
		return conn;
	}
	
	final public  JSONObject getChildrenToJSon()  {
		try {
			load();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
//		JSONArray array=new JSONArray();
//		try{for(int i=0;i<children.size();i++){
//			array.put(((IStructureNode)children.get(i)).toJSON());
//		}
//		}catch(JSONException e){e.printStackTrace();}
		JSONObject js = new JSONObject();
		try{
			JSONArray arr=new JSONArray();
			for(int i=0;i<children.size();i++){
				JSONObject obj=new JSONObject();
				IStructureNode is = children.get(i);
				obj.put("text", is.getName() );
				obj.put("id",is.getId());
				obj.put("leaf",is.isLeaf());
				obj.put("type",is.getType());
				obj.put("cls",is.getCls());
				obj.put("qname",is.getQualifiedName());
				arr.put(obj);
			}
			js.put("nodes", arr);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return js;
	}
	public String getQualifiedName() {
		return null;
	}
	abstract protected void nodeLoad() throws SQLException;
	final public   void load() throws SQLException{
		if(loaded)return;
			nodeLoad();
		loaded=true;
	}
	final public List<IStructureNode> getChildren() {
		try {
			load();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return children;
	}
//	public  JSONObject toJSON() throws JSONException{
//		JSONObject obj=new JSONObject();
//		obj.put("text", getName());
//		obj.put("id",getId());
//		obj.put("leaf",isLeaf());
//		obj.put("type",getType());
//		obj.put("cls",getCls());
//		return obj;
//	}
}
