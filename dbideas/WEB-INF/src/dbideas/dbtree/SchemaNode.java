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

public class SchemaNode extends DBNode implements IStructureNode{

	String schemaName;
	
	private String catalogName;
	public SchemaNode( String schemaName,
			SQLConnection conn) {
		this(schemaName,conn,null);
	}
	public SchemaNode(String schemaName, SQLConnection conn, String catalogName) {
		super(conn);
		this.schemaName=schemaName;
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
		pm.loadSchemaChildren(this,children,conn);
		
	}
	
	public String getName() {
		return schemaName;
	}
	
	public String getCatalogName() {
		return catalogName;
	}
	public String getCls() {
		return "schema";
	}
	public String getType() {
		return "sc";
	}
	public boolean isLeaf() {
		return false;
	}
}
