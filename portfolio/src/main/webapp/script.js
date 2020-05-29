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
 * Picks a fun fact to be displayed.
 * @param {string array} facts A list of facts.
 * @param {string} currentFact Current fun fact.
 * @return {string} Picked fun fact.
 */
function getRandomFact(facts, currentFact) {
    let factIndex = Math.floor(Math.random() * facts.length)
    let fact = facts[factIndex];
    while (fact == currentFact) {
        factIndex = (factIndex + Math.floor(Math.random() * facts.length)) % facts.length;
        fact = facts[factIndex];
    }
    return fact;
}