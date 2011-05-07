int SCREEN_WIDTH = window.innerWidth;
int SCREEN_HEIGHT = window.innerHeight * .99;
BufferedReader reader;
ArrayList conversations;
ArrayList nodes;
Graph g;
color backgroundColor = color(0, 0, 0);
MyProgressBar progressBar;
int p = 0;

void setup() {
  g = new Graph();
  size(SCREEN_WIDTH, SCREEN_HEIGHT);
  background(backgroundColor);
  progressBar = new MyProgressBar(1000);
}

function loadFriends () {
	var data = {
		'user_id': oApp.user_id
	},
	url = '/get_friends';
	$.getJSON(url, data, function (JSON) {
		p = 1900;
		$.each(JSON, function (i, ele) {
			var n1 = ele['node1'],
				n2 = ele['node2'],
				w = ele['weight'];
			g.addEdge(n1, n2, w);
		});
	});
	$.getJSON(url, {'user_id':1}, function (JSON) {
		p = 1900;
		$.each(JSON, function (i, ele) {
			var n1 = ele['node1'],
				n2 = ele['node2'],
				w = ele['weight'];
			g.addEdge(n1, n2, w);
		});
	});
} 

void draw() {
	if (p === 0) {           // fire ajax calls
		loadFriends();
		p = 1;
	} else if (p < 1000) {  // keep updating the progress bar
  	
		progressBar.updatePB(p);
		p = p + 1;
  } else if (p < 2000) {   // transition from progress to graph
		$('#header').show('slow');
		$('#stats_div').show('slow');
		p = 3000;
	} else {   // main method
		
	}
  g.draw();
}











    

