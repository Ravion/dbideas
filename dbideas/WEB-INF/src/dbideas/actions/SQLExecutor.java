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
import java.sql.SQLException;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.json.JSONArray;

import dbideas.IDManager;
import dbideas.utils.ResultSetReader;

public class SQLExecutor {
	
	public SQLExecutor(SQLConnection sqlconn, int limit){
		this.sqlconn=sqlconn;
		this.id=IDManager.get().nextID();
		IDManager.get().put(id,this);
		this.limit=limit;
	}
	private SQLConnection sqlconn;
	private int limit;
	private PreparedStatement ps;
	private ResultSet rs;
	private int columncount;
	private String id;
	private ResultSetReader reader;
	boolean closed;
	
	public void executeQuery(String nextQuery, JSONArray data, JSONArray meta, JSONArray info)throws SQLException	{
		
		
		
		info.put(id);
		info.put(nextQuery);
//		if(sqlconn.getConnection() instanceof com.mysql.jdbc.Connection){
//			((com.mysql.jdbc.Connection)sqlconn.getConnection()).setProfileSQL(true);
//		}
		ps=sqlconn.prepareStatement(nextQuery);
		
		try {
			ps.setFetchSize(limit);
		} catch (Throwable e) {
		}
		boolean bHasResultSet=ps.execute();
//		SQLWarning warn=ps.getWarnings();
//		if(warn!=null)
//			warn.printStackTrace();
//		warn.
		if(bHasResultSet){
			rs=ps.getResultSet();
			if(rs!=null){
//				SQLWarning warn2 = rs.getWarnings();
//				if(warn2!=null)
//					warn2.printStackTrace();
				ResultSetMetaData metadata=rs.getMetaData();
				columncount=metadata.getColumnCount();
				
				for(int i=1;i<=columncount;i++){
					
					String label=metadata.getColumnLabel(i);
					meta.put(label);
				}
				reader=new ResultSetReader(rs);
				int loaded=0;
				Object[]row;
				while((row=reader.readRow())!=null){
					loaded++;
					JSONArray record=new JSONArray();
					for(int i=0;i<columncount;i++){
						Object obj=row[i];
						record.put(obj);
					}
					data.put(record);
					if(loaded==limit)
						break;
				}
				if( loaded<limit){
					closed=true;
					rs.close();
					ps.close();
				}
			}else{
				closed=true;
				ps.close();
			}
		}else{
			int updateCount=ps.getUpdateCount();
			closed=true;
			ps.close();
			meta.put("Update Count");
			JSONArray record=new JSONArray();
			record.put(updateCount);
			data.put(record);
		}
//		SQLWarning warn3 = sqlconn.getWarnings();
//		if(warn3!=null)
//			warn3.printStackTrace();
		
	}
	public void next(JSONArray data)throws Exception{
		int loaded=0;
		Object[]row;
		if(closed==false){
			while((row=reader.readRow())!=null){
				loaded++;
				JSONArray record=new JSONArray();
				for(int i=0;i<columncount;i++){
					Object obj=row[i];
					record.put(obj);
				}
				data.put(record);
				if(loaded==limit)
					break;
			}
			if( loaded<limit ){
				closed=true;
				rs.close();
				ps.close();
			}
		}
	}
	public void close(){
		closed=true;
		try {
			if(rs!=null)
				rs.close();
		} catch (SQLException e) {
		}
		try {
			if(ps!=null)
			ps.close();
		} catch (SQLException e) {
		}
	
	}
}
