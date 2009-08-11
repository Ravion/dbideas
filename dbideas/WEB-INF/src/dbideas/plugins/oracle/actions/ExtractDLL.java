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

package dbideas.plugins.oracle.actions;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;
import dbideas.IDManager;
import dbideas.dbtree.IStructureNode;
import dbideas.dbtree.TableNode;
import dbideas.dbtree.TablesNode;
import dbideas.plugin.BasePluginType;

public class ExtractDLL {

	private String nodeid;

	public ExtractDLL(String nodeid) {
		this.nodeid=nodeid;
	}

	public String execute() {
		IStructureNode nd=(IStructureNode)IDManager.get().get(nodeid);
		if(nd instanceof TableNode){
			TableNode tb=(TableNode)nd;
			final String sql="select DBMS_METADATA.GET_DDL(?,?,?) FROM dual";
			PreparedStatement stmt =null;
			ResultSet rs=null;
			try{
				SQLConnection conn=tb.getConn();
				stmt = conn.prepareStatement(sql);
				TablesNode tot=tb.getParent();
				String parent_type=tot.getTableType().toUpperCase();
				//if("PACKAGE BODY".equals(parent_type))
				//	parent_type="PACKAGE_BODY";
				
				stmt.setString(1, parent_type);
				stmt.setString(2, tb.getName());
				
				
				
				String owner=tot.getParent().getName();
				stmt.setString(3,owner);
				//StringBuffer result = new StringBuffer(1000);
				rs=stmt.executeQuery();
				String txt=null;
				if(rs.next())
				{
					Clob clob=rs.getClob(1);
					txt = clob.getSubString(1, (int)clob.length());
				}
		
				return txt;
			}catch(Exception e){
				
			}finally{
				try {if(rs!=null)
					rs.close();
				} catch (SQLException e) {
				}
				try {if(stmt!=null)
					stmt.close();
				} catch (SQLException e) {
				}
			}
		}else if (nd instanceof BasePluginType){
			BasePluginType tb=(BasePluginType)nd;
			final String sql="select DBMS_METADATA.GET_DDL(?,?,?) FROM dual";
			PreparedStatement stmt =null;
			ResultSet rs=null;
			try{
				SQLConnection conn=tb.getConn();
				 stmt = conn.prepareStatement(sql);
				BasePluginType tot=(BasePluginType)tb.getParent();
				
				String parent_type=tot.getName().toUpperCase();
				if("PACKAGE BODY".equals(parent_type))
					parent_type="PACKAGE_BODY";
				
				stmt.setString(1, parent_type);
				
				//stmt.setString(1, tot.getName().toUpperCase());
				stmt.setString(2, tb.getName());
				
				String owner=tot.getParent().getName();
				stmt.setString(3,owner);
				//StringBuffer result = new StringBuffer(1000);
				 rs=stmt.executeQuery();
				String txt=null;
				if(rs.next())
				{
					Clob clob=rs.getClob(1);
					txt = clob.getSubString(1, (int)clob.length());
				}
		
				return txt;
			}catch(Exception e){
				
			}finally{
				try {if(rs!=null)
					rs.close();
				} catch (SQLException e) {
				}
				try {if(stmt!=null)
					stmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return null;
	}

}
