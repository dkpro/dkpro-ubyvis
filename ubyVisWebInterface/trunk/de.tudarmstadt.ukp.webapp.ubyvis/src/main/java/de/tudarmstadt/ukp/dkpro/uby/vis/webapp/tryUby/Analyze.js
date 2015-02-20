/*******************************************************************************
 * Copyright 2015
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
function graphs(id, json, lemma){

    data = json;
    
    var OW_eng = [],
		OW_deu = [],
		WikiDE = [],
		WikiEN = [],
		WktDE = [],
		WktEN = [],
		FN = [],
		VN = [],
		WN = [],
		OntoWktEN = [],
		OntoWktDE = [];
        
    for(var i=0;i<data.length;i++){
    	
    	size = data[i].length;
    	
    	if(size > 1){
    		
    		temp_arr = data[i];
    		
    		for(var j=0;j<size;j++){
    			
    			sd_temp = temp_arr[j].senseId;
                var res = sd_temp.split("_");

                if (res[1] == "eng" || res[1] == "deu") {
                    res_color = res[0] + "_" + res[1];
                } else {
                    res_color = res[0];
                }

                switch (res_color) {

                    case "OW_eng":
                        OW_eng.push(temp_arr[j]);
                        break;
                    case "OW_deu":
                        OW_deu.push(temp_arr[j]);
                        break;
                    case "WikiEN":
                        WikiEN.push(temp_arr[j]);
                        break;
                    case "WikiDE":
                        WikiDE.push(temp_arr[j]);
                        break;
                    case "WktEN":
                        WktEN.push(temp_arr[j]);
                        break;
                    case "WktDE":
                        WktDE.push(temp_arr[j]);
                        break;
                    case "FN":
                        FN.push(temp_arr[j]);
                        break;
                    case "VN":
                        VN.push(temp_arr[j]);
                        break;
                    case "WN":
                        WN.push(temp_arr[j]);
                        break;
                    case "OntoWktEN" :
                    	OntoWktEN.push(temp_arr[j]);
                    	break;
                    case "OntoWktDE" :
                    	OntoWktDE.push(temp_arr[j]);
                    	break;
                } 
    			
    		}
    		
    	}else if(size == 1){
    		
    		 sd = data[i];
    		 
    		 sd_temp = sd[0].senseId;
             var res = sd_temp.split("_");

             if (res[1] == "eng" || res[1] == "deu") {
                 res_color = res[0] + "_" + res[1];
             } else {
                 res_color = res[0];
             }

             switch (res_color) {

             case "OW_eng":
                 OW_eng.push(sd);
                 break;
             case "OW_deu":
                 OW_deu.push(sd);
                 break;
             case "WikiEN":
                 WikiEN.push(sd);
                 break;
             case "WikiDE":
                 WikiDE.push(sd);
                 break;
             case "WktEN":
                 WktEN.push(sd);
                 break;
             case "WktDE":
                 WktDE.push(sd);
                 break;
             case "FN":
                 FN.push(sd);
                 break;
             case "VN":
                 VN.push(sd);
                 break;
             case "WN":
                 WN.push(sd);
                 break;
             case "OntoWktEN": 
             	OntoWktEN.push(sd);
             	break;
             case "OntoWktDE": 
             	OntoWktDE.push(sd);
             	break;
                 	
             }
    
    	}
    	
    }
    
    var margin = {top: 20, right: 20, bottom: 100, left: 50},
    width = 400 - margin.left - margin.right,
    height = 400 - margin.top - margin.bottom;

    var x = d3.scale.ordinal().rangeRoundBands([0, width], .1);

    var y = d3.scale.linear().range([height,0]);

	var xAxis = d3.svg.axis()
		.scale(x)
		.orient("bottom");

	var yAxis = d3.svg.axis()
    	.scale(y)
    	.orient("left")
    	.ticks(5);

	var no_of_senses_graph = d3.select("#" + id).append("div")
                .attr("class", "sensesGraph")
                .style("width", width + margin.left + margin.right + "px")
                .style("height", height + margin.top + margin.bottom + "px");
	
	var graphText = d3.select("#" + id).append("div")
    .attr("class", "graphText")
    .attr("width", 300)
    .attr("height", 120)
    .attr('transform', 'translate(0,0)');

	graphText.append("text")
    .attr("x", "25") 
    .attr("y", "20")
    .style("font-size","16px")
    .text("Resource frequency of lemma " + "\""+lemma+"\"");
	
	var svgSenses = d3.select(".sensesGraph").append("svg")
		.attr("width", width + margin.left + margin.right)
		.attr("height", height + margin.top + margin.bottom)
      .append("g")
      	.attr("transform", "translate(50,10)");
	
	var data = [
	            { "res": "FrameNet",          "color": "#9467bd", "value" : FN.length }, 
                { "res": "OmegaWiki_DE",      "color": "#ff9896", "value" : OW_deu.length }, 
                { "res": "OmegaWiki_EN",      "color": "#d62728", "value" : OW_eng.length }, 
                { "res": "OntoWiktionary_DE", "color": "#ffbb78", "value" : OntoWktDE.length }, 
                { "res": "OntoWiktionary_EN", "color": "#ff7f0e", "value" : OntoWktEN.length },
                { "res": "VerbNet",           "color": "#bcbd22", "value" : VN.length }, 
                { "res": "Wikipedia_DE",      "color": "#aec7e8", "value" : WikiDE.length }, 
                { "res": "Wikipedia_EN",      "color": "#1f77b4", "value" : WikiEN.length}, 
                { "res": "Wiktionary_DE",     "color": "#98df8a", "value" : WktDE.length }, 
                { "res": "Wiktionary_EN",     "color": "#2ca02c", "value" : WktEN.length},
                { "res": "WordNet",           "color": "#17becf", "value" : WN.length }];

  	x.domain(data.map(function(d) { return d.res; }));
  	y.domain([0, d3.max(data, function(d) { return d.value; })]);
  	
  	svgSenses.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis)
	  .selectAll("text")
		.attr("y", 0)
		.attr("x", -14)
		.attr("dy", ".50em")
		.attr("transform", "rotate(-45)")
		.style("text-anchor", "end")
		.style("font-size","14px");
  	
  	svgSenses.append("g")
      .attr("class", "y axis")
      .attr("transform", "translate(" + 0 - margin.left - 10 + " ,0)")
      .call(yAxis)
    .append("text")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("x", 0 - (margin.left - 10))
      .attr("dy", ".71em")
      .style("text-anchor", "end")
      .style("font-size","14px")
      .text("Number of senses");

  	svgSenses.selectAll(".bar")
      .data(data)
    .enter().append("rect")
      .attr("class", "bar")
      .attr("x", function(d) { return x(d.res); })
      .attr("width", x.rangeBand())
      .attr("y", function(d) { return y(d.value); })
      .attr("height", function(d) { return height - y(d.value); })
      .style("fill",function(d){return d.color;})
      .style("opacity","0.7");
  	
  	var pieText = d3.select("#" + id).append("div")
    .attr("class", "pieText")
    .attr("width", 300)
    .attr("height", 120)
    .attr('transform', 'translate(0,0)');

  	pieText.append("text")
    .attr("x", "25") 
    .attr("y", "20")
    .style("font-size","16px")
    .text("Resource ratio of lemma " + "\""+lemma+"\"");
  	
  	drawpie();
  	
  	// Creates pie chart
    function drawpie(){
    	
    	var radius = 75;

        var color = d3.scale.ordinal()
        	.range(["#9467bd", "#ff9896", "#d62728", "#ffbb78", "#ff7f0e", "#bcbd22",
            		"#aec7e8","#1f77b4", "#98df8a", "#2ca02c", "#17becf"]);

        var arc = d3.svg.arc()
        	.outerRadius(radius - 10)
        	.innerRadius(0);
    	
    	var pie = d3.layout.pie()
        .sort(null)
        .value(function(d) { return d.value; });
        
         d3.select("#" + id).append("div")
         	.attr("class", "sensesPie")
         	.style("width", width + margin.left + margin.right + "px")
         	.style("height", height + margin.top + margin.bottom + "px");

         var svgSensePie = d3.select(".sensesPie").append("svg")
        	.attr("width", width + margin.left + margin.right)
        	.attr("height", height + margin.top + margin.bottom)
        	.append("g")
    		.attr("transform", "translate(" + width/2 + "," + height/2 + ")");

        data.forEach(function(d) {
        	d.value = +d.value;
        });

        var g = svgSensePie.selectAll(".arc")
         	 .data(pie(data))
            .enter().append("g")
             .attr("class", "arc");

        g.append("path")
          .attr("d", arc)
          .style("fill", function(d,i) { return color(i); })
          .style("opacity","0.7");

        g.append("text")
          .attr("transform", function(d) { return "translate(" + arc.centroid(d) + ")"; })
          .attr("dy", ".35em")
          .style("text-anchor", "middle")
          .text(function(d) { return d.res; });
    	
    }

}