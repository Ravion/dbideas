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
package dbideas.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import dbideas.entities.Source;


public class SourcesDAO {
	@SuppressWarnings("unchecked")
	public  static  List<Source> getSources(EntityManager em) {
		Query q=em.createNamedQuery("Source.selectAll");
		List<Source> ls=q.getResultList();
		return ls;
	}
	
	public  static  void deleteAllSources(EntityManager em) {
		//Query q=em.createNamedQuery("Source.deleteAll");
		//q.executeUpdate ();
		
		for (Source source : SourcesDAO.getSources(em)) {
			em.remove(source);
		}
	}
	
public static void addSource(EntityManager em, String sourcename, String jdbcUrl, int driverID, String userName){
		
		Source al = createSource( sourcename,  jdbcUrl,  driverID,  userName);
		em.persist(al);
	}
	private static Source createSource(String sourcename, String jdbcUrl, int driverID, String userName){
		Source al=new Source();
		al.setSourceName(sourcename);
		al.setDriverid(driverID);
		al.setJdbcUrl(jdbcUrl);
		al.setUserName(userName);
		al.setCreationDate(new Date());
		return al;
	}

	public static void updateSource(EntityManager em, int sourceid, String jdbcUrl, int driverid, String user, String sourcename) {
		Source as=em.find(Source.class, sourceid);
		as.setUserName(user);
		as.setDriverid(driverid);
		as.setJdbcUrl(jdbcUrl);
		as.setSourceName(sourcename);
		
		em.merge(as);
	}
	public  static  Source getSource(EntityManager em,int sourceid) {
		Source source=em.find(Source.class, Integer.valueOf(sourceid));
		return source;
		
	}
	public static void deleteSource(EntityManager em, int id) {
		Source as=em.find(Source.class, id);
		em.remove(as);
		
	}
}
