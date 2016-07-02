function runTask()
{
    var promptsDiv = this.parentElement;

    var variableList = {};

    for (i = 0; i < promptsDiv.getElementsByTagName('input').length; i++)
    {
        var input = promptsDiv.getElementsByTagName('input')[i];

        if (input.type == 'text')
        {
            variableList[input.name] = input.value;
        }
        else if (input.type == 'password')
        {
            variableList[input.name] = input.value;
        }
        else if (input.type == 'file')
        {
            variableList[input.name] = input.value;
        }
        else
        {
            variableList[input.name] = input.checked ? 'yes' : 'no';
        }
    }

    var params =
    {
        "task_id" : this.getAttribute('task_id'),
        "variable_list" : variableList
    }

    $.ajax({
        url: "/run/task",
        type: "POST",
        data: JSON.stringify(params),
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function ()
        {
            console.log('Run task request successfully sent');
        },
        error: function (xhr, ajaxOptions, thrownError)
        {
            console.log('Failed sending run task request');
        }
    });
}



function toggleExpand(event)
{
    //var target = event.target || event.srcElement;

    this.parentElement.parentElement.getElementsByTagName('div')[1].style.display = (this.parentElement.parentElement.getElementsByTagName('div')[1].style.display == '' ? 'none' : '')
}


function initSuccessCallback(response)
{
    if ('title' in response)
    {
        document.getElementById('title').innerHTML = response['title'];
    }



    var panelDiv = document.getElementById('panel');

    for (task in response['list'])
    {
        var id = response['list'][task]['id'];

        var title = response['list'][task]['title'];

        var description = 'description' in response['list'][task] ? response['list'][task]['description'] : '';

        var promptList = 'prompt_list' in response['list'][task] ? response['list'][task]['prompt_list'] : null;

        var taskDiv = document.createElement('div');


/*
        task.className = 'task';

        var taskName = document.createElement('div');

        taskName.className = 'task_name';

        var a = document.createElement('a');

        task_name.appendChild(a);

        a.href = 'javascript:void(0)';

        a.addEventListener(click, toggleExpand);

        a.innerHTML = '<h3>' + response['list'][task]['title'] + '</h3>';

        panel.appendChild(div);



        var promptsDiv = document.createElement('div');

        promptsDiv.id = 'task_prompts';

        task.appendChild(promptsDiv);

        var runButton = document.createElement('button');

        runButton.innerHTML = 'Run';

        runButton.addEventListener('click', runTask());

        */

        taskDiv.innerHTML = '<div class="task"><div id="task_header"><a href="javascript:void(0)"><h3>' + title + '</h3>' + description + '</a></div><div id="task_prompts" style="display: none;"><button id="button_run" task_id="' + id + '">Run</button></div></div>';

        taskDiv.getElementsByTagName('a')[0].addEventListener('click', toggleExpand);

        var runButton = taskDiv.getElementsByTagName('button')[0];

        runButton.addEventListener('click', runTask);

        var promptsDiv = runButton.parentElement;

        for (pr in promptList)
        {
            promptsDiv.insertBefore(document.createTextNode(promptList[pr]['prompt'] + ':'), runButton);

            var input = document.createElement('input');

            input.name = promptList[pr]['name'];

            if (promptList[pr]['type'] == 'password')
            {
                input.type = 'password';
            }
            else if (promptList[pr]['type'] == 'file')
            {
                input.type = 'file';
            }
            else if (promptList[pr]['type'] != 'value')
            {
                input.type = 'checkbox';

                input.flag = promptList[pr]['type'];
            }

            promptsDiv.insertBefore(input, runButton);

            promptsDiv.insertBefore(document.createElement('br'), runButton);
        }

        panelDiv.appendChild(taskDiv);
    }
}



function init()
{
//    var jsonRequest =
//    {
 //       command: "ADD",
  //      listId: listId,
   //     item: newItem
    //};
    
    
    $.ajax({
        url: "/list/tasks",
        type: "GET",
        //data: JSON.stringify(jsonRequest),
        //dataType: "json",
        //contentType: "application/json; charset=utf-8",
        success: initSuccessCallback,
        error: function (xhr, ajaxOptions, thrownError)
        {
            console.log('Failed function init()')
        }
    });
}



//window.onload = init();

