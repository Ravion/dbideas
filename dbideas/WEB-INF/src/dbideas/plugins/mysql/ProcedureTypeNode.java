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

package dbideas.plugins.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;
import dbideas.dbtree.CatalogNode;
import dbideas.dbtree.IStructureNode;
import dbideas.plugin.BasePluginType;

public class ProcedureTypeNode extends BasePluginType implements IStructureNode{

	@Override
	public void load(){	
		if(loaded )
			return;
		ResultSet rs=null;
		PreparedStatement ps=null;
		try{
			final String sql="select routine_name from information_schema.routines where routine_schema=? and routine_type='PROCEDURE' order by 1 asc";
			ps=conn.prepareStatement(sql);
			String owner=getOwner();
			ps.setString(1,owner);
			rs=ps.executeQuery();
			while(rs.next()){
				String oname=rs.getString(1);
				ProcedureNode functNode=new ProcedureNode(this,oname,conn);
				list.add(functNode);
			}
	
		}catch(Exception e){
			list.clear();
		}finally{
			try{if(rs!=null)
				rs.close();
			}catch(Exception e){
			}	
			try{
				if(ps!=null)
					ps.close();
			}catch(Exception e){
			}	
		}		
		loaded=true;
	}
	
	private String getOwner() {
		return parentNode.getName();
	}

	public ProcedureTypeNode(CatalogNode caNode,SQLConnection conn) {
		super("Procedure",caNode,conn);
	}

	public String getCls() {
		
		return "objs";
	}

	public String getType() {
		
		return "mysql_procs";
	}

	public boolean isLeaf() {
		
		return false;
	}

}
