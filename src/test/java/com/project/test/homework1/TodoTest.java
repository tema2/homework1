package com.project.test.homework1;

import com.codeborne.selenide.ElementsCollection;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class TodoTest {

    public void addTask(String task) {
        $("#new-todo").setValue(task).pressEnter();
    }
    ElementsCollection taskList = $$("#todo-list>li");

    @Test
    public void testCreateTask() {

        open("http://todomvc.com/examples/troopjs_require/#/");
        addTask("do1");
        addTask("do2");
        addTask("do3");
        addTask("do4");

        taskList.shouldHave(texts("do1", "do2", "do3", "do4"));

        taskList.findBy(text("do2")).hover();
        taskList.findBy(text("do2")).find(".destroy").click();

        taskList.shouldHave(texts("do1", "do3", "do4"));

        taskList.find(text("do4")).find(".toggle").click();
        $("#clear-completed").click();
        taskList.shouldHave(texts("do1", "do3"));

        $("#toggle-all").click();
        $("#clear-completed").click();

        taskList.shouldBe(empty);
    }
}