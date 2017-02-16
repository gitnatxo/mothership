# mothership
A desktop tool to launch automated tasks

## Building

mvn compile assembly:single

## Customizing

You may customize the user interface of the application.

The parameters your are able to customize are:

- Icon in the title bar (icon.png)

- Stylesheet (style.css)

On the other hand, you may customize your application either, prior to compiling it, or after compiling it.

To customize the application before compiling, do replace the icon or edit the stylesheet located under /src/main/webapp.

To do so after the compilation:

1. Create a working directory

> mkdir /path/to/workdir

2. Change to the working directory

> mkdir /path/to/workdir

3. Extract the contents of the application's JAR archive

> unzip /path/to/source/application.jar

4. Replace/edit the icon and style files located in the current directory.

5. Re-archive the application

> zip -r /path/to/destination/application.jar ./*

## Configuring the task list

Mothership is a desktop task launcher intended to automate operations that otherwise would require much typing on a command line.

To work, it reads a configuration file in Yaml format which lists the tasks that may be launched during an operation session. The application parses the configuration file and renders the task list with all the prompts.

As an example, below there is a task list:

title: This is the task list title

list:

  - title: Search manual page

    description: This is the task description

    prompt_list:

      - name: page

        prompt: Manual page

        type: value

      - name: debug

        prompt: Debug mode

        type: -d

    exec: man {{ page }}

Where the first "title" entry defines the title for the this working session.

The list is a list element containing one entry per task. It may contain a minimum of 1 task, and up to as many tasks as needed.

The fields that describe a tasks are: the title, which is a short description of the task.

The description is an optional file, and defines a longer description of the task.

The prompt_list is another list element containg the prompts the user must fill in prior to launching the task.

Every prompt must contain a name, which will be used as a placeholder in the "exec" element, the prompt that will be shown to the user, and the type, which may take a value from the set: text, password, file. Other values may set as the type, but in such case the application will render a checkbox so, if checked by the user, the literal content of the field "type" will be used to replace the placeholder in the "exec" field. See the prompt named "debug" in the example above.

The exec element contains the command line that will be used. If the tasks prompts for user inserted values, placeholders using the jinja style {{ name }} may be added to the field content. Prior to running the command, the application will replace the placeholders with the values inserted by the user save for the checkbox parameters, that will be replaced with the content of the field "type" (see the prompt named "debug" in the example above).

## Operation the application

Once you have your configuration file defined, you may run the application providing the path to the configuration file:

> java -jar /path/to/application.jar /path/to/task_list.yml

Alternatively, you may embed the task list file into your application archive, so you don't need to distribute it along with the task list file:

1. Create a working directory:

> mkdir /path/to/workdir

2. Change to the working directory:

> cd /path/to/workdir

3. replace the file conf/task_list.yml with your configuration file:

> cp /path/to/source/task_list.yml conf/task_list.yml

4. Re-achive your application

> zip -r /path/to/destination/application.jar ./*
