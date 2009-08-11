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

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class IDManager {
	protected IDManager(){}
	private static ThreadLocal<IDManager> threadlocalIDManager = new ThreadLocal<IDManager>();
	public static IDManager get()
	{
		IDManager ex = threadlocalIDManager.get();
		return ex;
	}
	public static void set(IDManager ex)
	{
		threadlocalIDManager.set(ex);
	}
	AtomicInteger ai=new AtomicInteger(0);
	HashMap<String, Object> map=new HashMap<String, Object>();
	protected  int nextInt(){
		return ai.incrementAndGet();
	}
	public  String nextID(){
		return "0000"+Integer.toHexString(nextInt());
	}
	synchronized public  void put(String id, Object schemaNode) {
		map.put(id,schemaNode);
		
	}
	synchronized public  Object get(String id) {
		return map.get(id);
	}
}
