package com.project.test;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TodoMVC {

    public static ElementsCollection taskList = $$("#todo-list li");
    public static SelenideElement clearCompleted = $("#clear-completed");
    public static SelenideElement itemsLeft = $("#todo-count>strong");

    public static void addTask(String task) {
        $("#new-todo").setValue(task).pressEnter();
    }

    public static void filterAll() {
        $("[href='#/']").click();
    }

    public static void filterActive(){
        $("[href='#/active']").click();
    }

    public static void filterCompleted(){
        $("[href='#/completed']").click();
    }

    public static void toggle(String task) {
        taskList.find(text(task)).find(".toggle").click();
    }

    public static void deleteTask(String task) {
        taskList.findBy(text(task)).hover().find(".destroy").click();
    }

    public static void editTask(String oldTask, String newTask) {
        taskList.findBy(text(oldTask)).find("label").doubleClick();
        taskList.findBy(cssClass("editing")).find(".edit").setValue(newTask).pressEnter();
    }

    public static void clearCompleted(){
        clearCompleted.click();
        clearCompleted.should(disappear);
    }

    public static void toggleAll() {
        $("#toggle-all").click();
    }

    public static void checkItemsLeftCounter(int number){
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(number)));
    }

    public static void checkCompletedCounter(int number) {
        $("#clear-completed").shouldHave(exactText(Integer.toString(number)));
        /*
        что если тут написать exactText вместо
        $("#clear-completed").shouldHave(text("(" + Integer.toString(number) + ")"));
        я не понимаю как кавычки спасают положение
         */
    }

}