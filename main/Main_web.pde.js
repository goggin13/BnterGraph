int SCREEN_WIDTH = round(window.innerWidth * .99);
int SCREEN_HEIGHT = round(window.innerHeight * .99);
Graph g;
color backgroundColor = color(0, 0, 0);
String activeNode = "";
int clickedX = 0;
int clickedY = 0;
int maxDisplayEdgeWeight = 7;
int maxEdgeWeight = 1;
boolean isLoading = false;
VizManager manager;
float CENTER_X = (SCREEN_WIDTH) / 2;
float CENTER_Y = (SCREEN_HEIGHT) / 2;
String bg_url = "http://lh5.ggpht.com/BJd07kKZyj7L7-Y0KH54Osqm8vRZJ7giQJIHqQBusPjfqtG-Ezjg5nPrcjksOfwRV9kAK0YT_3tGmAk";
PImage bg_img;

var yourUsername = oApp.user_name;
var stats = {
	'friends': 0,
	'conversations': 0,
	'strongest_edge': 0
};

void setup() {
	bg_url += "=s" + SCREEN_WIDTH + "-c";
  manager = new VizManager();
  bg_img = loadImage(bg_url, "png");
  size(SCREEN_WIDTH, SCREEN_HEIGHT);
	$('#header').show('slow');
	$('#stats_div').show('slow');
	updateFriends();
}

void mouseClicked() {
  clickedX = mouseX;
  clickedY = mouseY;
}

void loadMoreFriends(String name) {
	if (isLoading) {
		return;
	}
	isLoading = true;
	if (name.length > 0) {
		updateFriends(name);
	}
}

function showLoading (text) {
	$('#loading p').text(text + "... ");
	$('#loading').show();
}

function hideLoading () {
	$('#loading').hide();
	isLoading = false;
}

function updateFriends (username) {
	var data = {
		'user_name': username ? username : oApp.user_name
	};
	showLoading("loading friends for " + data.user_name);
	$.getJSON('/update_friends', data, function (JSON) {
		loadFriends(username);
	});
}

function loadFriends (username) {
	var data = {
		'user_name': username ? username : oApp.user_name
	};
	$.getJSON('/get_friends', data, function (JSON) {
		$.each(JSON, function (i, ele) {
			var n1 = ele['node1'],
				n2 = ele['node2'],
				w = ele['weight'];
			if (w > stats.strongest_edge) {
				stats.strongest_edge = w;
				maxEdgeWeight = w;
			}
			stats.friends++;
			stats.conversations += w;
			manager.addEdge(n1['name'], n1['image'], n2['name'], n2['image'], w);
		});
		for (key in stats) {
			$("#" + key).html(stats[key]);
		}
		hideLoading();
	});

} 

void draw() {
  manager.draw();
  clickedX = 0;
  clickedY = 0;
}


$('#stats_div').click(function () {
	var type = manager.getGraphType();
	if (type === 'solar') {
		manager.setGraphType('graph');
	} else {
		manager.setGraphType('solar');
	}
});












    

