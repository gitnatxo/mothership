/*    
 * Mothership - A desktop tool to launch automated tasks
 * Copyright (C) 2016 Natxo Silla
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.ntx.mothership.webapp;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

import org.json.simple.*;

import org.ntx.mothership.*;

public class TaskListServlet extends HttpServlet
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
//        response.getWriter().println("{\"title\": \"Task list\", \"list\": [{\"id\": \"1234\", \"title\": \"This is the title\", \"description\": \"This is the description\", \"prompt_list\": [{\"prompt\": \"Insert file\", \"variable\": \"file\", \"type\": \"file\"},{\"prompt\": \"New file name\", \"variable\": \"name\", \"type\": \"value\"},{\"prompt\": \"Secret key\", \"variable\": \"password\", \"type\": \"password\"},{\"prompt\": \"Use debug mode\", \"variable\": \"debug\", \"type\": \"-d\"}]},{\"id\": \"3134\", \"title\": \"A task without description\", \"prompt_list\": [{\"prompt\": \"Insert file\", \"variable\": \"file\", \"type\": \"file\"},{\"prompt\": \"New file name\", \"variable\": \"name\", \"type\": \"value\"},{\"prompt\": \"Secret key\", \"variable\": \"password\", \"type\": \"password\"},{\"prompt\": \"Use debug mode\", \"variable\": \"debug\", \"type\": \"-d\"}]}]}");

        JSONObject jsonTaskList = new JSONObject();

        if (Main.taskList.get("title") != null)
        {
            jsonTaskList.put("title", (String) Main.taskList.get("title"));
        }



        List<Object> list = (List<Object>) Main.taskList.get("list");

	if (list == null)
        {
            System.out.println("Missing task list");
        }
        else
        {
            JSONArray jsonList = new JSONArray();

            jsonTaskList.put("list", jsonList);

            for (int i = 0; i < list.size(); i++)
            {
		Map<String, Object> task = (Map<String, Object>) list.get(i);

                String title = (String) task.get("title");

                String exec = (String) task.get("exec");

                String id = (String) task.get("id");

                // title and exec are mandatory fields
                if (title == null || exec == null || id == null)
                {
                    System.out.println("Either missing title, exec or id fields in task");
                }
                else
                {
                    JSONObject jsonTask = new JSONObject();

                    jsonList.add(jsonTask);

                    jsonTask.put("title", title);

                    jsonTask.put("id", id);

                    String description = (String) task.get("description");

                    if (description != null)
                    {
                        jsonTask.put("description", description);
                    }

                    List promptList = (List) task.get("prompt_list");

                    if (promptList != null)
                    {
                        JSONArray jsonPromptList = new JSONArray();

                        jsonTask.put("prompt_list", jsonPromptList);

                        for (i = 0; i < promptList.size(); i++)
                        {
                            JSONObject jsonPrompt = new JSONObject();

                            jsonPromptList.add(jsonPrompt);

                            jsonPrompt.put("name", ((Map<String, String>) promptList.get(i)).get("name"));

                            jsonPrompt.put("prompt", ((Map<String, String>) promptList.get(i)).get("prompt"));

                            jsonPrompt.put("type", ((Map<String, String>) promptList.get(i)).get("type"));
                        }
                    }    
                }
            }
        }



        response.getWriter().println(jsonTaskList.toJSONString());
    }
}
