var itemGroup;

function loadImage(src) {
	var imageObj = new Image();
	imageObj.onload = function() {
		AddImageToLayer(imageObj);
	};
	imageObj.src = src;
}
function AddImageToLayer(image) {
	itemGroup = new Kinetic.Group({
		x: 100,
		y: 100,
		draggable: true,
		offset:10,
		name: 'imgGroup'
	});

	var itemImage = new Kinetic.Image({
		x: 0,
		y: 0,
		image: image,
		width: 280,
		height: 180,
		name: 'image'
	});
	
	
	layer.add(itemGroup);
	itemGroup.add(itemImage);
	
	addAnchor(itemGroup, 0, 0, 'topLeft');
	addAnchor(itemGroup, 270, 0, 'topRight');
	addAnchor(itemGroup, 270, 180, 'bottomRight');
	addAnchor(itemGroup, 0, 180, 'bottomLeft');
	/*	
		
		itemGroup.get('.topLeft').toArray()[0].setVisible(false);
		itemGroup.get('.topRight').toArray()[0].setVisible(false);
		itemGroup.get('.bottomRight').toArray()[0].setVisible(false);
		itemGroup.get('.bottomLeft').toArray()[0].setVisible(false);
		
		
	
	
	itemGroup.on("mouseover", function(){	
		itemGroup.on("mouseover", function(){
			clearTimeout($(this).data('timeoutId'));
			this.get('.topLeft').toArray()[0].setVisible(true);
			this.get('.topRight').toArray()[0].setVisible(true);
			this.get('.bottomRight').toArray()[0].setVisible(true);
			this.get('.bottomLeft').toArray()[0].setVisible(true);
		});
	});
	
	itemGroup.on("mouseout", function(){
		itemGroup.on("mouseout", function(){
		var thisGroup = this;
		var timeoutId = setTimeout( function() {
			thisGroup.get('.topLeft').toArray()[0].setVisible(false);
			thisGroup.get('.topRight').toArray()[0].setVisible(false);
			thisGroup.get('.bottomRight').toArray()[0].setVisible(false);
			thisGroup.get('.bottomLeft').toArray()[0].setVisible(false);
		}, 500);
		$(itemGroup).data('timeoutId', timeoutId);
		});
	});
	*/
	
	itemImage.moveToTop();

	
	
	itemGroup.on('dragstart', function() {
		this.moveToTop();
	});
	stage.draw();
	
	
		
}




function updateActivePos(activeAnchor) {
	var group = activeAnchor.getParent();

	var topLeft = group.get('.topLeft')[0];
	var topRight = group.get('.topRight')[0];
	var bottomRight = group.get('.bottomRight')[0];
	var bottomLeft = group.get('.bottomLeft')[0];
	var image = group.get('.image')[0];

	var anchorX = activeAnchor.getX();
	var anchorY = activeAnchor.getY();

	// update anchor positions
	switch (activeAnchor.getName()) {
	case 'topLeft':
		topRight.setY(anchorY);
		bottomLeft.setX(anchorX);
		break;
	case 'topRight':
		topLeft.setY(anchorY);
		bottomRight.setX(anchorX);
		break;
	case 'bottomRight':
		bottomLeft.setY(anchorY);
		topRight.setX(anchorX); 
		break;
	case 'bottomLeft':
		bottomRight.setY(anchorY);
		topLeft.setX(anchorX); 
		break;
	}

	image.setPosition(topLeft.getPosition());

	var width = topRight.getX() - topLeft.getX();
	var height = bottomLeft.getY() - topLeft.getY();
	if(width && height) {
		image.setSize(width, height);
	}
}

function addAnchor(group, x, y, name) {
	var stage = group.getStage();
	var layer = group.getLayer();

	var anchor = new Kinetic.Circle({
		x: x,
		y: y,
		stroke: '#666',
		fill: '#ddd',
		strokeWidth: 2,
		radius: 8,
		name: name,
		draggable: true,
		dragOnTop: false
	});

	anchor.on('dragmove', function() {
		updateActivePos(this);
		layer.draw();
	});
	anchor.on('mousedown touchstart', function() {
		group.setDraggable(false);
		this.moveToTop();
	});
	anchor.on('dragend', function() {
		group.setDraggable(true);
		layer.draw();
	});
	// add hover styling
	

	group.add(anchor);
}

function removeAnchor(group) {
	var stage = group.getStage();
	var layer = group.getLayer();

	
	}
