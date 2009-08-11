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

import java.util.List;

public interface ISyntaxGenerator {
	public String sep="\r\n";
	void dropColumn(StringBuilder sb,String tableQualifiedName, String droppedColumn);
	void renameColumn(StringBuilder sb,String tableQualifiedName, String oldColumnName, String columnName);
	void changeColumn(StringBuilder sb,String tableQualifiedName,String oldColumnName, String columnName,String type, String size, String decDigits,String remarks, String defValue,boolean acceptNull, boolean acceptNullOldValue);
	void newColumn(StringBuilder sb,String tableQualifiedName,String columnName,String type, String size, String decDigits,String remarks, String defValue,boolean acceptNull);
	void dropIndex(StringBuilder strbuilder, String qualifiedName,
			String indexName);
	void newIndex(StringBuilder strbuilder, String qualifiedName, String indexName,
			boolean unique, List<String> cols);
	void newPK(StringBuilder strbuilder, String qualifiedName, String pkname,
			List<String> lsCols);
	void dropPK(StringBuilder strbuilder, String qualifiedName, String droppedPK);
}
