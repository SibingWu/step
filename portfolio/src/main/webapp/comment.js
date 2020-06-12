/**
 * Gets the log in status from "/login".
 */
function getLoginStatus() {
    fetch("/login", {headers: {"Content-Type": "application/json"}}).
    then(response => response.json()).then((json) => {
        let commentSection = document.getElementById("content");
        // Hides the comment section.
        commentSection.style.display = "none";

        let isLoggedIn = json.isLoggedIn;
        let loggingUrl = json.loggingUrl;
        let user = json.user;

        if (isLoggedIn) {
            showMemberUI(user, loggingUrl, commentSection);
        } else {
            showGuestUI(user, loggingUrl);
        }
    });
}

/**
 * Shows the UI for logged in member.
 * @param {string} user User name.
 * @param {string} loggingUrl Log in or log out url.
 * @param {html element} commentSection HTML div element for comment section.
 */
function showMemberUI(user, loggingUrl, commentSection) {
    const logoutDiv = document.createElement("div");
    let id = document.createAttribute("id");
    id.value = "logout";
    logoutDiv.setAttributeNode(id);
    logoutDiv.innerHTML = getGreetingHTML(user, loggingUrl);

    commentSection.appendChild(logoutDiv);
    commentSection.style.display = "block";
}

/**
 * Shows the UI for non-logged in guest.
 * @param {string} user User name.
 * @param {string} loggingUrl Log in or log out url.
 */
function showGuestUI(user, loggingUrl) {
    const loginDiv = document.createElement("div");
    let id = document.createAttribute("id");
    id.value = "login";
    loginDiv.setAttributeNode(id);
    loginDiv.innerHTML = getGreetingHTML(user, loggingUrl);

    let body = document.getElementById("body");
    body.appendChild(loginDiv);
}

/**
 * Gets the HTML for logging page.
 * @param {string} user User name.
 * @param {string} loggingUrl Log in or log out url.
 * @return HTML content.
 */
function getGreetingHTML(user, loggingUrl) {
    let resultHTML = `<p>Hello ${user}.</p>\n`
                   + `<p>Login <a href=${loggingUrl}>here</a>.</p>`;

    return resultHTML;
}

/**
 * Fetches the response of "/comment".
 */
function loadAndShowComments() {
    let maxNumberOfComments = document.getElementById("quantity").value;
    if (maxNumberOfComments.length == 0) {
        maxNumberOfComments = 0;
    }
    let url = "/list-comment?quantity=" + maxNumberOfComments;
    fetch(url, {method: "GET"}).then(response => response.json()).then((json) => {
        const div = document.getElementById("comments");
        div.innerHTML = "";

        for (let i = 0; i < json.length; i++) {
            let comment = json[i];
            let commentString = getFormattedComment(comment);
            div.appendChild(createCommentElement(comment, commentString));
        }
    });
}

/**
 * Get a human readable string from a comment json.
 * @param {json} json Comment object in json form.
 * @return {string} A formatted string.
 */
function getFormattedComment(json) {
    let commenter = json.commenter;
    let content = json.content;
    let timestamp = json.timestamp;

    let timeString = getFormattedDate(timestamp);

    let resultString = `Commenter: ${commenter}\nTime: ${timeString}\nComment: ${content}`;

    return resultString;
}

/**
 * Converts timestamp to formatted human readable date string.
 * @param {long number} timestamp Timestamp.
 */
function getFormattedDate(timestamp) {
    let date = new Date(timestamp);

    let timeString = date.toLocaleString();

    return timeString;
}

/**
 * Creates an element that represents a comment, including its delete button.
 * @param {json} comment Comment object in json form.
 * @param {string} A formatted comment string.
 */
function createCommentElement(comment, commentString) {
    const commentElement = document.createElement("li");
    commentElement.className = "comment";

    const contentElement = document.createElement("span");
    contentElement.innerText = commentString;

    const deleteButtonElement = document.createElement('button');
    deleteButtonElement.className = "button";

    deleteButtonElement.innerText = "Delete this comment from database";
    deleteButtonElement.addEventListener("click", () => {
    deleteComment(comment);

    // Remove the comment from the DOM.
    commentElement.remove();
    });

    commentElement.appendChild(contentElement);
    commentElement.appendChild(deleteButtonElement);
    return commentElement;
}

/**
 * Tells the server to delete the comment.
 * @param {json} comment Comment object in json form.
 */
function deleteComment(comment) {
    const params = new URLSearchParams();
    params.append("id", comment.id);
    fetch('/delete-comment', {method: "POST", body: params});
}
