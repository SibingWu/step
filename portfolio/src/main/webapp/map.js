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

    trexMarker.addListener("click", function() {
        toggleBounce(trexMarker);
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
