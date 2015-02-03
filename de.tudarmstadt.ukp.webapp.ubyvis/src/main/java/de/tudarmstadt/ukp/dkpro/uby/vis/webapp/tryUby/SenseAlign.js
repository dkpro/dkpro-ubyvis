function draw(id, data, lemma, order, callbackUrl) {

	var margin = {
		top : 10,
		right : 50,
		bottom : 50,
		left : 50
	};
	var clickedNodes = 0, selectedList = [], cluster_array = [], singletons = [], noclusters = false;

	data_map = data;

	function compare(a, b) {
		if (a.senseId < b.senseId)
			return -1;
		if (a.senseId > b.senseId)
			return 1;
		return 0;
	}

	function sort_singletons(singletons) {

		var OW_eng = [], OW_deu = [], WikiDE = [], WikiEN = [], WktDE = [], WktEN = [], FN = [], VN = [], WN = [], OntoWktEN = [], OntoWktDE = [];

		var temp_total_array = [], total_array = [];

		singletons.forEach(function(sd) {

			if (sd.length > 0) {

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
		});

		temp_total_array.push(FN);
		temp_total_array.push(OW_deu);
		temp_total_array.push(OW_eng);
		temp_total_array.push(OntoWktDE);
		temp_total_array.push(OntoWktEN);
		temp_total_array.push(WikiDE);
		temp_total_array.push(WikiEN);
		temp_total_array.push(WktDE);
		temp_total_array.push(WktEN);
		temp_total_array.push(VN);
		temp_total_array.push(WN);

		temp_total_array.sort(function(a, b) {
			return b.length - a.length;
		});

		temp_total_array.forEach(function(arr) {

			arr.forEach(function(element) {
				total_array.push(element);
			});
		});

		return total_array;
	}

	// 
	function sort_cluster_nodes(cluster_array) {

		var sorted_clusters = [];

		for ( var ca = 0; ca < cluster_array.length; ca++) {

			temp_arr = cluster_array[ca];

			if (temp_arr.length > 3) {

				var OW_eng = [], OW_deu = [], WikiDE = [], WikiEN = [], WktDE = [], WktEN = [], FN = [], VN = [], WN = [], OntoWktEN = [], OntoWktDE = [];

				var temp_total_array = [], total_array = [];

				for ( var ta = 0; ta < temp_arr.length; ta++) {

					sd_temp = temp_arr[ta].senseId;
					var res = sd_temp.split("_");

					if (res[1] == "eng" || res[1] == "deu") {
						res_color = res[0] + "_" + res[1];
					} else {
						res_color = res[0];
					}

					switch (res_color) {

					case "OW_eng":
						OW_eng.push(temp_arr[ta]);
						break;
					case "OW_deu":
						OW_deu.push(temp_arr[ta]);
						break;
					case "WikiEN":
						WikiEN.push(temp_arr[ta]);
						break;
					case "WikiDE":
						WikiDE.push(temp_arr[ta]);
						break;
					case "WktEN":
						WktEN.push(temp_arr[ta]);
						break;
					case "WktDE":
						WktDE.push(temp_arr[ta]);
						break;
					case "FN":
						FN.push(temp_arr[ta]);
						break;
					case "VN":
						VN.push(temp_arr[ta]);
						break;
					case "WN":
						WN.push(temp_arr[ta]);
						break;
					case "OntoWktEN":
						OntoWktEN.push(temp_arr[ta]);
						break;
					case "OntoWktDE":
						OntoWktDE.push(temp_arr[ta]);
						break;
					}

				}

				temp_total_array.push(FN);
				temp_total_array.push(OW_deu);
				temp_total_array.push(OW_eng);
				temp_total_array.push(OntoWktDE);
				temp_total_array.push(OntoWktEN);
				temp_total_array.push(WikiDE);
				temp_total_array.push(WikiEN);
				temp_total_array.push(WktDE);
				temp_total_array.push(WktEN);
				temp_total_array.push(VN);
				temp_total_array.push(WN);

				temp_total_array.sort(function(a, b) {
					return b.length - a.length;
				});

				temp_total_array.forEach(function(arr) {

					arr.forEach(function(element) {

						total_array.push(element);

					});

				});

				sorted_clusters.push(total_array);

			} else {

				sorted_clusters.push(temp_arr);

			}

		}

		return sorted_clusters;

	}

	// Packs the grouped sense nodes
	drawCirclePacking(data_map);

	function drawCirclePacking(data_map) {

		clusters = data_map;
		clusters.forEach(function(cd) {
			if (cd.length > 1) {
				cluster_array.push(cd);
			} else {
				singletons.push(cd);
			}
		});

		if (cluster_array.length > 1) {

			cluster_array = sort_cluster_nodes(cluster_array);

			cluster_array.sort(function(a, b) {
				return b.length - a.length;
			});
		}

		singletons = sort_singletons(singletons);

		if (cluster_array.length > 1) {
			clusters = cluster_array.concat(singletons);
		} else {
			noclusters = true;
			clusters = singletons;
		}

		var data_set = [], diameter_arr = [], diameter, no_of_clusters = 0, format = d3
				.format(",d"), transform_matrix = [], maxDiameter = 200, i = 0, val = 30, j = 0;

		var pack = d3.layout.pack();

		/*
		 * var colorEnum = { "OW_eng": "#FF0066", "OW_deu": "#FF4D94", "WikiEN":
		 * "#3333CC", "WikiDE": "#6666FF", "WktEN": "#66FF33", "WktDE":
		 * "#00CC00", "FN": "#BD7DFF", "VN": "#FFB84D", "WN": "#FFFF66",
		 * "OntoWktEN" :"#228B22", "OntoWktDE" :"" };
		 */

		var colorEnum = {
			"FN" : "#9467bd",
			"OW_deu" : "#ff9896",
			"OW_eng" : "#d62728",
			"OntoWktDE" : "#ffbb78",
			"OntoWktEN" : "#ff7f0e",
			"VN" : "#bcbd22",
			"WikiDE" : "#aec7e8",
			"WikiEN" : "#1f77b4",
			"WktDE" : "#98df8a",
			"WktEN" : "#2ca02c",
			"WN" : "#17becf"
		};

		var temp_cluster_count = 0;

		clusters.forEach(function(entry) {

			if (entry.length == 1) {

				temp_align = entry[0].senseId;
				var res = temp_align.split("_");

				if (res[1] == "eng" || res[1] == "deu") {
					res_color = res[0] + "_" + res[1];
				} else {
					res_color = res[0];
				}

				entry[0].color = colorEnum[res_color];
				entry[0].res = res_color;

				data_set.push(entry[0]);

			} else {

				temp_cluster_count = temp_cluster_count + 1;
				var tempArray = entry;
				var tempObj = {};
				tempObj.name = "Sense Cluster";
				tempObj.children = [];

				diameter = (tempArray.length / 2) * 20 + 20;

				// console.log(diameter);

				if (tempArray.length == 1 || tempArray.length == 2
						|| tempArray.length == 3) {
					diameter = tempArray.length * 20 + 20;
				}

				if (tempArray.length == 4 || tempArray.length == 5) {
					diameter = tempArray.length * 20;
				}

				if (tempArray.length > 13) {
					diameter = (tempArray.length / 3) * 20 + 20;
				}

				if (diameter > maxDiameter) {
					diameter = maxDiameter;
				}

				diameter_arr.push(diameter);

				for ( var i = 0; i < tempArray.length; i++) {
					temp_align = tempArray[i].senseId;
					var res = temp_align.split("_");

					if (res[1] == "eng") {
						res_color = res[0] + "_" + res[1];
					} else {
						res_color = res[0];
					}

					tempArray[i].color = colorEnum[res_color];
					tempArray[i].res = res_color;
					tempObj.children.push(tempArray[i]);

				}
				tempObj.clusterNo = temp_cluster_count;
				data_set.push(tempObj);
			}
		});
		
		if (noclusters) {
			var j = 200;
		} else {
			var j = diameter_arr[0] + 220;
		}

		var k = 0;
		var l = 0;
		var cluster1 = true;
		var nextline = false;
		var cluster_count = 0;
		var yTrans = 200;

		var current_res_pos = [];

		data_set.forEach(function(entry) {

			if (entry.hasOwnProperty("children")) {

				if (cluster1) {
					val = 30;
					cluster1 = false;
					no_of_clusters++;
				} else {

					val = val + diameter_arr[i] + 20;

					i++;
				}

				if (val > 600) {

					yTrans = yTrans + diameter_arr[cluster_count] + 10;
					cluster_count = i;
					val = 40;

					nextline = true;
				}

				tempArray = [ val, yTrans ];
				

				transform_matrix.push(tempArray);

			} else {

				// order = 2;
				positionSingletons(order, entry);

			}
		});

		function positionSingletons(order, entry) {

			if (order == 1) {

				if (nextline) {
					j = yTrans + diameter_arr[cluster_count] + 10;
					nextline = false;
				}

				tempval = 50 + k;
				// console.log(" x : " + tempval + " y : " + j);
				tempArray = [ tempval, j ];
				k = k + 30;
				l++;
				transform_matrix.push(tempArray);

				if (l == 20) {
					l = 0;
					j = j + 30;
					k = 0;
				}
			} else if (order = 2) {

				var sameRes = false;

				if (nextline) {
					j = yTrans + diameter_arr[cluster_count] + 10;
					nextline = false;
				}

				jQuery.map(current_res_pos, function(val) {

					if (val.res == entry.res) {

						sameRes = true;

						k = k + 30;
						tempval = 30 + k;
						l++;

						if (l > 20) {
							k = 0;
							j = j + 30;
							tempval = 30 + k;
							l = 0;
						}

						val.pos = [ tempval, j ]

						// console.log("The same res " + entry.res + " is placed
						// at " + " x " + tempval + " y " + j);

						transform_matrix.push(val.pos);

					}
				});

				if (current_res_pos.length == 0) {

					sameRes = true;

					tempval = 30;

					res_pos = {};
					res_pos.res = entry.res;
					res_pos.pos = [ tempval, j ];

					current_res_pos.push(res_pos);

					transform_matrix.push(res_pos.pos);

				}

				if (!sameRes) {

					k = 0;
					j = j + 20;
					tempval = 30 + k;

					res_pos = {};
					res_pos.res = entry.res;
					res_pos.pos = [ tempval, j ]

					current_res_pos.push(res_pos);

					
					transform_matrix.push(res_pos.pos);
				}

			}

		}

		drawCircles(data_set, transform_matrix);

		function drawCircles(data_entry, transform_matrix) {

			widthNet = 600;
			heightNet = 600;

			var tempL = transform_matrix.length;
			var tempH = transform_matrix[tempL - 1];

			heightPackC = tempH[1];

			d3.select("#" + id).append("div").attr("class", "circlePack")
					.style("width",
							widthNet + margin.left + margin.right + "px")
					.style("height",
							heightPackC + margin.top + margin.bottom + "px");

			d3.select("#" + id).append("div").attr("class", "custom_menu");
			

			tooltip_div = d3.select("#" + id)
			                .append("div")
			                .attr("class","tooltip")
			                .style("opacity", 0);

			var col = [ 10, 110, 240, 370, 530 ], row = [ 50, 90, 130 ];

			legendPack = [ {
				"res" : "FrameNet",
				"color" : "#9467bd",
				"x" : col[0],
				"y" : row[0],
			}, {
				"res" : "OmegaWiki_DE",
				"color" : "#ff9896",
				"x" : col[1],
				"y" : row[0],
			}, {
				"res" : "OmegaWiki_EN",
				"color" : "#d62728",
				"x" : col[2],
				"y" : row[0],
			}, {
				"res" : "OntoWiktionary_DE",
				"color" : "#ffbb78",
				"x" : col[3],
				"y" : row[0],
			}, {
				"res" : "OntoWiktionary_EN",
				"color" : "#ff7f0e",
				"x" : col[4],
				"y" : row[0],
			}, {
				"res" : "VerbNet",
				"color" : "#bcbd22",
				"x" : col[0],
				"y" : row[1],
			}, {
				"res" : "Wikipedia_DE",
				"color" : "#aec7e8",
				"x" : col[1],
				"y" : row[1],
			}, {
				"res" : "Wikipedia_EN",
				"color" : "#1f77b4",
				"x" : col[2],
				"y" : row[1],
			}, {
				"res" : "Wiktionary_DE",
				"color" : "#98df8a",
				"x" : col[3],
				"y" : row[1],
			}, {
				"res" : "Wiktionary_EN",
				"color" : "#2ca02c",
				"x" : col[4],
				"y" : row[1],
			}, {
				"res" : "WordNet",
				"color" : "#17becf",
				"x" : col[0],
				"y" : row[2],
			} ];

			var legendText = d3.select(".circlePack")
			                  .append("svg")
			                   .attr("class", "legendText")
			                   .attr("width", 600)
			                   .attr("height",120)
			                   .attr('transform', 'translate(50,10)');

			legendText.append("text")
			          .attr("x", "300")
			          .attr("y", "15")
			          .style("font-size", "16px")
			          .text("Resources");

			var legend = d3.select(".circlePack")
			              .append("svg")
			               .attr("class","legend")
			               .attr("width", 680)
			               .attr("height", 180)
			               .attr('transform', 'translate(5,5)');

			legend.selectAll('circle').data(legendPack).enter()
					.append("circle").attr("class", "legendPack").attr("cx",
							function(d) {
								return d.x;
							}).attr("cy", function(d) {
						return d.y;
					}).attr("r", 10).style("fill", function(d) {
						return d.color;
					}).style("opacity", "1");

			legend.selectAll('text').data(legendPack).enter().append("text")
					.attr("x", function(d) {
						return (d.x + 15);
					}).attr("y", function(d, i) {
						return (d.y + 5);
					}).style("font-size", "14px").text(function(d) {
						return d.res;
					});

			var lemmaText = d3.select(".circlePack").append("svg").attr(
					"class", "lemma").attr("width", 680).attr("height", 50)
					.attr('transform', 'translate(50,10)');

			lemmaText.append("text").attr("x", "250").attr("y", "20").style(
					"font-size", "16px").text("Lemma : " + lemma);

			svgPack = d3.select(".circlePack").append("svg").attr("width",
					widthNet + margin.left + margin.right).attr("height",
					heightPackC + margin.top + margin.bottom);

			d3.select("#" + id).append("div")
			                   .style("position", "absolute")
					           .style("z-index", "10");

			var grp = svgPack.selectAll('g')
			                 .data(data_entry)
			                 .enter()
			                 .append('g')
			                 .attr('transform', function(d, i) {
			                	 temp = transform_matrix[i];
			                	 return 'translate(' + temp[0] + ',' + temp[1] + ')';
			                 })
			                 .attr("class", "pack");

			var node = grp.selectAll(".node").data(function(d) {
				return packNodes(d);
			}).enter().append("g").attr("class", function(d) {
				return d.children ? "node" : "leaf node";
			}).attr("transform", function(d) {
				return "translate(" + d.x + "," + d.y + ")";
			});

			node.append("circle").attr("class", "leaf").attr("r", function(d) {
				if (d.depth == 0) {
					return d.r;
				} else {
					return 10;
				}
			}).style("fill", function(d) {
				return d.color;
			});

			node.filter(function(d) {
				return !d.children;
			}).on(
					"mousemove",
					function(d) {
						tooltip_div.transition().duration(200).style("opacity",
								.9);
						tooltip_div.text(
								"Sense Definition: " + d.senseDefination)
								.style("left", (d3.event.pageX + 20) + "px")
								.style("top", (d3.event.pageY) + "px");
					}).on("mouseout", function(d) {
				tooltip_div.transition().duration(500).style("opacity", 0);
			});

			node.filter(function(d) {
				return d.children;
			}).on(
					"mousemove",
					function(d) {
						tooltip_div.transition().duration(200).style("opacity",
								.9);
						tooltip_div.text("Cluster " + d.clusterNo).style(
								"left", (d3.event.pageX + 20) + "px").style(
								"top", (d3.event.pageY) + "px");
					}).on("mouseout", function(d) {
				tooltip_div.transition().duration(200).style("opacity", 0);
			});

			node.filter(function(d) {
				return !d.children;
			}).on("click", function(d) {

				var selected = this.getElementsByTagName('circle');

				clickedNodes = clickedNodes + 1;

				if (selected[0].style.fill == "rgb(128, 128, 128)") {

					selected[0].style.fill = d.color;

				} else {

					selectedList.push(d);
					highlightNode(selected[0], node);

					$(function() {
						Wicket.Ajax.get({
							u : callbackUrl,
							ep : {
								'word' : lemma,
								'senseId' : d.senseId,
								'synsetDefinition' : d.synsetDefinition,
								'senseDefination' : d.senseDefination,
								'senseExample' : d.senseExample,
								'tab' : '2'
							}
						});

					});

				}

			});

			node
					.filter(function(d) {
						return !d.children;
					})
					.on(
							"contextmenu",
							function(d) {

								if (d3.event.pageX || d3.event.pageY) {
									var x = d3.event.pageX;
									var y = d3.event.pageY;
								} else if (d3.event.clientX || d3.event.clientY) {
									var x = d3.event.clientX
											+ document.body.scrollLeft
											+ document.documentElement.scrollLeft;
									var y = d3.event.clientY
											+ document.body.scrollTop
											+ document.documentElement.scrollTop;
								}

								if (clickedNodes == 2) {

									d3
											.select('.custom_menu')
											.style('position', 'absolute')
											.style('left', x + 'px')
											.style('top', (y - 50) + 'px')
											// .style('left', position[0] + 300
											// + "px")
											// .style('top', position[1] + 400 +
											// "px")
											.style('display', 'block')
											.text("Compare")
											.on(
													'mouseleave',
													function() {
														d3
																.select(
																		'.custom_menu')
																.style(
																		'display',
																		'none');
													})
											.on(
													'click',
													function() {

														var selectedListLen = selectedList.length;
														var first = selectedList[selectedListLen - 2];
														var second = selectedList[selectedListLen - 1];

														$(function() {

															Wicket.Ajax
																	.get({
																		u : callbackUrl,
																		ep : {
																			'firstSenseId' : first.senseId,
																			'firstSynsetDefinition' : first.synsetDefinition,
																			'firstSenseDefination' : first.senseDefination,
																			'firstSenseExample' : first.senseExample,
																			'secondSenseId' : second.senseId,
																			'secondSynsetDefinition' : second.synsetDefinition,
																			'secondSenseDefination' : second.senseDefination,
																			'secondSenseExample' : second.senseExample,
																			'tab' : '3'
																		}
																	});

														});

													});

								} else {

									d3
											.select('.custom_menu')
											.style('position', 'absolute')
											.style('left', x + 'px')
											.style('top', (y - 50) + 'px')
											// .style('left', position[0] + 300
											// + "px")
											// .style('top', position[1] + 400 +
											// "px")
											.style('display', 'block')
											.text("Select two nodes to compare")
											.on(
													'mouseleave',
													function() {
														d3
																.select(
																		'.custom_menu')
																.style(
																		'display',
																		'none');
													});

								}

								d3.event.preventDefault();
							});

		}

		// The selected node is colored grey
		function highlightNode(selected, node) {

			if (clickedNodes > 2) {

				clickedNodes = 1;

				var allnodes = node.filter(function(d) {
					return !d.children;
				});

				allnodes.select(".leaf").style("fill", function(d) {
					return d.color;
				});

			}

			selected.style.fill = "rgb(128, 128, 128)";

			console.log("total clicked nodes are " + clickedNodes);
		}

		// returns packed sense clusters
		function packNodes(nodeRoot) {

			if (nodeRoot.hasOwnProperty("children")) {

				diameter = (nodeRoot["children"].length / 2) * 20 + 20;

				if (nodeRoot["children"].length == 2
						|| nodeRoot["children"].length == 3) {
					diameter = nodeRoot["children"].length * 20 + 20;
				}

				if (nodeRoot["children"].length == 4
						|| nodeRoot["children"].length == 5) {
					diameter = nodeRoot["children"].length * 20;
				}

				if (nodeRoot["children"].length > 13) {
					diameter = (nodeRoot["children"].length / 3) * 20 + 20;
				}

				if (diameter > maxDiameter) {
					diameter = maxDiameter;
				}
			} else {
				diameter = 20;
			}

			pack.size([ diameter - 4, diameter - 4 ])
			    .padding(2)
			    .value(
					function(d) {
						return 20;
					});

			var temp = pack.nodes(nodeRoot);
			return temp;
		}

	}

}