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
