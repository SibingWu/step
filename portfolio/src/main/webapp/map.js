/** Creates a map and adds it to the page. */
function createMap() {
    const map = new google.maps.Map(
        document.getElementById('map'),
        {center: {lat: 37.422, lng: -122.084}, zoom: 16});

    const trexMarker = new google.maps.Marker({
      position: {lat: 37.421903, lng: -122.084674},
      map: map,
      draggable: true,
      animation: google.maps.Animation.DROP,
      title: "Stan the T-Rex"
    });

    trexMarker.addListener("click", function() {
        toggleBounce(trexMarker);
    });

    const trexInfoWindow = new google.maps.InfoWindow({content: 'This is Stan, the T-Rex statue.'});
    trexInfoWindow.open(map, trexMarker);
}

/**
 * Generates the bouncing animation for marker.
 * @param {marker} marker Target marker.
 */
function toggleBounce(marker) {
  if (marker.getAnimation() !== null) {
    marker.setAnimation(null);
  } else {
    marker.setAnimation(google.maps.Animation.BOUNCE);
  }
}
