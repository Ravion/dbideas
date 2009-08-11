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

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

public class CatalogOfSchemasNode extends DBNode implements IStructureNode {


	private String text;
	
	private String[] schemas;
	
	public CatalogOfSchemasNode(String catalogName, String[] schemas,
			SQLConnection conn) {
		super(conn);
		this.text=catalogName;
		this.schemas=schemas;
	}

	

	 
	@Override
	protected void nodeLoad() {
		if(schemas!=null)
			for(int i=0;i<schemas.length;i++){
				SchemaNode sn=new SchemaNode(schemas[i],conn,text);
				children.add(sn);
			}
	}

	public String getName() {
		return text;
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
