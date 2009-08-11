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

package dbideas.dbtree;

public class ColumnModel {
	Object []el;
	public ColumnModel(int size){el=new Object[size];}
	public ColumnModel(Object[] obj) {
		el= new Object[8];
		el[0]=obj[3];//("COLUMN_NAME"); 
		el[1]=obj[4];//("COLUMN_NAME");
		el[2]= obj[5];//set.getString("TYPE_NAME");//map.get(new Integer(set.getShort("DATA_TYPE")));				
		//el[2]=new Integer(obj[6].toString());//new Integer(set.getInt("COLUMN_SIZE")); 
		el[3]=obj[6];//new Integer(set.getInt("COLUMN_SIZE"));
		//el[3]=new Integer(obj[8].toString());//new Integer(set.getInt("DECIMAL_DIGITS"));
		el[4]=obj[8];//new Integer(set.getInt("DECIMAL_DIGITS")); 
		el[7]=obj[11];//set.getString("REMARKS"); 
		el[5]=obj[12];//set.getString("COLUMN_DEF"); 
		el[6]=obj[17];
	}
	public Object getValue(int k){
		return el[k];
	}
	public void setValue(int k, Object value){
		el[k]=value;
	}
}
