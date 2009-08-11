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

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import dbideas.JSONAction;
import dbideas.plugin.Plugin;
import dbideas.plugin.PluginManager;




public class PluginAction implements JSONAction {

	String pluginName;
	String pluginAction;
	public void setPluginAction(String pluginAction) {
		this.pluginAction = pluginAction;
	}
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		Plugin plug=PluginManager.getInstance().getPluginByName(pluginName);
		if(plug!=null){
			JSONObject str=plug.executeAction(request,response, em,et);
			return str;
		}
		return null;
	}

}
