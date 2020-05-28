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

let old = -1;

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
                   "I don't like watching thrillers"];
    
    // Pick a random fact.
    let randomIndex = Math.floor(Math.random() * facts.length);
    while (randomIndex == old) {
        randomIndex = Math.floor(Math.random() * facts.length);
    }
    const fact = facts[randomIndex];
    old = randomIndex;

    // Add it to the page.
    const factContainter = document.getElementById("fact-container");
    factContainter.innerText = fact;
}