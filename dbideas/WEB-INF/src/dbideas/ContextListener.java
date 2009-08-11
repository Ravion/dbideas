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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import dbideas.dao.DriversDAO;


public class ContextListener implements ServletContextListener {

	private static EntityManagerFactory emf;
	public void contextDestroyed(ServletContextEvent sce) {
		EntityManagerFactory emf = (EntityManagerFactory)sce.getServletContext().getAttribute("emf");
		if(emf!=null)
			emf.close();
	}

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("dbIdeas");
		setEntityManagerFactory(emf);
		sc.setAttribute("emf",emf);
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		if(DriversDAO.getDrivers(em).isEmpty()){
			tx.begin();
			DriversDAO.initialize(em);
			tx.commit();
		}/*else{
			tx.begin();
			DriversDAO.deleteAll(em);
			DriversDAO.initialize(em);
			tx.commit();
		}*/
		tx.begin();
		DriversDAO.validateDrivers(em);
		tx.commit();
		
//		EntityTransaction tx = em.getTransaction();
		
//			tx.begin();
//			try {
//				SourcesDAO.deleteAllSources(em);
//				tx.commit();
//				tx.begin();
//				SourcesDAO.initialize(em);
//				tx.commit();
//			} catch (Exception e) {
//				tx.rollback();
//			}

		em.close();
		sc.setAttribute("dbIdeas_version", sc.getInitParameter("dbIdeas_version"));
	}
	public static void setEntityManagerFactory(EntityManagerFactory emf ) {
		ContextListener.emf=emf;
	}
	public static EntityManagerFactory getEntityManagerFactory(){
		return emf;
	}
}