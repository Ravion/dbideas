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

package dbideas.plugin;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import dbideas.dbtree.CatalogNode;
import dbideas.dbtree.IStructureNode;
import dbideas.dbtree.SchemaNode;



public interface Plugin {

	List<IStructureNode> getSchemaAddedChildren(SchemaNode schemaNode, SQLConnection conn);

	JSONArray[] getContextMenu(SQLConnection conn, String nodeType);

	JSONObject executeAction(HttpServletRequest request,
			HttpServletResponse response, 
			EntityManager em, EntityTransaction et) throws Exception;

	JSONArray[] getAddedTabs(SQLConnection conn, String nodeType);

	List<IStructureNode> getCatalogAddedChildren(CatalogNode schemaNode,
			SQLConnection conn);
	
	JSONArray[] getDynamicPluginScripts(SQLConnection conn);
}
