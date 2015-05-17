package com.project.test;

import com.codeborne.selenide.ElementsCollection;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class TodoCompleteTest {

    public void addTask(String task) {
        $("#new-todo").setValue(task).pressEnter();
    }
    public void gotoFilter(String filter) {
        $(By.linkText(filter)).click();
    }

    ElementsCollection taskList = $$("#todo-list>li");

    @Test
    public void testCreateTask() {

        //adding 5 tasks
        open("http://todomvc.com/examples/troopjs_require/#/");
        addTask("do1");
        addTask("do2");
        addTask("do3");
        addTask("do4");
        addTask("do5");
        taskList.shouldHave(texts("do1", "do2", "do3", "do4", "do5"));
        //marking task 1 and 2 as completed and checking that total number of tasks is 5
        taskList.find(text("do1")).find(".toggle").click();
        taskList.find(text("do2")).find(".toggle").click();
        taskList.shouldHave(texts("do1", "do2", "do3", "do4", "do5"));
        //checking active tasks on Active filter
        gotoFilter("Active");
        taskList.shouldHave(texts("", "", "do3", "do4", "do5"));
        //checking completed tasks on Completed filter
        gotoFilter("Completed");
        taskList.shouldHave(texts("do1", "do2", "", "", ""));

        //marking third task as completed and checking filters
        gotoFilter("All");
        taskList.find(text("do3")).find(".toggle").click();
        taskList.shouldHave(texts("do1", "do2", "do3", "do4", "do5"));
        gotoFilter("Active");
        taskList.shouldHave(texts("", "", "", "do4", "do5"));
        gotoFilter("Completed");
        taskList.shouldHave(texts("do1", "do2", "do3", "", ""));

        //adding tasks on Active filter page
        gotoFilter("Active");
        addTask("active1");
        addTask("active2");
        taskList.shouldHave(texts("", "", "", "do4", "do5", "active1", "active2"));
        //checking that two new tasks does not appeared on Completed filter and presented on All filter
        gotoFilter("Completed");
        taskList.shouldHave(texts("do1", "do2", "do3", "", "", "", ""));
        gotoFilter("All");
        taskList.shouldHave(texts("do1", "do2", "do3", "do4", "do5", "active1", "active2"));

        //adding tasks on Completed filter page, checking that they are not presented there
        gotoFilter("Completed");
        addTask("completed1");
        addTask("completed2");
        taskList.shouldHave(texts("do1", "do2", "do3", "", "", "", "", "", ""));
        gotoFilter("Active");
        taskList.shouldHave(texts("", "", "", "do4", "do5", "active1", "active2", "completed1", "completed2"));
        gotoFilter("All");
        taskList.shouldHave(texts("do1", "do2", "do3", "do4", "do5", "active1", "active2", "completed1", "completed2"));

        //rechecking tasks, as a result do1-3 marked as active, active1-2, completed1-2 are marked as completed
        taskList.find(text("do1")).find(".toggle").click();
        taskList.find(text("do2")).find(".toggle").click();
        taskList.find(text("do3")).find(".toggle").click();
        taskList.find(text("active1")).find(".toggle").click();
        taskList.find(text("active2")).find(".toggle").click();
        taskList.find(text("completed1")).find(".toggle").click();
        taskList.find(text("completed2")).find(".toggle").click();
        taskList.shouldHave(texts("do1", "do2", "do3", "do4", "do5", "active1", "active2", "completed1", "completed2"));

        //deleting completed tasks from Completed filter
        gotoFilter("Completed");
        taskList.shouldHave(texts("", "", "", "", "", "active1", "active2", "completed1", "completed2"));
        taskList.findBy(text("active1")).hover();
        taskList.findBy(text("active1")).find(".destroy").click();
        taskList.findBy(text("completed1")).hover();
        taskList.findBy(text("completed1")).find(".destroy").click();
        $("#clear-completed").click();
        taskList.shouldHave(texts("", "", "", "", ""));

        //complete from Active
        gotoFilter("Active");
        taskList.shouldHave(texts("do1", "do2", "do3", "do4", "do5"));
        taskList.find(text("do4")).find(".toggle").click();
        taskList.find(text("do5")).find(".toggle").click();
        taskList.shouldHave(texts("do1", "do2", "do3", "", ""));

        //delete completed one by one from All
        gotoFilter("All");
        taskList.findBy(text("do4")).hover();
        taskList.findBy(text("do4")).find(".destroy").click();
        taskList.findBy(text("do5")).hover();
        taskList.findBy(text("do5")).find(".destroy").click();
        taskList.shouldHave(texts("do1", "do2", "do3"));

        //delete completed from Active
        gotoFilter("Active");
        taskList.find(text("do3")).find(".toggle").click();
        $("#clear-completed").click();
        gotoFilter("Completed");
        taskList.shouldHave(texts("", ""));

        //renew task from completed
        gotoFilter("All");
        taskList.find(text("do2")).find(".toggle").click();
        gotoFilter("Completed");
        taskList.shouldHave(texts("", "do2"));
        taskList.find(text("do2")).find(".toggle").click();
        taskList.shouldHave(texts("", ""));
        gotoFilter("All");
        taskList.shouldHave(texts("do1", "do2"));
        gotoFilter("Active");
        taskList.shouldHave(texts("do1", "do2"));

        //mark all as completed
        gotoFilter("All");
        $("#toggle-all").click();
        taskList.shouldHave(texts("do1", "do2"));
        gotoFilter("Active");
        taskList.shouldHave(texts("", ""));
        gotoFilter("Completed");
        taskList.shouldHave(texts("do1", "do2"));
        $("#clear-completed").click();
        taskList.shouldBe(empty);

        //mark all as completed-2
        /*  Тут я і знайшов дефект. Якщо видалити всі задачі знаходячись у фільтрі Completed,
            то фільтри зникають і лишається лише поле вводу нової задачі. Якщо в ній створити
            нову задачу, то знову з'являться фільтри і активним фільтром буде Completed.
            Щоб побачити активні задачі, необхідно перейти на фільтр All або Active.
            Щоб тест пройшов, я переходжу у фільтр All, після додавання нових задач.
        */
        addTask("task1");
        addTask("task2");
        gotoFilter("All");
        taskList.find(text("task2")).find(".toggle").click();
        taskList.shouldHave(texts("task1", "task2"));
        gotoFilter("Active");
        taskList.shouldHave(texts("task1", ""));
        $("#toggle-all").click();
        taskList.shouldHave(texts("", ""));
        gotoFilter("Completed");
        taskList.shouldHave(texts("task1", "task2"));
        $("#toggle-all").click();
        taskList.shouldHave(texts("", ""));
        gotoFilter("Active");
        taskList.shouldHave(texts("task1", "task2"));

        //edit task...

        //edit completed task

    }
}