package com.project.test.homework2;

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

    public void filterAll() {
        // $("[href='#/'").click();
        $(By.linkText("All")).click();
    }

    public void filterActive(){
        // $("[href='#/active'").click();
        $(By.linkText("Active")).click();
    }

    public void filterCompleted(){
        $(By.linkText("Completed")).click();
        //  $("[href='#/completed'").click();
    }

    public void toggle(String task) {
        taskList.find(text(task)).find(".toggle").click();
    }

    public void deleteTask(String task) {
        taskList.findBy(text(task)).hover();
        taskList.findBy(text(task)).find(".destroy").click();
    }

    ElementsCollection taskList = $$("#todo-list>li");

    /* добавить проверку счетчика
    public static void checkItemsLeftCounter(int number){
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(number)));
    }
     */

    @Test
    public void testCreateTask() {
        open("http://todomvc.com/examples/troopjs_require/#/");

        //adding tasks
        addTask("do1");
        addTask("do2");
        addTask("do3");
        addTask("do4");
        addTask("do5");
        taskList.shouldHave(texts("do1", "do2", "do3", "do4", "do5"));
        //marking task 1 and 2 as completed and checking that total number of tasks is 5
        toggle("do1");
        toggle("do2");
        taskList.shouldHave(texts("do1", "do2", "do3", "do4", "do5"));
        //checking active tasks on Active filter
        filterActive();
        taskList.shouldHave(texts("", "", "do3", "do4", "do5"));
        //checking completed tasks on Completed filter
        filterCompleted();
        taskList.shouldHave(texts("do1", "do2", "", "", ""));

        //marking third task as completed and checking filters
        filterAll();
        toggle("do3");
        taskList.shouldHave(texts("do1", "do2", "do3", "do4", "do5"));
        filterActive();
        taskList.shouldHave(texts("", "", "", "do4", "do5"));
        filterCompleted();
        taskList.shouldHave(texts("do1", "do2", "do3", "", ""));

        //adding tasks on Active filter page
        filterActive();
        addTask("active1");
        addTask("active2");
        taskList.shouldHave(texts("", "", "", "do4", "do5", "active1", "active2"));
        //checking that two new tasks does not appeared on Completed filter and presented on All filter
        filterCompleted();
        taskList.shouldHave(texts("do1", "do2", "do3", "", "", "", ""));
        filterAll();
        taskList.shouldHave(texts("do1", "do2", "do3", "do4", "do5", "active1", "active2"));

        //adding tasks on Completed filter page, checking that they are not presented there
        filterCompleted();
        addTask("completed1");
        addTask("completed2");
        taskList.shouldHave(texts("do1", "do2", "do3", "", "", "", "", "", ""));
        filterActive();
        taskList.shouldHave(texts("", "", "", "do4", "do5", "active1", "active2", "completed1", "completed2"));
        filterAll();
        taskList.shouldHave(texts("do1", "do2", "do3", "do4", "do5", "active1", "active2", "completed1", "completed2"));

        //rechecking tasks, as a result do1-3 marked as active, active1-2, completed1-2 are marked as completed
        toggle("do1");
        toggle("do2");
        toggle("do3");
        toggle("active1");
        toggle("active2");
        toggle("completed1");
        toggle("completed2");
        taskList.shouldHave(texts("do1", "do2", "do3", "do4", "do5", "active1", "active2", "completed1", "completed2"));

        //deleting completed tasks from Completed filter
        filterCompleted();
        taskList.shouldHave(texts("", "", "", "", "", "active1", "active2", "completed1", "completed2"));
        deleteTask("active1");
        deleteTask("completed1");
        $("#clear-completed").click();
        taskList.shouldHave(texts("", "", "", "", ""));

        //complete from Active
        filterActive();
        taskList.shouldHave(texts("do1", "do2", "do3", "do4", "do5"));
        toggle("do4");
        toggle("do5");
        taskList.shouldHave(texts("do1", "do2", "do3", "", ""));

        //delete completed one by one from All
        filterAll();
        deleteTask("do4");
        deleteTask("do5");
        taskList.shouldHave(texts("do1", "do2", "do3"));

        //delete completed from Active
        filterActive();
        toggle("do3");
        $("#clear-completed").click();
        filterCompleted();
        taskList.shouldHave(texts("", ""));

        //renew task from completed
        filterAll();
        toggle("do2");
        filterCompleted();
        taskList.shouldHave(texts("", "do2"));
        toggle("do2");
        taskList.shouldHave(texts("", ""));
        filterAll();
        taskList.shouldHave(texts("do1", "do2"));
        filterActive();
        taskList.shouldHave(texts("do1", "do2"));

        //mark all as completed
        filterAll();
        $("#toggle-all").click();
        taskList.shouldHave(texts("do1", "do2"));
        filterActive();
        taskList.shouldHave(texts("", ""));
        filterCompleted();
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
        filterAll();
        toggle("task2");
        taskList.shouldHave(texts("task1", "task2"));
        filterActive();
        taskList.shouldHave(texts("task1", ""));
        $("#toggle-all").click();
        taskList.shouldHave(texts("", ""));
        filterCompleted();
        taskList.shouldHave(texts("task1", "task2"));
        $("#toggle-all").click();
        taskList.shouldHave(texts("", ""));
        filterActive();
        taskList.shouldHave(texts("task1", "task2"));

        //edit task...

        //edit completed task

    }
}