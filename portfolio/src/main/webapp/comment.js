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
 * @param {string} loggingUrl Log out url.
 * @param {html element} commentSection HTML div element for comment section.
 */
function showMemberUI(user, loggingUrl, commentSection) {
    let htmlText = getGreetingHTML(/* isLoggedIn= */true, user, loggingUrl);
    let logoutDiv = createLoggingRelatedSection("logout", htmlText);

    commentSection.appendChild(logoutDiv);
    commentSection.style.display = "block";
}

/**
 * Shows the UI for non-logged in guest.
 * @param {string} user User name.
 * @param {string} loggingUrl Log in url.
 */
function showGuestUI(user, loggingUrl) {
    let htmlText = getGreetingHTML(/* isLoggedIn= */false, user, loggingUrl);
    let loginDiv = createLoggingRelatedSection("login", htmlText);

    let body = document.getElementById("body");
    body.appendChild(loginDiv);
}

/**
 * Gets the HTML for logging page.
 * @param {boolean} isLoggedIn If the user is logged in or not.
 * @param {string} user User name.
 * @param {string} loggingUrl Log in or log out url.
 * @return HTML content.
 */
function getGreetingHTML(isLoggedIn, user, loggingUrl) {
    let logging = isLoggedIn ? "Logout" : "Login";
    let resultHTML = `<p>Hello ${user}.</p>\n`
                   + `<p>${logging} <a href=${loggingUrl}>here</a>.</p>`;

    return resultHTML;
}

/**
 * Creates logging section.
 * @param {string} name Element id name.
 * @param {string} htmlText HTML content for the target element.
 * @return HTML element
 */
function createLoggingRelatedSection(name, htmlText) {
    const div = document.createElement("div");
    let id = document.createAttribute("id");
    id.value = name;
    div.setAttributeNode(id);
    div.innerHTML = htmlText;

    return div;
}

/**
 * Fetches the response of "/comment".
 */
function loadAndShowComments() {
    let maxNumberOfComments = document.getElementById("quantity").value;
    if (maxNumberOfComments.length == 0) {
        maxNumberOfComments = "0";
    }
    let url = "/list-comment?quantity=" + maxNumberOfComments;
    fetch(url, {method: "GET"}).then(response => response.json()).then((commentsJson) => {
        showComments(commentsJson);
    });
}

/**
 * Shows comments on the page.
 * @param {json} commentsJson A list of comments in json format.
 */
function showComments(commentsJson) {
    const div = document.getElementById("comments");
    div.innerHTML = "";

    for (let i = 0; i < commentsJson.length; i++) {
        let commentJson = commentsJson[i];
        div.appendChild(createCommentElement(commentJson));
    }
}

/**
 * Creates an element that represents a comment, including its delete button.
 * @param {json} comment Comment object in json form.
 * @return A HTML element.
 */
function createCommentElement(comment) {
  const commentElement = document.createElement("li");
  commentElement.className = "comment";

  const contentElement = document.createElement("span");
  contentElement.innerText = getFormattedComment(comment);

  const deleteButtonElement = document.createElement('button');
  deleteButtonElement.className = "button";

  deleteButtonElement.innerText = "Delete this comment";
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
 * Get a human readable string from a comment json.
 * @param {json} json Comment object in json form.
 * @return {string} A formatted string.
 */
function getFormattedComment(json) {
    let commenter = json.commenter;
    let email = json.email;
    let content = json.content;
    let timestamp = json.timestamp;

    let timeString = getFormattedDate(timestamp);

    let resultString = `Commenter: ${commenter}\n`
                     + `Email: ${email}\n`
                     + `Time: ${timeString}\n`
                     + `Comment: ${content}`;

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
 * Tells the server to delete the comment.
 * @param {json} comment Comment object in json form.
 */
function deleteComment(comment) {
    const params = new URLSearchParams();
    params.append("id", comment.id);
    fetch("/delete-comment", {method: "POST", body: params});
}
