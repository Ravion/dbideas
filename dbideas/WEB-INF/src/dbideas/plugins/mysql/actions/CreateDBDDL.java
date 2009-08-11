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

package dbideas.plugins.mysql.actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

import org.json.JSONObject;

public class CreateDBDDL {

	private SQLConnection conn;
	private String name;
	private String collation;

	public CreateDBDDL(SQLConnection conn, String name, String collation) {
		this.conn=conn;
		this.name=name;
		this.collation=collation;
	}

	public JSONObject execute()throws Exception {
		String sql="CREATE DATABASE `"+name+"`";
		if(collation!=null && !collation.trim().equals("")){
			String query="SELECT character_set_name FROM information_schema.collations where collation_name =?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1,collation);
			ResultSet rs = ps.executeQuery();
			rs.next();
			String characterset = rs.getString(1);
			rs.close();
			ps.close();
			sql=sql+" DEFAULT CHARACTER SET "+characterset+" COLLATE "+collation;
		}
		JSONObject obj=new JSONObject();
		obj.put("string", sql);
		return obj;
	}

}
