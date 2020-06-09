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
import com.google.sps.utils.CommentDataStore;
import com.google.sps.data.Comment;
import com.google.sps.utils.ServletUtils;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles posting new comment. */
@WebServlet("/comment")
public final class NewCommentServlet extends HttpServlet {

  private CommentDataStore commentDataStore;

  @Override
  public void init() {
    this.commentDataStore = new CommentDataStore(DatastoreServiceFactory.getDatastoreService());
  }

  private final static String PARAM_NAME_COMMENTER = "commenter";
  private final static String PARAM_NAME_CONTENT = "content";

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Gets comments from the form
    String commenter = ServletUtils.getParameter(
            request, PARAM_NAME_COMMENTER, /*defaultValue=*/"");
    String content = ServletUtils.getParameter(
            request, PARAM_NAME_CONTENT, /*defaultValue=*/"No comments");
    // TODO: validate request parameters
    // TODO: why keep submitting No comments

    // Stores the comment into the Datastore
    storeComment(commenter, content);

    // Redirects back to the HTML page.
    response.sendRedirect("/comments.html");
  }

  /** Stores the comment into the Datastore */
  private void storeComment(String commenter, String content) {
    // Creates a miscellaneous comment object to convert it to entity
    long timestamp = System.currentTimeMillis();
    Comment comment = new Comment(commenter, content, timestamp);

    // Stores the comment as an entity into Datastore
    this.commentDataStore.store(comment);
  }
}
