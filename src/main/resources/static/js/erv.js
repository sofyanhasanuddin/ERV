var tokenParam, tokenValue;

var finishFirstTimeRender = false;

var margin = {
	    top: 50,
	    bottom: 50,
	    left: 50,
	    right: 50,
	}

// dimensions
var width = window.innerWidth - margin.left - margin.right;
var height = 1500 - margin.top - margin.bottom;

var radius = 200;

var link, 
    node, 
    edgelabels, 
    edgepaths,
    linkedByIndex = {};

const R = 18;

var svg;
var simulation;

function initSVG() {
	
	d3.select("svg").remove();
	
	// create an svg to draw in
    svg = d3.select("body")
    	.select("#drawArea")
        .append("svg")
        .attr("width", window.innerWidth)
        .attr("height", 2000)
        .append('g')
        .attr('transform', 'translate(' + margin.top + ',' + margin.left + ')');

    
    
//    svg.append('defs').append('marker')
//        .attrs({'id':'arrowhead',
//            'viewBox':'-0 -5 10 10',
//            'refX':13,
//            'refY':0,
//            'orient':'auto',
//            'markerWidth':13,
//            'markerHeight':13,
//            'xoverflow':'visible'})
//        .append('svg:path')
//        .attr('d', 'M 0,-5 L 10 ,0 L 0,5')
//        .attr('fill', '#999')
//        .style('stroke','none');
    
    // add defs-marker
    // add defs-markers
    svg.append('svg:defs').selectAll('marker')
      .data([{ id: 'end-arrow', opacity: 1 }, { id: 'end-arrow-fade', opacity: 0 }])
      .enter().append('marker')
        .attr('id', d => d.id)
        .attr('viewBox', '-0 -5 10 10')
        .attr('refX', 13)
        .attr('refY', 0)
        .attr('markerWidth', 13)
        .attr('markerHeight', 13)
        .attr('orient', 'auto')
        .append('svg:path')
          .attr('d', 'M 0,-5 L 10 ,0 L 0,5')
          .style('opacity', d => d.opacity);
	
}

function initForce() {
	
	simulation = null;
	
	simulation = d3.forceSimulation()
    // pull nodes together based on the links between them
    .force("link", d3.forceLink().id(function(d) {
        return d.id;
    }).distance(100)
    .strength(0.025))
    // push nodes apart to space them out
    .force("charge", d3.forceManyBody().strength(-80))
    // add some collision detection so they don't overlap
    .force("collide", d3.forceCollide().radius(12))
    // and draw them around the centre of the space
    .force("center", d3.forceCenter(width / 2, height / 2))
    .alphaTarget(0)
    .alphaDecay(0.05)
    .on('end', function() {
        d3.selectAll(".link").each(
            function(d) {
                d.fx = d.x;
                d.fy = d.y;
            }
        );
        d3.selectAll(".node").each(
            function(d) {
                d.fx = d.x;
                d.fy = d.y;
            }
        );
        finishFirstTimeRender = true;
    });
	
}

function update( links, nodes ) {
    
    // add the curved links to our graphic
    link = svg.selectAll(".link")
        .data(links)
        .enter()
        .append("path")
        .attr("class", "link")
        .attr('stroke', function(d){
            return "#ddd";
        })
        //.attr('marker-end','url(#arrowhead)');
        .attr('marker-end', 'url(#end-arrow)');

    edgepaths = svg.selectAll(".edgepath")
        .data(links)
        .enter()
        .append('path')
        .attrs({
            'class': 'edgepath',
            'fill-opacity': 0,
            'stroke-opacity': 0,
            'id': function (d, i) {return 'edgepath' + i}
        })
        .style("pointer-events", "none");

    edgelabels = svg.selectAll(".edgelabel")
        .data(links)
        .enter()
        .append('text')
        .style("pointer-events", "none")
        .attrs({
            'class': 'edgelabel',
            'id': function (d, i) {return 'edgelabel' + i},
            'font-size': 10,
            'fill': '#aaa'
        });

    edgelabels.append('textPath')
        .attr('xlink:href', function (d, i) {return '#edgepath' + i})
        .style("text-anchor", "middle")
        .style("pointer-events", "none")
        .attr("startOffset", "50%")
        .text(function (d) {return d.fieldRelation});

    // add the nodes to the graphic
    node = svg.selectAll(".node")
        .data(nodes)
        .enter().append("g")
        .call(d3.drag()
            .on("start", dragstarted)
            .on("drag", dragged)
            // .on("end", dragended)
        );

    // a circle to represent the node
    node.append("circle")
        .attr("class", "node")
        .attr("r", 8)
        .attr("fill", function(d) {
            // return d.colour;
            return "#008142";
        })
        .on('mouseover', mouseOver(0))
        .on('mouseout', mouseOver(1));

    // hover text for the node
    node.append("title")
        .text(function(d) {
            return d.label;
        });

    // add a label to each node
    node.append("text")
        .attr("dx", 12)
        .attr("dy", ".35em")
        .text(function(d) {
            return d.name;
        })
        .style("stroke", "black")
        .style("stroke-width", 0.5)
        .style("fill", function(d) {
            // return d.colour;
            return "#008142";
        });

    // add the nodes to the simulation and
    // tell it what to do on each tick
    simulation
        .nodes(nodes)
        .on("tick", ticked);

    // add the links to the simulation
    simulation
        .force("link")
        .links(links);

    // build a dictionary of nodes that are linked
    linkedByIndex = {};
    links.forEach(function(d) {
        linkedByIndex[d.source.index + "," + d.target.index] = 1;
    });

}

// check the dictionary to see if nodes are linked
function isConnected(a, b) {
    return linkedByIndex[a.index + "," + b.index] || linkedByIndex[b.index + "," + a.index] || a.index == b.index;
}

//check the dictionary to see if nodes are linked
function isLinkConnected(a, b) {
	return a.id == b.source.id || a.id == b.target.id;
    //return linkedByIndex[a.source.index + "," + b.source.index] || linkedByIndex[b.source.index + "," + a.source.index];
}

// fade nodes on hover
function mouseOver(opacity) {
    return function(d) {
        // check all other nodes to see if they're connected
        // to this one. if so, keep the opacity at 1, otherwise
        // fade
        node.style("stroke-opacity", function(o) {
            thisOpacity = isConnected(d, o) ? 1 : opacity;
            return thisOpacity;
        });
        node.style("fill-opacity", function(o) {
            thisOpacity = isConnected(d, o) ? 1 : opacity;
            return thisOpacity;
        });
        // also style link accordingly
        link.style("stroke-opacity", function(o) {
            return o.source === d || o.target === d ? 1 : opacity;
        });
        link.style("stroke", function(o){
            return o.source === d || o.target === d ? o.source.colour : "#ddd";
        });
        link.attr('marker-end', o => (opacity === 1 || o.source === d || o.target === d ? 'url(#end-arrow)' : 'url(#end-arrow-fade)'));
        edgelabels.style("opacity", function(o) {
            thisOpacity = isLinkConnected(d, o) ? 1 : opacity;
            return thisOpacity;
        });
    };
}

function mouseOut() {
    node.style("stroke-opacity", 1);
    node.style("fill-opacity", 1);
    link.style("stroke-opacity", 1);
    link.style("stroke", "#ddd");
    link.attr('marker-end', o => (opacity === 1 || o.source === d || o.target === d ? 'url(#end-arrow)' : 'url(#end-arrow-fade)'));
}

// on each tick, update node and link positions
function ticked() {
    
    link.attr("d", positionLink);
    node.attr("transform", positionNode);

    edgepaths.attr('d', function (d) {
        return 'M ' + d.source.x + ' ' + d.source.y + ' L ' + d.target.x + ' ' + d.target.y;
    });

    edgelabels.attr('transform', function (d) {
        if (d.target.x < d.source.x) {
            var bbox = this.getBBox();

            rx = bbox.x + bbox.width / 2;
            ry = bbox.y + bbox.height / 2;
            return 'rotate(180 ' + rx + ' ' + ry + ')';
        }
        else {
            return 'rotate(0)';
        }
    });

}

// links are drawn as curved paths between nodes,
// through the intermediate nodes
function positionLink(d) {

    var offset = 0;

    var midpoint_x = (d.source.x + d.target.x) / 2;
    var midpoint_y = (d.source.y + d.target.y) / 2;

    var dx = (d.target.x - d.source.x);
    var dy = (d.target.y - d.source.y);

    var normalise = Math.sqrt((dx * dx) + (dy * dy));

    var offSetX = midpoint_x + offset*(dy/normalise);
    var offSetY = midpoint_y - offset*(dx/normalise);

    return "M" + d.source.x + "," + d.source.y +
        "S" + offSetX + "," + offSetY +
        " " + d.target.x + "," + d.target.y;
}

// move the node based on forces calculations
function positionNode(d) {
    // keep the node within the boundaries of the svg
    if (d.x < 0) {
        d.x = 0
    };
    if (d.y < 0) {
        d.y = 0
    };
    if (d.x > width) {
        d.x = width
    };
    if (d.y > height) {
        d.y = height
    };
    return "translate(" + d.x + "," + d.y + ")";
}

function dragstarted(d) {
    if( !finishFirstTimeRender ) return;
    if (!d3.event.active) simulation.alphaTarget(0.3).restart();
    d.fx = d.x;
    d.fy = d.y;
}

function dragged(d) {
    if( !finishFirstTimeRender ) return;
    d.fx = d3.event.x;
    d.fy = d3.event.y;
}

function dragended(d) {
    
}

$('#uploadAndScan').on('click', function() {
	
	finishFirstTimeRender = false;
	
	initSVG();
    initForce();

    //Clear existing if any
    svg.selectAll(".node,.link,.edgelabel,text,.edgelabels,edgepaths").remove();

    //Form file	
    var fd = new FormData();
    fd.append( 'file', $( '#file' ).prop('files')[0] );
    fd.append( 'pkg', $( '#pkg' ).val() );

    //The token
    getToken();

    $.ajax({
        url: 'doUpload?' + tokenParam +"=" +tokenValue,
        type: 'POST',
        data: fd,
        cache       : false,
        contentType : false,
        processData : false,
        async: false,
        success:function(resp){
        	update( resp.links, resp.nodes );
        },
        cache: false,
        contentType: false,
        processData: false
    });
});

function getToken() {
    $.ajax({
        url: 'generateCSRF',
        type: 'GET',
        async: false,
        success:function(data){
            tokenParam = data["X-CSRF-PARAM"];
            tokenValue = data["X-CSRF-TOKEN"];
        }
    });
}