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
            let commentString = getFormattedComment(json[i]);
            div.appendChild(createListElement(commentString));
        }
    });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
    const liElement = document.createElement('li');
    liElement.innerText = text;
    return liElement;
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

/** Tells the server to delete the task. */
function deleteTask(task) {
  const params = new URLSearchParams();
  params.append('id', task.id);
  fetch('/delete-task', {method: 'POST', body: params});
}