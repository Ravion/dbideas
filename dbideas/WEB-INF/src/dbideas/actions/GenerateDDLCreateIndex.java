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

//import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dbideas.JSONAction;
import dbideas.WebSQLSession;


public class GenerateDDLCreateIndex implements JSONAction {
String tableid;
	
	public void setTableid(String tableid) {
	this.tableid = tableid;
}

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, WebSQLSession sessions,
			EntityManager em, EntityTransaction et) throws Exception {
//		TableNode tn=(TableNode)IDManager.get().get(tableid);
//		
//		String[] idxKeys = request.getParameterValues("idxKeys");
//		System.out.println(Arrays.asList(idxKeys));
//	
		JSONObject obj=new JSONObject();
		
//			if(idxKeys==null || idxKeys.length==0)
//				throw new Exception("No Columns have been selected");
//			boolean unique=request.getParameter("unique")!=null;
//			String indexName=request.getParameter("name");
//			
//			String sql="DDL not implemented";
//			SQLConnection conn=tn.getConn();
//			String tableQualifiedName=tn.getITableInfo().getQualifiedName();
//			if(DialectFactory.isOracle(conn)){
//				sql=buildOracleString(idxKeys, tableQualifiedName, unique, indexName);
//			}else if(DialectFactory.isMySQL(conn)){
//				sql=buildMySQLString(idxKeys, tableQualifiedName, unique, indexName);
//			}else if(DialectFactory.isMSSQL(conn)){
//				
//				sql=buildOracleString(idxKeys, tableQualifiedName, unique, indexName);
//			}else if(DialectFactory.isPostgreSQL(conn)){
//				sql=buildOracleString(idxKeys, tableQualifiedName, unique, indexName);
//			}else if(DialectFactory.isDerby(conn)){
//				sql=buildOracleString(idxKeys, tableQualifiedName, unique, indexName);
//			}else if(DialectFactory.isHssql(conn)){
//				sql=buildOracleString(idxKeys, tableQualifiedName, unique, indexName);
//			}else if(DialectFactory.isH2(conn)){
//				sql=buildOracleString(idxKeys, tableQualifiedName, unique, indexName);
//			}
//			
//			
//			obj.put("ddl", sql);
		
		return obj;
	}
//	private String buildOracleString(String [] idxKeys, String qualifiedName, boolean unique, String indexName){
//		StringBuilder sbuilder=new StringBuilder();
//		sbuilder.append("CREATE ");
//		if(unique){
//			sbuilder.append("UNIQUE ");
//		}
//		sbuilder.append("INDEX ");
//		sbuilder.append(indexName);
//		sbuilder.append(" ON ").append(qualifiedName);
//		sbuilder.append(" (");
//		for(int j=0;j<idxKeys.length;j++){
//			sbuilder.append(idxKeys[j]);
//			if(j<idxKeys.length-1)
//				sbuilder.append(",");
//		}
//		sbuilder.append(")");
//		return sbuilder.toString();
//	}
//	private String buildMySQLString(String [] idxKeys, String qualifiedName, boolean unique, String indexName){
//		StringBuilder sbuilder=new StringBuilder();
//		sbuilder.append("ALTER TABLE ");
//		
//		sbuilder.append(qualifiedName);
//		sbuilder.append(" ADD " );
//		if(unique){
//			sbuilder.append("UNIQUE ");
//		}else{
//			sbuilder.append("INDEX ");
//		}
//		
//		sbuilder.append(indexName);
//		sbuilder.append(" (");
//		for(int j=0;j<idxKeys.length;j++){
//			sbuilder.append(idxKeys[j]);
//			if(j<idxKeys.length-1)
//				sbuilder.append(",");
//		}
//		sbuilder.append(")");
//		return sbuilder.toString();
//	}

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
