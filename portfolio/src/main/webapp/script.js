// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

/**
 * Adds a random fact to the page.
 */
function addRandomFact() {
    const facts = ["I have no sibling", 
                   "I seldom eat spicy food", 
                   "I don't like watching thrillers",
                   "I love drinking milk tea"];

    // Pick a random fact.
    const factContainer = document.getElementById("fact-container");
    let currentFact = factContainer.innerText;
    let fact = getRandomFact(facts, currentFact);

    // Add it to the page.
    factContainer.innerText = fact;
} 

/**
 * Picks a fun fact to be displayed that is different from the current fun fact.
 * @param {string array} facts A list of facts.
 * @param {string} currentFact Current fun fact.
 * @return {string} Picked fun fact.
 */
function getRandomFact(facts, currentFact) {
    if (facts == null || facts.length <= 0) {
        return "No fact sorry:(";
    }

    let newFactIndex = 0;
    let currentIndex = facts.indexOf(currentFact);

    if (currentIndex == -1) {
        return facts[newFactIndex];
    }

    newFactIndex = (currentIndex + Math.floor(Math.random() * (facts.length - 1) + 1)) % facts.length;

    let fact = facts[newFactIndex];

    return fact;
}

/**
 * Goes back to the previous page.
 */
function goBack() {
    // If this is the first page.
    if(history.length === 1){
        window.location = "index.html"
    } else {
        history.back();
    }
}

/**
 * Fetches the response of "/data".
 */
function loadAndShowData() {
    fetch("/comment").then(response => response.json()).then((json) => {
        const div = document.getElementById("comments");
        div.innerHTML = "";

        for (let i = 0; i < json.length; i++) {
            let commentString = formatComment(json[i]);
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
 * Formats the comment json.
 * @param {json} json Comment object in json form.
 * @return {string} A formatted string.
 */
function formatComment(json) {
    let name = json.name;
    let comment = json.comment;

    let year = json.time.date.year;
    let month = json.time.date.month;
    let day = json.time.date.day;

    let hour = json.time.time.hour;
    let minute = json.time.time.minute;
    let second = json.time.time.second;

    let jsonString = `Name: ${name}\nTime: ${year}/${month}/${day} ${hour}:${minute}:${second}\nComment: ${comment}`;

    return jsonString;
}
