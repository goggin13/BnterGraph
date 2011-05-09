int SCREEN_WIDTH = window.innerWidth * .99;
int SCREEN_HEIGHT = window.innerHeight * .99;
Graph g;
color backgroundColor = color(0, 0, 0);
String activeNode = "";
int clickedX = 0;
int clickedY = 0;
boolean isLoading = false;

var stats = {
	'friends': 0,
	'conversations': 0,
	'strongest_edge': 0
};

void setup() {
  g = new Graph();
  size(SCREEN_WIDTH, SCREEN_HEIGHT);
  background(backgroundColor);
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
			}
			stats.friends++;
			stats.conversations += w;
			g.addEdge(n1['name'], n1['image'], n2['name'], n2['image'], w);
		});
		for (key in stats) {
			$("#" + key).html(stats[key]);
		}
		hideLoading();
	});

} 

void draw() {
  g.draw();
  clickedX = 0;
  clickedY = 0;
}











    

