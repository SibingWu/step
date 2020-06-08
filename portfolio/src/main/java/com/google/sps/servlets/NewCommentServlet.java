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

package com.google.sps.servlets;

import com.google.appengine.api.datastore.*;
import com.google.sps.data.Comment;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.google.sps.utils.Constants.COMMENT_COMMENTER;
import static com.google.sps.utils.Constants.COMMENT_CONTENT;
import static com.google.sps.utils.Constants.COMMENT_KIND;

/** Servlet that handles getting comment content. */
@WebServlet("/comment")
public final class NewCommentServlet extends CommentServlet {
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Gets comments from the form
    String commenter = getParameter(request, /*name=*/COMMENT_COMMENTER, /*defaultValue=*/"");
    String content = getParameter(request, /*name=*/COMMENT_CONTENT, /*defaultValue=*/"No comments");
    // TODO: validate request parameters

    // Stores the comment into the Datastore
    storeComment(commenter, content);

    // Redirects back to the HTML page.
    response.sendRedirect("/comments.html");
  }

  /** Stores the comment into the Datastore */
  private void storeComment(String commenter, String content) {
    // create a miscellaneous comment object to convert it to entity
    long timestamp = System.currentTimeMillis();
    Comment comment = new Comment(0, commenter, content, timestamp);

    // put the entity into Datastore
    DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
    datastoreService.put(comment.toEntity(COMMENT_KIND));
  }
}
