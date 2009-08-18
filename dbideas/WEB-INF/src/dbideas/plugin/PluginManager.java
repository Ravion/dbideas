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

import dbideas.IDManager;
import dbideas.dbtree.CatalogNode;
import dbideas.dbtree.IStructureNode;
import dbideas.dbtree.SQLSession;
import dbideas.dbtree.SchemaNode;
import dbideas.plugins.mysql.MySQLPlugin;
import dbideas.plugins.oracle.OraclePlugin;




public class PluginManager {
	private static PluginManager instance=new PluginManager();
	private PluginManager(){
		plugins.add(new OraclePlugin());
//		plugins.add(new ScriptPlugin());
		plugins.add(new MySQLPlugin());
//		plugins.add(new PostgresPlugin());
//		plugins.add(new SQLServerPlugin());
	}
	public static PluginManager getInstance(){
		return instance;
	}
	
	private ArrayList<Plugin> plugins=new ArrayList<Plugin>();
	
	public void loadSchemaChildren(SchemaNode schemaNode, List<IStructureNode> children, SQLConnection conn) {
		for (Plugin plugin : plugins) {
			try {
				List<IStructureNode> extnodes=plugin.getSchemaAddedChildren(schemaNode,conn);
				if(extnodes!=null && extnodes.size()>0){
					children.addAll(extnodes);
				}
			} catch (Exception e) {
			}
		}
		
	}
	
	public void loadCatalogChildren(CatalogNode catalogNode, List<IStructureNode> children, SQLConnection conn) {
		for (Plugin plugin : plugins) {
			try {
				List<IStructureNode> extnodes=plugin.getCatalogAddedChildren(catalogNode,conn);
				if(extnodes!=null && extnodes.size()>0){
					children.addAll(extnodes);
				}
			} catch (Exception e) {
			}
		}
		
	}
	
	public void dynamicPluginScripts(JSONArray arr,SQLConnection conn){
		for (Plugin plugin : plugins) {
			try{JSONArray[] objs = plugin.getDynamicPluginScripts(conn);
			if(objs!=null ){
				for(int i=0;i<objs.length;i++){
					arr.put(objs[i]);
				}
			}
			}catch(Exception e){}
		}
	}
	public void loadAddedMenu(JSONArray arr, String sessionid, String nodeType) {
		SQLSession sqlsession=(SQLSession)IDManager.get().get(sessionid);
		SQLConnection conn;
		if(sqlsession!=null){
			conn=sqlsession.getConn();
			for (Plugin plugin : plugins) {
				try {
					JSONArray [] objs=plugin.getContextMenu(conn,nodeType);
					if(objs!=null){
						for(int i=0;i<objs.length;i++){
							arr.put(objs[i]);
						}
					}
				} catch (Exception e) {
				}
			}
			
		}
		
	}
	public Plugin getPluginByName(String pluginName) {
		for (Plugin plugin : plugins) {
			if(plugin.getClass().getSimpleName().equals(pluginName)){
				return plugin;
			}
		}
		return null;
	}
	public void loadAddedDetails(JSONArray array, String sessionid,
			String nodeType) {
		SQLSession sqlsession=(SQLSession)IDManager.get().get(sessionid);
		SQLConnection conn;
		if(sqlsession!=null){
			conn=sqlsession.getConn();
			for (Plugin plugin : plugins) {
				try {
					JSONArray [] objs=plugin.getAddedTabs(conn,nodeType);
					if(objs!=null){
						for(int i=0;i<objs.length;i++){
							array.put(objs[i]);
						}
					}
				} catch (Exception e) {
				}
			}
		}
		
	}
}
