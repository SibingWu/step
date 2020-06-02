/**
 * Adds a heading with a paragraph to each picture.
 */
function addDescription(directory) {
    let imagesAndMapping = getImagesnMapping(directory);
    let files = imagesAndMapping[0];
    let mapping = imagesAndMapping[1];
    let size = files.length;

    generateDiv("content");

    for (let i = 0; i < size; i++) {
        generateHTMLLayout(files[i], mapping, i, "description");
    }
}

/**
 * Gets the images to be displayed.
 * @param {string} directory  directory to the images.
 * @return {array of string array} an array containing image paths and image-description mapping
 */
function getImagesnMapping(directory) {
    let fileNames = ["sibing-1.jpg",
                     "swag.jpg",
                     "welcomePackage.jpg",
                     "sibing-2.jpg"];

    let descriptions = ["This picture was taken accidentally by my friend two years ago. However it becomes one of my favorite photo since at that time I was still quite slim.",
                        "Me & Welcome Packge with Google!",
                        "Welcome package from Google!",
                        "My favorite ID photo, since I was truly slim at that time."];
    
    let files = [];
    let mapping = {};
    for (let i = 0; i < fileNames.length; i++) {
        let filePath = directory + "/" + fileNames[i];
        files.push(filePath);
        mapping[filePath] = descriptions[i];
    }

    let result = [files, mapping];

    return result;
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
 * @param {dictionary} mapping images and description mapping.
 * @param {number} i index in the file string array.
 * @param {string} elementId id of the element where the paragraphs place.
 */
function generateHTMLLayout(image, mapping, i, elementId) {
    let tag = document.createElement("a");
    let source = document.createAttribute("href");
    source.value = image; 
    tag.setAttributeNode(source);
    tag.innerHTML = "<h3>P" + (i + 1) + "</h3>";

    let description = document.createElement("p");
    let text = document.createTextNode(mapping[image]);
    description.appendChild(text);

    let element = document.getElementById(elementId);
    element.appendChild(tag);
    element.appendChild(description);
}