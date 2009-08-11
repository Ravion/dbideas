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

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;
import dbideas.plugin.PluginManager;

public class CatalogNode extends DBNode implements IStructureNode{

	String catalogName;
	private boolean dummy;
	public CatalogNode( String catalogName,
			SQLConnection conn) {
		
		this(catalogName,conn,false);
	}
	
	public CatalogNode( String catalogName,
			SQLConnection conn,boolean dummy) {
		super(conn);
		this.dummy=dummy;
		this.catalogName=catalogName;
	}
	
	
	 
	@Override
	protected void nodeLoad() throws SQLException{
		
		String[] tbTypes = conn.getSQLMetaData().getTableTypes();
		for (int i = 0; i < tbTypes.length; ++i) {
			String tableType = tbTypes[i];
			children.add(new TablesNode(this, tableType, conn));
		}
		PluginManager pm=PluginManager.getInstance();
		pm.loadCatalogChildren(this,children,conn);
	}

	public String getName() {
		return catalogName;
	}
	
	public boolean isDummy() {
		return dummy;
	}

	public String getCls() {
		return "catalog";
	}

	public String getType() {
		
		return "ct";
	}

	public boolean isLeaf() {
		return false;
	}
}
