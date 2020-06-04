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
import com.google.gson.Gson;
import com.google.sps.data.Comment;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/** Servlet that handles getting and posting comment content. */
@WebServlet("/comment")
public final class CommentServlet extends HttpServlet {
  
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
    Query query = new Query("Comment").addSort("timestamp", Query.SortDirection.DESCENDING);

    DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastoreService.prepare(query);

    List<Comment> comments = new ArrayList<>();
    for (Entity entity: results.asIterable()) {
      long id = entity.getKey().getId();
      String commenter = (String) entity.getProperty("name");
      String content = (String) entity.getProperty("comment");
      long timestamp = (long) entity.getProperty("timestamp");

      LocalDateTime time = convertTimestampToLocalDateTime(timestamp);

      Comment comment = new Comment(id, commenter, content, time);
      comments.add(comment);
    }
    return comments;
  }

  /** Converts the long timestamp into LocalDateTime form */
  private LocalDateTime convertTimestampToLocalDateTime(long timestamp) {
    LocalDateTime time =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp),
                    TimeZone.getDefault().toZoneId());
    return time;
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
    String name = getParameter(request, /*name=*/"name", /*defaultValue=*/"");
    String comment = getParameter(request, /*name=*/"comment", /*defaultValue=*/"No comments");
    long timestamp = System.currentTimeMillis();
    // TODO: validate request parameters

    // Stores the comment into the Datastore
    storeComment(name, comment, timestamp);

    // Redirects back to the HTML page.
    response.sendRedirect("/index.html");
  }

  /** Stores the comment into the Datastore */
  private void storeComment(String name, String comment, long timestamp) {
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("name", name);
    commentEntity.setProperty("comment", comment);
    commentEntity.setProperty("timestamp", timestamp);

    DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
    datastoreService.put(commentEntity);
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
