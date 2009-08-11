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
package dbideas.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@NamedQueries(value={
		@NamedQuery (name="Source.selectAll",query="SELECT p FROM Source p"),
		@NamedQuery (name="Source.deleteAll",query="delete FROM Source p")	
	}
)

@Entity
@Table(name = "DWL_SOURCE")
public class Source {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "SOURCE_ID")
	int id;
	
	@Column(name = "SOURCE_NAME",nullable=false,length=255,unique=true)
	String sourceName;
	
	
	@Column(name = "JDBC_URL",nullable=false,length=255)
	String jdbcUrl;
	
	@Column(name = "USER_NAME",nullable=false,length=255)
	String userName;
	
	
		
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	Date creationDate;
	
	@Column(name = "DRIVER_ID",nullable=false)
	int driverid;
	
	@Version
	int version;
	
	
	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getDriverid() {
		return driverid;
	}
	
	public void setDriverid(int driverid) {
		this.driverid = driverid;
	}
}
