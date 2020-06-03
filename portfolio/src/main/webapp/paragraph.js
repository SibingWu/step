/**
 * Adds a heading with a paragraph to each picture.
 */
function addDescription(directory) {
    let imagePathDescriptionMap = getImageDescriptionMap(directory);

    generateDiv("content");

    Object.entries(imagePathDescriptionMap).forEach(
        ([image, description], index) => generateHTMLLayout(image, description, index + 1, "description")
    );
}

/**
 * Gets the images to be displayed.
 * @param {string} directory  directory to the images.
 * @return {array of string array} a map from image path to image description
 */
function getImageDescriptionMap(directory) {
    let mapping = {
        "images/sibing-1.jpg": "This picture was taken accidentally by my friend two years ago. However it becomes one of my favorite photo since at that time I was still quite slim.",
        "images/swag.jpg": "Me & Welcome Packge with Google!",
        "images/welcomePackage.jpg": "Welcome package from Google!",
        "images/sibing-2.jpg": "My favorite ID photo, since I was truly slim at that time."
    }

    return mapping;
}

/**
 * Creates a "div" that contain images and descriptions.
 * @param {string} elementId id of the element where the "div" places.
 */
function generateDiv(elementId) {
    let div = document.createElement("div");
    let id = document.createAttribute("id");
    id.value = "description";
    div.setAttributeNode(id);

    let element = document.getElementById(elementId);
    element.appendChild(div);
}

/**
 * Generates the layout in the html.
 * @param {string} image path to the image.
 * @param {string} description description to the image.
 * @param {number} paragraphId paragraph id.
 * @param {string} elementId id of the element where the paragraphs place.
 */
function generateHTMLLayout(image, description, paragraphId, elementId) {
    let tag = document.createElement("a");
    let source = document.createAttribute("href");
    source.value = image; 
    tag.setAttributeNode(source);
    tag.innerHTML = "<h3>P" + paragraphId + "</h3>";

    let descriptionText = document.createElement("p");
    let text = document.createTextNode(description);
    descriptionText.appendChild(text);

    let element = document.getElementById(elementId);
    element.appendChild(tag);
    element.appendChild(descriptionText);
}