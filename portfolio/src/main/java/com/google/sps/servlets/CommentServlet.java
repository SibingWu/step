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
import com.google.appengine.repackaged.com.google.datastore.v1.Datastore;
import com.google.gson.Gson;
import com.google.sps.utils.CommentDataStore;
import com.google.sps.data.Comment;
import com.google.sps.utils.ServletUtils;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

/** Servlet that handles getting and posting comment content. */
@WebServlet("/comment")
public final class CommentServlet extends HttpServlet {

  private CommentDataStore commentDataStore;

  @Override
  public void init() {
    this.commentDataStore = new CommentDataStore(DatastoreServiceFactory.getDatastoreService());
  }

  static class Constants {
    private final static String PARAM_NAME_COMMENTER = "commenter";
    private final static String PARAM_NAME_CONTENT = "content";
  }
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Loads the comment from Datastore
    List<Comment> comments = getComments();

    // Converts into json form
    String json = convertToJsonUsingGson(comments);

    // Sends the JSON as the response
    sendJsonResponse(response, json);
  }

  /** Loads the comment from Datastore */
  private List<Comment> getComments() {
    // Loads comments from Datastore
    List<Comment> comments = this.commentDataStore.load();

    return comments;
  }


  /** Converts the list of Comment objecct into json form */
  private String convertToJsonUsingGson(List<Comment> comments) {
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    return json;
  }

  /** Sends the JSON as response */
  private void sendJsonResponse(HttpServletResponse response, String json) throws IOException {
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Gets comments from the form
    String commenter = ServletUtils.getParameter(
            request, /*name=*/Constants.PARAM_NAME_COMMENTER, /*defaultValue=*/"");
    String content = ServletUtils.getParameter(
            request, /*name=*/Constants.PARAM_NAME_CONTENT, /*defaultValue=*/"No comments");
    // TODO: validate request parameters

    // Stores the comment into the Datastore
    storeComment(commenter, content);

    // Redirects back to the HTML page.
    response.sendRedirect("/index.html");
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
