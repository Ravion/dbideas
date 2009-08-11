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

package dbideas.databases;


import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

public class DBSyntaxGeneratorFactory {
	private DBSyntaxGeneratorFactory(){}
	public static ISyntaxGenerator getSyntaxGenerator( SQLConnection conn){
		if(DialectFactory.isOracle(conn)){
			return new OracleSyntaxGenerator();
		}
		else if(DialectFactory.isMySQL(conn)){
			return new MySqlSyntaxGenerator();
		}else if(DialectFactory.isMSSQL(conn)){
			return new MSSQLSyntaxGenerator();
		}else if(DialectFactory.isPostgreSQL(conn))
			return new PostgreSQLSyntaxGenerator();
		return null;
	}
}
