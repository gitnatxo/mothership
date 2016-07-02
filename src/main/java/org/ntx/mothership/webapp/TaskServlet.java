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

import java.util.List;
import java.util.Map;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.*;

import org.json.simple.parser.JSONParser;

import org.ntx.mothership.*;

public class TaskServlet extends HttpServlet
{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
//        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);

        try
        {
            JSONParser parser = new JSONParser();

            JSONObject params = (JSONObject) parser.parse(request.getReader());

            List<Map> list = (List<Map>) Main.taskList.get("list");

            for (int i = 0; i < list.size(); i++)
            {
                String taskId = (String) ((Map) list.get(i)).get("id");

                if (taskId != null && taskId.equals((String) params.get("task_id")))
                {
                    System.out.println("Running: " + (String) ((Map) list.get(i)).get("exec"));

                    String exec = (String) ((Map)list.get(i)).get("exec");

                    Map<String, String> varMap = (Map<String, String>) params.get("variable_list");

                    for (String name : varMap.keySet())
                    {
                        String value = varMap.get(name);

                        exec = exec.replaceAll("\\{\\{ *" + name + " *\\}\\}", value);
                    }




                    if (System.getProperty("os.name").toLowerCase().startsWith("win"))
                    {
                        String[] command = {"cmd", "/c", "start", "cmd.exe", "/K", "\"" + exec + "\""};

                        Process p = Runtime.getRuntime().exec(command);

                    }
                    else
                    {
                        String[] command = {"/usr/bin/xterm", "-e", exec + "; while true; do sleep 5; done"};

                        Process p = Runtime.getRuntime().exec(command);
                    }

                    break;
                }
            }

System.out.println("Running task: " + params.toJSONString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
