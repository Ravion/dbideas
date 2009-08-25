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

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.json.JSONArray;

import dbideas.IFileUploadAction;
import dbideas.actions.export.impl.PDFTableExporter;

public class PdfExport implements IFileUploadAction {

	public void execute(Map<String, FileItem> fileMap, Map<String,String> parameterMap,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		
		String meta_ = parameterMap.get("meta");
		String data_ = parameterMap.get("data");
		//String info_ = parameterMap.get("info");
		JSONArray meta=new JSONArray(meta_); 
		JSONArray data=new JSONArray(data_);
		//JSONArray info2=new JSONArray(info_);
		PDFTableExporter tableExporter=new PDFTableExporter(meta.length(),meta);
		/*ByteArrayOutputStream baos=new ByteArrayOutputStream(1024*16);
		Document doc=new Document(PageSize.A4.rotate(), 25, 25, 80, 25);
		PdfWriter writer = PdfWriter.getInstance(doc, baos);
		doc.open();
		*/
		for(int i=0;i<data.length();i++){
			JSONArray row=data.getJSONArray(i);
			tableExporter.newLine();
			for(int j=0;j<row.length();j++){
				tableExporter.newCell(row.get(j));
			}
		}
		tableExporter.finish();
//		doc.add(new Paragraph("hello"));
//		doc.close();
//		writer.close();
		response.setHeader("Pragma" ,"public");
		response.setHeader("Expires", "0"); // set expiration time
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setContentType(tableExporter.getMimeType());
		response.setHeader("Content-Disposition","attachment;filename=export.pdf");
		response.setContentLength(tableExporter.getContentSize());
		ServletOutputStream os = response.getOutputStream();
		tableExporter.copyTo(os);
	}

}
