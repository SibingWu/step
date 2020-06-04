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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import com.google.sps.data.Comment;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Servlet that handles getting and posting comment content. */
@WebServlet("/comment")
public final class CommentServlet extends HttpServlet {
  private final List<Comment> comments = new ArrayList<>();
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
     String json = convertToJsonUsingGson(this.comments);

    // Send the JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  private String convertToJsonUsingGson(List<Comment> comments) {
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    return json;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get comments from the form
    String name = getParameter(request, /*name=*/"name", /*defaultValue=*/"");
    String comment = getParameter(request, /*name=*/"comment", /*defaultValue=*/"No comments");
    long timestamp = System.currentTimeMillis();
    // TODO: validate request parameters

    // Modify the state of server.
    addComment(name, comment);

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("name", name);
    commentEntity.setProperty("comment", comment);
    commentEntity.setProperty("time", timestamp);

    DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
    datastoreService.put(commentEntity);

    // Redirect back to the HTML page.
    response.sendRedirect("/index.html");
  }

  private void addComment(String name, String comment) {
    comments.add(new Comment(name, comment, LocalDateTime.now()));
  }

  /**
   * @return the value of parameter with the {@code name} in the {@code request}
   *         or returns {@code defaultValue} if that parameter does not exist.
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
