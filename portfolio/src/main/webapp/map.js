/** Creates a map and adds it to the page. */
function createMap() {
    const trexPosition = {lat: 37.422, lng: -122.084};
    const map = new google.maps.Map(
        document.getElementById("map"),
        {center: trexPosition, zoom: 16});

    const trexMarker = new google.maps.Marker({
      position: trexPosition,
      map: map,
      draggable: true,
      animation: google.maps.Animation.DROP,
      title: "Stan the T-Rex"
    });

    const trexInfoWindow = new google.maps.InfoWindow({content: 'This is Stan, the T-Rex statue.'});

    trexMarker.addListener("click", function() {
        toggleBounce(trexMarker);
        trexInfoWindow.open(map, trexMarker);
    });
}

/**
 * Toggles the animation of the marker on / off.
 * @param {marker} marker Target marker.
 */
function toggleBounce(marker) {
  if (marker.getAnimation() !== null) {
    marker.setAnimation(null);
  } else {
    marker.setAnimation(google.maps.Animation.BOUNCE);
  }
}
