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
import java.sql.ResultSetMetaData;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;
import dbideas.IDManager;
import dbideas.IPageAction;
import dbideas.actions.export.ITableExporter;
import dbideas.actions.export.impl.ExcelTableExporter;
import dbideas.actions.export.impl.PDFTableExporter;
import dbideas.dbtree.TableNode;
import dbideas.utils.ResultSetReader;

public class DoExport implements IPageAction {
	String id; int count;String format;
	public void setId(String id) {
		this.id = id;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		TableNode tn=(TableNode)IDManager.get().get(id);
		String sql="SELECT * FROM "+tn.getQualifiedName();
		SQLConnection conn = tn.getConn();
		PreparedStatement ps=null;
		ResultSet rs=null;
		ITableExporter tExp=null;
		if("excel".equals(format))
			tExp=new ExcelTableExporter(tn.getQualifiedName());
		else
			tExp=new PDFTableExporter(tn.getQualifiedName());
		
		try{
			ps=conn.prepareStatement(sql);
			try{if(count>0)ps.setMaxRows(count);}catch(Exception e){}
			try{ps.setFetchSize(500);}catch(Exception e){}
			rs = ps.executeQuery();
			ResultSetMetaData metadata=rs.getMetaData();
			int columncount=metadata.getColumnCount();
			tExp.configure(metadata);
			ResultSetReader reader=new ResultSetReader(rs);
			Object[]row;
			while((row=reader.readRow())!=null){
				tExp.newLine();
				for(int i=0;i<columncount;i++){
					tExp.newCell(row[i]);
				}
			}
			tExp.finish();
		}finally{
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
		response.setContentLength(tExp.getContentSize());
		response.setContentType( tExp.getMimeType() ); 
		tExp.copyTo(response.getOutputStream());

	}

}
