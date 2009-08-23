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
function FormGridFactory(){
};
function formgridbutton(item){
	  if(item.id=='next'){
		  if(item.formGrid.rowIndex<item.formGrid.myData.length-1){
			  item.formGrid.rowIndex=item.formGrid.rowIndex+1;
		  }
	  }
	  else if(item.id=='prev'){
		  if(item.formGrid.rowIndex>0){
			  item.formGrid.rowIndex=item.formGrid.rowIndex-1
		  }
	  }
	  else if(item.id=='first'){
		  item.formGrid.rowIndex=0
	  }
	  else {
		  item.formGrid.rowIndex=item.formGrid.myData.length-1;
	  }
	  
	  var myData2 = [];
  	if(item.formGrid.myData.length>0){
	    	for(i=0;i<item.formGrid.meta.length;i++){
	    		var rc=[];
	    		rc.push(item.formGrid.meta[i]);
	    		rc.push(item.formGrid.myData[item.formGrid.rowIndex][i]);
	    		myData2.push(rc);
	    	}
  	}
  	item.formGrid.store.loadData(myData2);
}; //end formgridbutton

FormGridFactory.prototype.build=function(queryID,meta,myData){
	 var store2 = new Ext.data.SimpleStore({
         fields: [
            {name: 'column',sortable: false},
            {name: 'value',sortable: false}
         ]
     });

     var formTB=new Ext.Toolbar({items:[]});
     var rowIndex=0;
     var formGrid = new Ext.grid.GridPanel({
         store: store2,
         formTB:formTB,
         columns: [
             {header: "Column",  dataIndex: 'column',
              	renderer:defaultRenderer},
             {header: "Value", dataIndex: 'value',width:250,renderer:defaultRenderer}
         ],
         stripeRows: true,
         myData:myData,
         meta:meta,
         queryID:queryID,
         rowIndex:rowIndex,
         additionalData:function(addedData,totalData){},
	     listeners: {
	     	render: function(){
   	       		gridnext=new Ext.Toolbar.Button({
   	       			cls: 'x-btn-icon',
   	       			icon:'icons/resultset_next.png',
   	  				tooltip:'<b>Next Record</b>',
   	  				handler:formgridbutton,
   	  				id:'next',
   	  				formGrid:this
   	  		    });
   	    	          
	    	    gridprev=new Ext.Toolbar.Button({
	    	    	cls: 'x-btn-icon',
   	       			icon:'icons/resultset_previous.png',
	    			tooltip:'<b>Previous Record</b>',
   	  				handler:formgridbutton	,
   	  				id:'prev',
   	  				formGrid:this
	    	    });
	    	         
	    	    gridfirst=new Ext.Toolbar.Button({
	    	    	cls: 'x-btn-icon',
   	       			icon:'icons/resultset_first.png',
	  				tooltip:'<b>First Record</b>'	,
	  				handler:formgridbutton,
	  				id:'first',
	  				formGrid:this
	  		    });
	    	          
	    	    gridlast=new Ext.Toolbar.Button({
	    	    	cls: 'x-btn-icon',
   	       			icon:'icons/resultset_last.png',
	    	    	tooltip:'<b>Last Available Record</b>',
   	  				handler:formgridbutton	,
   	  				id:'last',
   	  				formGrid:this
	    		});
	    	    this.formTB.add(gridfirst);
	    	    this.formTB.add(gridprev);
	    	    this.formTB.add(gridnext);
	    	    this.formTB.add(gridlast);
	    	    var myData2 = [];
		    	if(this.myData.length>0){
			    	for(i=0;i<this.meta.length;i++){
			    		var rc=[];
			    		rc.push(this.meta[i]);
			    		rc.push(this.myData[this.rowIndex][i]);
			    		myData2.push(rc);
			    	}
		    	}
			    this.store.loadData(myData2);
     		}
     	}
     	,bbar:formTB
     });
     return formGrid;
}

FormGridFactory.prototype.name=function(){
	return "Form";
}

sqlResultPluginFactories.push(new FormGridFactory());

var sqlTableTemplate=new Ext.XTemplate('<table class="sqltable">',
		 '<thead><tr><tpl for="meta">',
		 '<th class="sqlth">{.}</th>',
		 '</tpl></tr></thead>','<tpl for="data"><tr><tpl for=".">',
		 '<td  class="sqltd">{.}</td>',
		 '</tpl></tr></tpl></table>'
	);

	function HTMLGridFactory(){
	};
	HTMLGridFactory.prototype.build=function(queryID,meta,myData){
		return new Ext.Panel({
			html:'',autoScroll:true,
			myData:myData,
			queryID:queryID,
			meta:meta,
			loaded:false,
			listeners:{
	    		render: function(){
	        	 	this.loaded=true;
	        		var tmpObject=new Object();
	        		tmpObject["data"]=this.myData;
	        		tmpObject["meta"]=this.meta;
	        		sqlTableTemplate.overwrite(this.body, tmpObject, true);
	        	
	    		}
	    	},
			additionalData:function(addedData,totalData){
	    		if(this.loaded){
		    		var tmpObject=new Object();
		    		tmpObject["data"]=this.myData;
		    		tmpObject["meta"]=this.meta;
		    		sqlTableTemplate.overwrite(this.body, tmpObject, true);
	    		}
	    	}
		});
	}
	HTMLGridFactory.prototype.name=function(){
		return "HTML";
	}

sqlResultPluginFactories.push(new HTMLGridFactory());