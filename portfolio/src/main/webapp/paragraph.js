/**
 * Adds a headig with a paragraph to each picture.
 */
let files = getImages("/images");
let descriptions = ["This picture was taken accidentally by my friend two years ago. However it becomes one of my favorite photo since at that time I was still quite slim.",
                    "Me & Welcome Packge with Google!",
                    "Welcome package from Google!",
                    "My favorite ID photo, since I was truly slim at that time."];
let size = files.length;
for (let i = 0; i < size; i++) {
    let tag = document.createElement("a");
    let source = document.createAttribute("href");
    source.value = files[i]; 
    tag.setAttributeNode(source);
    tag.innerHTML = "<h3>P" + (i + 1) + "</h3>";

    let description = document.createElement("p");
    let text = document.createTextNode(descriptions[i]);
    description.appendChild(text);

    let element = document.getElementById("content");
    element.appendChild(tag);
    element.appendChild(description);
}

/**
 * Gets the images to be displayed.
 * @param {string} directory  directory to the images.
 * @return {string array} an array of file name string.
 */
function getImages(directory) {
    let files = ["images/sibing-1.jpg",
                 "images/swag.jpg",
                 "images/welcomePackage.jpg",
                 "images/sibing-2.jpg"];
    return files;
}