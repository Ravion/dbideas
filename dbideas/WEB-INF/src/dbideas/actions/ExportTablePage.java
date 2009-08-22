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
package dbideas.actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;
import dbideas.IDManager;
import dbideas.IPageAction;
import dbideas.dbtree.TableNode;

public class ExportTablePage implements IPageAction {
	String id;
	public void setId(String id) {
		this.id = id;
	}
	public void execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		TableNode tn=(TableNode)IDManager.get().get(id);
		request.setAttribute("qname", tn.getQualifiedName());
		String sql="SELECT COUNT(*) FROM "+tn.getQualifiedName();
		SQLConnection conn = tn.getConn();
		PreparedStatement ps=null;
		ResultSet rs=null;
		long count=0;
		try{
			ps=conn.prepareStatement(sql);
			rs = ps.executeQuery();
			rs.next();
			count=rs.getLong(1);
			
		}catch(Exception e){
			if(rs!=null){
				try {
					rs.close();
				} catch (Exception e1) {
				}
			}
			if(ps!=null){
				try {
					ps.close();
				} catch (Exception e1) {
				}
			}
		}
		request.setAttribute("rowCount", count);
		request.setAttribute("rnd",new Random().nextInt());
		request.getRequestDispatcher("exportTable.jsp").forward(request, response);

	}

}
