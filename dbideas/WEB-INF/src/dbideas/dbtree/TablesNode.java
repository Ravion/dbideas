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

import net.sourceforge.squirrel_sql.fw.sql.ITableInfo;
import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

public class TablesNode extends DBNode implements IStructureNode{

	
	private String tableType;
	private IStructureNode parent;
	String catName = null, schemaName = null;
	public TablesNode(IStructureNode parent,String tableType, SQLConnection conn) {
		super(conn);
		
		this.tableType=tableType;
	
		this.parent=parent;
		if(parent instanceof CatalogNode) {
			if(!((CatalogNode)parent).isDummy())
				catName = parent.getName();
		} else if(parent instanceof SchemaNode) {
			schemaName = parent.getName();
			catName=((SchemaNode)parent).getCatalogName();
		}
	}

	 
	@Override
	public void nodeLoad()throws SQLException{

		ITableInfo[] tables = conn.getSQLMetaData().getTables(catName,
			schemaName, "%", new String[]{tableType},null); 

		for(int i=0;i<tables.length;i++){
			children.add(new
				TableNode(this,tables[i].getSimpleName(),tables[i].getRemarks(),tables[i],conn));
		}
	}


	public String getName() {
		return tableType;
	}
	
	public IStructureNode getParent() {
		return parent;
	}

	public String getTableType() {
		return tableType;
	}

	public String getCls() {
		
		if(tableType.toUpperCase().indexOf("VIEW")>-1){
			return "views";
		}else{
			return "tables";
		}
	}

	public String getType() {
		if(tableType.toUpperCase().indexOf("VIEW")>-1){
			return "views";
		}else{
			return "tbs";
		}
	}

	public boolean isLeaf() {
		return false;
	}


}
