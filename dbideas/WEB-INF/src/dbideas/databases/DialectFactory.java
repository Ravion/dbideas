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
import net.sourceforge.squirrel_sql.fw.sql.SQLDatabaseMetaData;


public class DialectFactory {
	
    private static final DB2Dialect db2Dialect = new DB2Dialect();
      
    private static final DerbyDialect derbyDialect = new DerbyDialect();
    
    private static final H2Dialect h2Dialect = new H2Dialect();
    
    private static final HSQLDialect hsqlDialect = new HSQLDialect();
    
    private static final MySQLDialect mysqlDialect = new MySQLDialect();
    
    private static final Oracle9iDialect oracle9iDialect = new Oracle9iDialect();
    
   private static final PostgreSQLDialect postgreSQLDialect = 
        new PostgreSQLDialect();

    private static final SybaseDialect sybaseDialect = new SybaseDialect();
    
    private static final SQLServerDialect sqlserverDialect = new SQLServerDialect();
    

	public static boolean isPostgreSQL(SQLConnection conn) {
        return dialectSupportsProduct(conn, postgreSQLDialect);        
    }    
	
	 public static boolean isOracle(SQLConnection conn) {
	        return dialectSupportsProduct(conn, oracle9iDialect);      
	 }
	 
	 public static boolean isMySQL(SQLConnection conn) {
	        return dialectSupportsProduct(conn, mysqlDialect);
	    }  
	
//	 private static HashMap sessionDialectMap = new HashMap();
	 
	private static boolean dialectSupportsProduct(SQLConnection conn, 
			IDialect dialect) 
	{
		boolean result = false;
		if (conn != null && dialect != null) {
			SQLDatabaseMetaData data = conn.getSQLMetaData();
			try {
				String productName = data.getDatabaseProductName();
				String productVersion = data.getDatabaseProductVersion();
				result = dialect.supportsDatabase(productName, productVersion);
			} catch (Exception e) {
				
			}
		}
		return result;
	}

	public static boolean isMSSQL(SQLConnection conn) {
		 return dialectSupportsProduct(conn, sqlserverDialect);
	}

	public static boolean isDerby(SQLConnection conn) {
		return dialectSupportsProduct(conn, derbyDialect);
	}
	public static boolean isHssql(SQLConnection conn) {
		return dialectSupportsProduct(conn, hsqlDialect);
	}
	public static boolean isH2(SQLConnection conn) {
		return dialectSupportsProduct(conn, h2Dialect);
	}

	public static boolean isDB2(SQLConnection conn) {
		return dialectSupportsProduct(conn, db2Dialect);
	}

	public static boolean isSyBase(SQLConnection _conn) {
		return dialectSupportsProduct(_conn, sybaseDialect);
	}

	public static boolean isInformix(SQLConnection _conn) {
		// TODO Auto-generated method stub
		return false;
	}
}
