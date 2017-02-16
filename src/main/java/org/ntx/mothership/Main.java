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

package org.ntx.mothership;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import java.security.ProtectionDomain;

import java.io.FileInputStream;
import java.io.InputStream;

import java.net.InetSocketAddress;
import java.net.URL;

import java.util.List;
import java.util.Map;

import java.util.concurrent.ThreadLocalRandom;



import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
 
//import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
//import org.apache.commons.lang3.builder.ToStringStyle;
 
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.yaml.snakeyaml.Yaml;


public class Main extends Application
{

    public static Map<String, Object> taskList; // = new JSONObject();
    //public static JSONObject taskList = new JSONObject();

  VBox vb = new VBox();

  @Override
  public void start(Stage primaryStage)
  {
    primaryStage.setTitle("Mothership");

	// TODO: set the application icon from the jar
	// primaryScene.getIcons().add(new Image("/path/to/icon.png"));

    vb.setId("root");

    WebView  browser = new WebView();
    WebEngine engine = browser.getEngine();
    String url = "http://localhost:" + System.getProperty("app.http.port");
    engine.load(url);
    
//    vb.setPadding(new Insets(30, 50, 50, 50));
    vb.setSpacing(10);
    vb.setAlignment(Pos.CENTER);
    vb.getChildren().addAll(browser);

    Scene scene = new Scene(vb);
    primaryStage.setScene(scene);
    primaryStage.show();
  }



    public static void main(String[] args) throws Exception
    {
//        String jetty_home = System.getProperty("jetty.home","..");

        setup(args);

        Server server = new Server(new InetSocketAddress("127.0.0.1", Integer.parseInt(System.getProperty("app.http.port"))));

        WebAppContext context = new WebAppContext();
        context.setServer(server);
        context.setContextPath("/");
 
        ProtectionDomain protectionDomain = Main.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        context.setWar(location.toExternalForm());
 
        server.setHandler(context);

        server.start();



        Application.launch(args);



        server.stop();
        server.join();
    }



    static void setup (String[] argList)
    {
        System.setProperty("app.http.port", "" + ThreadLocalRandom.current().nextInt(2000, 65000));


        Yaml yaml = new Yaml();

        InputStream ios = null;

        try {

            if (argList.length > 0)
            {
                File fileYaml = new File(argList[0]);

                if (fileYaml.exists())
                {
                    ios = new FileInputStream(fileYaml);
                }
            }
            else
            {
                File fileYaml = new File("task_list.yml");
                
                if (fileYaml.exists())
                {
System.out.println("Default local file exists");
                    ios = new FileInputStream(new File("task_list.yml"));
                }
                else
                {
System.out.println("Trying default embedded task list file");
                    ios = Main.class.getClassLoader().getResourceAsStream("conf/task_list.yml");
                }
            }



            if (ios == null)
            {
                System.out.println("Failed to load task list file");

                System.exit(1);
            }
            else
            {
                // Parse the YAML file and return the output as a series of Maps and Lists
                Main.taskList = (Map< String, Object>) yaml.load(ios);

                List<Map> list = (List<Map>) Main.taskList.get("list");

                for (int i = 0; i < list.size(); i++)
                {
                    ((Map<String, Object>) list.get(i)).put("id", "" + ThreadLocalRandom.current().nextInt(1000, 9999));
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

/*
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try
        {
            YamlTaskList yamlTaskList = mapper.readValue(new File("task_list.yaml"), YamlTaskList.class);

            System.out.println(ReflectionToStringBuilder.toString(yamlTaskList,ToStringStyle.MULTI_LINE_STYLE));
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
*/
    }
}
