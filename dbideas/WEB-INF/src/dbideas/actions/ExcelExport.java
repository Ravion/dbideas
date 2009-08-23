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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import dbideas.IPageAction;

public class ExcelExport implements IPageAction {

	String ex;
	public void setEx(String ex) {
		this.ex = ex;
	}
	@SuppressWarnings("unchecked")
	public void execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		ServletRequestContext requestContext=new ServletRequestContext(request);
		HashMap<String,String> parameterMap=new HashMap<String,String>();
		HashMap<String, FileItem> fileMap=new HashMap<String, FileItem>();
		if(FileUploadBase.isMultipartContent(requestContext)){
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			 
			
			List<FileItem> items;
			
			try {
				//items = (List<FileItem>)upload.parseRequest(request);
				items =upload.parseRequest(request);
				Iterator<FileItem> iter = items.iterator();
				while(iter.hasNext()){
					FileItem item = iter.next();
					if(item.isFormField()){
						parameterMap.put(item.getFieldName(), item.getString());
					}else{
						fileMap.put(item.getFieldName(), item);
					}
				}
			} catch (FileUploadException e) {
			}
			//System.out.println(fileMap);
			//System.out.println(parameterMap);
		}
		/*
		 * header("Content-Type: application/force-download");
header('Content-type: application/vnd.ms-excel');
header("Content-Disposition:attachment;filename=export");
		 */
		ex=parameterMap.get("ex");
		System.out.println(ex);
//		response.setHeader("Pragma" ,"public");
//		response.setHeader("Expires", "0"); // set expiration time
//		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
//		response.setHeader("Content-Type","application/force-download");
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition","attachment;filename=export.xls");
		response.setContentLength(ex.length());
		ServletOutputStream os = response.getOutputStream();
		os.write(ex.getBytes());
		os.flush();
		os.close();
	}

}
