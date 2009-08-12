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

package dbideas;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONException;
import org.json.JSONObject;

import dbideas.actions.AboutPage;
import dbideas.actions.ChangeCatalog;
import dbideas.actions.CloseConnection;
import dbideas.actions.CloseResultSet;
import dbideas.actions.CommitConnection;
import dbideas.actions.ConfigPage;
import dbideas.actions.Connect;
import dbideas.actions.CreateSource;
import dbideas.actions.DeleteDriver;
import dbideas.actions.DeleteSource;
import dbideas.actions.ExecuteSQL;
import dbideas.actions.GenerateDDLAlterTable;
import dbideas.actions.GenerateDDLCreateIndex;
import dbideas.actions.GetAdditionalData;
import dbideas.actions.GetColumnsForViewer;
import dbideas.actions.GetConnectionStatus;
import dbideas.actions.GetDatabaseMetadata;
import dbideas.actions.GetDatabases;
import dbideas.actions.GetDetails;
import dbideas.actions.GetDrivers;
import dbideas.actions.GetExportedKeys;
import dbideas.actions.GetFK;
import dbideas.actions.GetGrants;
import dbideas.actions.GetIndexes;
import dbideas.actions.GetMenu;
import dbideas.actions.GetMeta;
import dbideas.actions.GetPK;
import dbideas.actions.GetPKForAlterTable;
import dbideas.actions.GetSources;
import dbideas.actions.GetTableColumns;
import dbideas.actions.GetTableColumnsForAlterTable;
import dbideas.actions.GetTableIndexesForAlterTable;
import dbideas.actions.GetTree;
import dbideas.actions.GetTypesAlterTable;
import dbideas.actions.PluginAction;
import dbideas.actions.Preview;
import dbideas.actions.RollbackConnection;
import dbideas.actions.SourcesPage;
import dbideas.actions.TestSourceConnection;
import dbideas.actions.UpdateDriver;
import dbideas.actions.UpdateSource;




@SuppressWarnings("serial")
public class Do extends HttpServlet {
	
	Map<String, Class<? extends JSONAction>> jsonActionMap;
	Map<String, Class<? extends IPageAction>> pageActionMap;
	
	 
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		HashMap<String, Class<? extends JSONAction>> tmp = new HashMap<String, Class<? extends JSONAction>>();
		tmp.put("generateDDLAlterTable",GenerateDDLAlterTable.class);
		tmp.put("generateDDLCreateIndex",GenerateDDLCreateIndex.class);
		tmp.put("getTableColumns",GetTableColumns.class);
		tmp.put("getTableIndexesAlterTable",GetTableIndexesForAlterTable.class);
		tmp.put("getTableColumnsAlterTable",GetTableColumnsForAlterTable.class);
		tmp.put("getPKAlterTable",GetPKForAlterTable.class);
		tmp.put("getTypesAlterTable",GetTypesAlterTable.class);
		tmp.put("getMenu",GetMenu.class);
		tmp.put("pluginAction",PluginAction.class);
		tmp.put("getPK",GetPK.class);
		tmp.put("getFK",GetFK.class);
		tmp.put("getMeta",GetMeta.class);
		tmp.put("getExportedKeys",GetExportedKeys.class);
		tmp.put("getAdditionalData",GetAdditionalData.class);
		tmp.put("changeCatalog",ChangeCatalog.class);
		tmp.put("getColumnsForViewer",GetColumnsForViewer.class);
		tmp.put("getSources",GetSources.class);
		tmp.put("getIndexes",GetIndexes.class);
		tmp.put("getDatabaseMetadata",GetDatabaseMetadata.class);
		tmp.put("getConnectionStatus",GetConnectionStatus.class);
		tmp.put("getGrants",GetGrants.class);
		tmp.put("closeResultSet", CloseResultSet.class);
		tmp.put("commitConnection",CommitConnection.class);
		tmp.put("closeConnection",CloseConnection.class);
		tmp.put("rollbackConnection", RollbackConnection.class);
		tmp.put("getDrivers",GetDrivers.class);
		tmp.put("getDetails",GetDetails.class);
		tmp.put("deleteDriver",DeleteDriver.class);
		tmp.put("updateDriver",UpdateDriver.class);
		tmp.put("preview",Preview.class);
		
		
		tmp.put("updateSource",UpdateSource.class);
		
		tmp.put("createSource",CreateSource.class);
		
		
		tmp.put("deleteSource",DeleteSource.class);
		
	
		tmp.put("getTree",GetTree.class);
		tmp.put("getDatabases",GetDatabases.class);
		
		tmp.put("testSourceConnection",TestSourceConnection.class);
		tmp.put("connect",Connect.class);
		
		tmp.put("execute",ExecuteSQL.class);
		jsonActionMap=Collections.unmodifiableMap(tmp);
		
		HashMap<String, Class<? extends IPageAction>> tmp2 = new HashMap<String, Class<? extends IPageAction>>();
		
		
		//tmp2.put("selectTable",SelectTable.class);
		
		tmp2.put("about",AboutPage.class);
		tmp2.put("config",ConfigPage.class);
		
		tmp2.put("sourcesPage",SourcesPage.class);
		
		pageActionMap=Collections.unmodifiableMap(tmp2);
		
	}
	
	 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session=req.getSession(true);
		
		synchronized (session) {
			WebSQLSession sessions=(WebSQLSession)session.getAttribute("sessions");
			if(sessions!=null){
				IDManager idmanager=sessions.getIDManager();
				IDManager.set(idmanager);
			}else{
				WebSQLSession newsessions=new WebSQLSession();
				IDManager.set(newsessions.getIDManager());
				session.setAttribute("sessions", newsessions);
				sessions=newsessions;
			}
		}
		
		
		EntityManagerFactory emf=(EntityManagerFactory)session.getServletContext().getAttribute("emf");
		EntityManager em=emf.createEntityManager();
		
		EntityTransaction et=null;
		
		
		String usernamesession=(String)session.getAttribute("loggeduser");
		String username=null;
	    // Do we allow that user?
	    if(usernamesession==null){
	    	String auth = req.getHeader("Authorization");
	    	username=allowUser(em,auth);
	    }
	    if (usernamesession==null && username==null) {
	    	em.close();
	    	resp.setContentType("text/plain");
	    	resp.setHeader("WWW-Authenticate", "BASIC realm=\"dwLoader users\"");
	    	resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	    	return;
	    }else if(usernamesession==null){
	    	usernamesession=username;
	    	session.setAttribute("loggeduser", username);
	    	int dot=usernamesession.indexOf(".");
		    if(dot>-1){
		    	String shortName=username.substring(0,dot);
		    	shortName=shortName.substring(0, 1).toUpperCase() + shortName.substring(1).toLowerCase();
		    	session.setAttribute("shortName", shortName);
		    }else{
		    	String capitalized=username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
		    	session.setAttribute("shortName", capitalized);
		    }
	    }
	    
	    String action=req.getParameter("action");
		if(action.equals("main")){
			em.close();
			req.getRequestDispatcher("/main.jsp").forward(req, resp);
			return;
		}
		
		Class<? extends JSONAction> iactionclass=jsonActionMap.get(action);
		if(iactionclass!=null){
			JSONObject obj=new JSONObject();
			PrintWriter writer=resp.getWriter();
			try {
				JSONAction iaction=iactionclass.newInstance();
				BeanUtils.populate(iaction, req.getParameterMap());
				
				et=em.getTransaction();
				et.begin();
				JSONObject objsr=iaction.execute(req, resp, em,et);
				if(et.isActive())
					et.commit();
				resp.setHeader("Content-Type", "text/html;charset=ISO-8859-1");
				obj.put("success",true);
				if(objsr!=null)
					obj.put("result",objsr);
			}
			catch (Exception e) {
				//e.printStackTrace();
				if(et!=null && et.isActive())
					et.rollback();
				try {
					
					obj.put("success",false);
					obj.put("error",e.toString());
				} catch (JSONException e1) {
				}
				
			}finally{
				IDManager.set(null);
				if(em!=null)
					em.close();
			}
			writer.write(obj.toString());
		}else{
			Class<? extends IPageAction> iPageActionclass=pageActionMap.get(action);
			if(iPageActionclass!=null){
				try{
					IPageAction iPageAction=iPageActionclass.newInstance();
					BeanUtils.populate(iPageAction, req.getParameterMap());
					et=em.getTransaction();
					et.begin();
					iPageAction.execute(req, resp, em,et);
					et.commit();
				}catch(Exception e){
					//e.printStackTrace();
					//TODO return page with error
					if(et!=null && et.isActive())
						et.rollback();
					try{
						req.setAttribute("pageid", req.getParameter("pageid"));
						req.setAttribute("emsg",e.getMessage());
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						pw.close();sw.close();
						
						req.setAttribute("error",sw.toString());
						req.getRequestDispatcher("error.jsp").forward(req, resp);
					}catch(Exception e1){}
				}finally{
					IDManager.set(null);
					if(em!=null)
						em.close();
				}
			}else
				if(em!=null)
					em.close();
		}
		
		
	}
	

	 
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	protected String allowUser(EntityManager em, String auth)  {

		return "User";
	   
	  }
}
