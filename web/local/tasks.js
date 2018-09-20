"use strict";
// Run some configuration code when the web page loads
var backendUrl = "https://stacklight.herokuapp.com";
var $;
var taskList;
var newtaskform;
var TaskList = /** @class */ (function () {
    function TaskList() {
    }
    TaskList.prototype.refresh = function () {
        $.ajax({
            type: "GET",
            url: backendUrl + "/tasks/all",
            dataType: "json",
            success: taskList.update
        });
    };
    TaskList.prototype.update = function (data) {
        $("#taskList").html("<table>");
        console.log(data);
        for (var i = 0; i < data.mTaskData.length; ++i) {
            $("#taskList").append("<tr><td>" + data.mTaskData[i].mId + ". </td><td> <b> " + data.mTaskData[i].mName + " :</b></td><td> " + data.mTaskData[i].mDescription + "</td><tr>");
        }
    };
    return TaskList;
}());
var NewTaskForm = /** @class */ (function () {
    function NewTaskForm() {
        $("#addButton").click(this.submitForm);
    }
    NewTaskForm.prototype.submitForm = function () {
        var taskname = "" + $("#taskname").val();
        var description = "" + $("#description").val();
        var priority = $("#priority").val();
        var assignee = "" + $("#assignee").val();
        var assigner = "" + $("assigner").val();
        if (taskname === "" || description === "") {
            window.alert("Error: Task is not valid");
            return;
        }
        console.log(taskname);
        // set up an AJAX post.  When the server replies, the result will go to
        // onSubmitResponse
        $.ajax({
            type: "POST",
            url: backendUrl + "/tasks",
            dataType: "json",
            data: JSON.stringify({ mProjectId: 1, mTaskname: taskname,
                mDescription: description, mPriority: priority, mAssignee: assignee,
                mAssigner: assigner }),
            success: newtaskform.onSubmitResponse,
            error: newtaskform.onSubmitResponse
        });
    };
    NewTaskForm.prototype.onSubmitResponse = function (data) {
        // If we get an "ok" message, clear the form
        if (data.mStatus === "ok") {
            console.log("Task Added Sucessfully!");
            taskList.refresh();
        }
        // Handle explicit errors with a detailed popup message
        else if (data.mStatus === "error") {
            window.alert("The server replied with an error:\n" + data.mMessage);
        }
        // Handle other errors with a less-detailed popup message
        else {
            window.alert("Unspecified error");
        }
    };
    return NewTaskForm;
}());
$(document).ready(function () {
    console.log("Loading Tasks Page.......");
    taskList = new TaskList();
    newtaskform = new NewTaskForm();
    taskList.refresh();
});
