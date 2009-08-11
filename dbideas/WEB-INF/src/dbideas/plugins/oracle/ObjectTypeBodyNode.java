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

package dbideas.plugins.oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;
import dbideas.dbtree.SchemaNode;
import dbideas.plugin.BasePluginType;

public class ObjectTypeBodyNode extends BasePluginType {


	public ObjectTypeBodyNode(SchemaNode schemaNode,SQLConnection conn) {
		super("ObjectBodyType",schemaNode,conn);
	}

	
	private String getOwner() {
		return parentNode.getName();
	}
	
	 
	@Override
	public void load() {
		if(loaded )
			return;
		ResultSet rs=null;
		PreparedStatement ps=null;
		try{
			final String sql="SELECT  object_name,status, created,last_ddl_time,timestamp "+  
			" FROM sys.all_objects where owner=? "+  
			" and object_type='TYPE BODY' order by 1 asc";
			ps=conn.prepareStatement(sql);
			String owner=getOwner();
			ps.setString(1,owner);
			rs=ps.executeQuery();
			while(rs.next()){
				String name2=rs.getString(1);
				String status=rs.getString(2);
				ObjectBodyNode obNode=new ObjectBodyNode(this,name2,conn,status);
				list.add(obNode);
			}
		}catch(Exception e){
			
			list.clear();
		}finally{
			try{if(rs!=null)
				rs.close();
			}catch(Exception e){
			}	
			try{if(ps!=null)
				ps.close();
			}catch(Exception e){
			}	
		}		
		loaded=true;

	}


	public String getCls() {
		
		return "objs";
	}


	public String getType() {
		// TODO Auto-generated method stub
		return "ora_bodytypes";
	}


	public boolean isLeaf() {
		
		return false;
	}

}
