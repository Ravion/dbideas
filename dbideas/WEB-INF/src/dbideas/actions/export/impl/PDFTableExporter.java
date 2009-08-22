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
package dbideas.actions.export.impl;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import dbideas.actions.export.IColumnFormatter;
import dbideas.actions.export.ITableExporter;

public class PDFTableExporter implements ITableExporter {
	IColumnFormatter formatters[];
	class PageHeaderFooterHandler extends PdfPageEventHelper{
		PageHeaderFooterHandler(){
			
		}
		PdfTemplate tpl;
		PdfPTable headerTable;
		private BaseFont times;
		@Override
		public void onCloseDocument(PdfWriter writer, Document document) {
			tpl.beginText();
			tpl.setFontAndSize(times, 9);
			tpl.setTextMatrix(0, 0);
			tpl.showText("" + (writer.getPageNumber() - 1));
			tpl.endText();
		}
		@Override
		public void onOpenDocument(PdfWriter writer, Document document) {
			try {
				times = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, false);
				// initialization of the header table
				
				headerTable = new PdfPTable(1);
				headerTable.getDefaultCell().setBorderWidth(0);
				headerTable.getDefaultCell().setBorderWidthBottom(1);
				headerTable.getDefaultCell().setPaddingBottom(5);
				headerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
				
				Phrase p = new Phrase();
				
				Chunk ck = new Chunk(tableName +" Data Export\n"); 
				p.add(ck);
				DateFormat sdfdate = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.ENGLISH);
				ck = new Chunk("Export date: "+sdfdate.format(new Date())+"\n",defaultFont);
				p.add(ck);
				headerTable.addCell(p);
				
				
				
				tpl = writer.getDirectContent().createTemplate(100, 100);
				tpl.setBoundingBox(new Rectangle(-20, -20, 100, 30));
				// initialization of the font

			}
			catch(Exception e) {
				
			}
		}    
		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			PdfContentByte cb = writer.getDirectContent();
			cb.saveState();
			// write the headertable
			headerTable.setTotalWidth(document.right() - document.left());
			headerTable.writeSelectedRows(0, -1, document.left(), document.getPageSize().getHeight() - 20, cb);
			// compose the footer
			String text = "Page " + writer.getPageNumber() + " of ";
			float textSize = times.getWidthPoint(text, 9);
			float textBase = document.bottom() - 10;
			cb.beginText();
			cb.setFontAndSize(times, 9);
			cb.setTextMatrix(document.left(), textBase);
			cb.showText(text);
			cb.endText();
			cb.addTemplate(tpl, document.left() + textSize, textBase);
		}

	}
	ByteArrayOutputStream baos=new ByteArrayOutputStream(1024*16);
	Document doc=new Document(PageSize.A4.rotate(), 25, 25, 80, 25);
	PdfWriter writer=null;
	PdfPTable table=null;
	private Font defaultFont;
	private Paragraph emptyPhrase=new Paragraph("");
	private PdfPCell emptyCell;	
	private int columnCount=-1;
	private Font headerFont;
	private final Color grayBorderColor=new Color(150,150,150);
	private final Color grayBackgroundColor=new Color(180,180,180);
	private final String tableName;
	private int rowIndex;
	public PDFTableExporter(String tableName) throws DocumentException{
		this.tableName = tableName;
		headerFont =new Font(Font.HELVETICA,9);
		headerFont.setStyle(Font.BOLD);
		headerFont.setColor(Color.white);
		defaultFont =new Font(Font.TIMES_ROMAN,Font.DEFAULTSIZE-1);
		emptyCell = new PdfPCell(emptyPhrase);
		writer =PdfWriter.getInstance(doc, baos);
		writer.setPageEvent(new PageHeaderFooterHandler());
		doc.addAuthor("dbIdeas");
		doc.addCreationDate();
		doc.addTitle(tableName);
		doc.open();
		
	}
	public void configure(ResultSetMetaData rsmd)  {
		try {
			columnCount=rsmd.getColumnCount();
			table=new PdfPTable(columnCount);
			table.setWidthPercentage(100);
			formatters=new IColumnFormatter[columnCount];
			for(int i=0;i<columnCount;i++){
				int type=rsmd.getColumnType(i+1);
				switch(type){
					case Types.DATE:
						formatters[i]=new IColumnFormatter(){
							DateFormat sdfdate = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
							public String format(Object obj) {
								
								return sdfdate.format((java.sql.Date)obj);
							}

							public int getAlign() {
								
								return Element.ALIGN_LEFT;
							}
							
						};
						break;
					case Types.TIME:
						formatters[i]=new IColumnFormatter(){
							DateFormat sdfdate = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.ENGLISH);
							public String format(Object obj) {
								
								return sdfdate.format((java.sql.Time)obj);
							}

							public int getAlign() {
								
								return Element.ALIGN_LEFT;
							}
							
						};
						break;
					case Types.TIMESTAMP :
                    case -101 : // Oracle's 'TIMESTAMP WITH TIME ZONE' == -101  
                    case -102 :
                    	formatters[i]=new IColumnFormatter(){
							DateFormat sdfdate = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM,Locale.ENGLISH);
							public String format(Object obj) {
								
								return sdfdate.format((java.sql.Timestamp)obj);
							}

							public int getAlign() {
								
								return Element.ALIGN_LEFT;
							}
							
						};
						break;
					
					case Types.DOUBLE:
					case Types.FLOAT:
					case Types.REAL:
						formatters[i]=new IColumnFormatter(){
							DecimalFormat df=new DecimalFormat("#####################0.00",new DecimalFormatSymbols(Locale.ENGLISH));
							public String format(Object obj) {
								Double d=(Double)obj;
								return df.format(d);
							}

							public int getAlign() {
								
								return Element.ALIGN_RIGHT;
							}
							
						};
						break;
					
					case Types.DECIMAL:
					case Types.NUMERIC:
						formatters[i]=new IColumnFormatter(){
							DecimalFormat df=new DecimalFormat("#####################0.00",new DecimalFormatSymbols(Locale.ENGLISH));
							public String format(Object obj) {
								BigDecimal d=(BigDecimal)obj;
								return df.format(d);
							}

							public int getAlign() {
								
								return Element.ALIGN_RIGHT;
							}
							
						};
						break;
					case Types.BIGINT :
						formatters[i]=new IColumnFormatter(){

							public String format(Object obj) {
								return obj.toString();
							}

							public int getAlign() {
								return Element.ALIGN_RIGHT;
							}};
						break;
					case Types.INTEGER:
					case Types.SMALLINT:
					case Types.TINYINT:
						formatters[i]=new IColumnFormatter(){

							public String format(Object obj) {
								return obj.toString();
							}

							public int getAlign() {
								return Element.ALIGN_RIGHT;
							}};
							break;
				}
				table.addCell(createHeaderCell(rsmd.getColumnLabel(i+1)));
			}
			table.setHeaderRows(1);
		} catch (SQLException e) {
		}

	}

	private PdfPCell createCellDefFont(String str,int alignement){
		PdfPCell cell =  new PdfPCell(new Paragraph(str,defaultFont));
		//cell.setBorder(0);
		cell.setPadding(5);
		cell.setHorizontalAlignment(alignement);
		return cell;
	}
	private PdfPCell createHeaderCell(String label){
		PdfPCell cell =  new PdfPCell(new Paragraph(label,headerFont));
		cell.setBorder(1);
		cell.setBorderWidth(1);


		cell.setBorderColor(grayBorderColor);
		cell.setBackgroundColor(grayBackgroundColor.darker());
		cell.setPadding(5);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		return cell;
	}
	
	public void newCell(Object obj) {
		if(obj!=null){
			IColumnFormatter iformatter = formatters[rowIndex];
			if(iformatter==null){
				table.addCell(createCellDefFont(obj.toString(),Element.ALIGN_LEFT));
			}else{
				table.addCell(createCellDefFont(iformatter.format(obj).toString(),iformatter.getAlign()));
			}
		}
		else
			table.addCell(emptyCell);
		rowIndex++;

	}

	public void newLine() {
		rowIndex=0;

	}

	public void copyTo(OutputStream os) throws IOException {
		baos.writeTo(os);
		
	}

	public int getContentSize() {
		
		return baos.size();
	}

	public String getMimeType() {
		return "application/pdf";
	}
	public void finish() {
		
		try {
			if(table.getRows().size()<2){
				doc.add(new Paragraph("Table is empty",defaultFont));
			}else{
				doc.add(table);
			}
		} catch (DocumentException e) {
		}
		doc.close();
		writer.close();
	}

}
