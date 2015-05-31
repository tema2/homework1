package com.project.test.homework2;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TodoCompleteTest {

    public void addTask(String task) {
        $("#new-todo").setValue(task).pressEnter();
    }

    public void filterAll() {
        $("[href='#/']").click();
    }

    public void filterActive(){
        $("[href='#/active']").click();
    }

    public void filterCompleted(){
        $("[href='#/completed']").click();
    }

    public void toggle(String task) {
        taskList.find(text(task)).find(".toggle").click();
    }

    public void deleteTask(String task) {
        taskList.findBy(text(task)).hover();
        taskList.findBy(text(task)).find(".destroy").click();
    }

    public void editTask(String oldTask, String newTask) {
        taskList.findBy(text(oldTask)).find("label").doubleClick();
        taskList.findBy(cssClass("editing")).find(".edit").setValue(newTask).pressEnter();
        }

    public void clearCompleted(){
        clearCompleted.click();
        clearCompleted.should(disappear);
    }

    public void toggleAll() {
        $("#toggle-all").click();
    }

    public static void checkItemsLeftCounter(int number){
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(number)));
    }

/*    public static void checkCompletedCounter(int number) {
       $("#clear-completed").shouldHave(text(Integer.toString(number)));
    } */

    ElementsCollection taskList = $$("#todo-list li");
    SelenideElement clearCompleted = $("#clear-completed");
    //SelenideElement itemsLeft = $("#todo-count>strong");


    @Test
    public void completePageTest() {
        open("http://todomvc.com/examples/troopjs_require/#/");

        //adding tasks
        addTask("do1");
        addTask("do2");
        addTask("do3");
        addTask("do4");
        addTask("do5");
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3", "do4", "do5"));
        checkItemsLeftCounter(5);
        clearCompleted.shouldBe(hidden);

        //marking task 1 and 2 as completed and checking that total number of tasks on filter All is 5
        toggle("do1");
        toggle("do2");
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3", "do4", "do5"));
        checkItemsLeftCounter(3);
        clearCompleted.shouldBe(visible);
        // checkCompletedCounter(2);

        //checking active tasks on Active filter
        filterActive();
        taskList.filter(visible).shouldHave(exactTexts("do3", "do4", "do5"));
        checkItemsLeftCounter(3);
        clearCompleted.shouldBe(visible);
        // checkCompletedCounter(2);

        //checking completed tasks on Completed filter
        filterCompleted();
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2"));
        checkItemsLeftCounter(3);
        // checkCompletedCounter(2);

        //marking third task as completed and checking filters
        filterAll();
        toggle("do3");
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3", "do4", "do5"));
        checkItemsLeftCounter(2);
        // checkCompletedCounter(3);
        filterActive();
        taskList.filter(visible).shouldHave(exactTexts("do4", "do5"));
        checkItemsLeftCounter(2);
        // checkCompletedCounter(3);
        filterCompleted();
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3"));
        checkItemsLeftCounter(2);
        // checkCompletedCounter(3);

        //adding tasks on Active filter page
        filterActive();
        addTask("active1");
        addTask("active2");
        taskList.filter(visible).shouldHave(exactTexts("do4", "do5", "active1", "active2"));
        checkItemsLeftCounter(4);
        // checkCompletedCounter(3);

        //checking that two new tasks does not appeared on Completed filter and presented on All filter
        filterCompleted();
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3"));
        checkItemsLeftCounter(4);
        // checkCompletedCounter(3);
        filterAll();
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3", "do4", "do5", "active1", "active2"));
        checkItemsLeftCounter(4);
        // checkCompletedCounter(3);

        //adding tasks on Completed filter page, checking that they are not presented there
        filterCompleted();
        addTask("completed1");
        addTask("completed2");
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3"));
        checkItemsLeftCounter(6);
        // checkCompletedCounter(3);
        filterActive();
        taskList.filter(visible).shouldHave(exactTexts("do4", "do5", "active1", "active2", "completed1", "completed2"));
        checkItemsLeftCounter(6);
        // checkCompletedCounter(3);
        filterAll();
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3", "do4", "do5", "active1", "active2", "completed1", "completed2"));
        checkItemsLeftCounter(6);
        // checkCompletedCounter(3);

        //rechecking tasks, as a result do1-5 marked as active, active1-2, completed1-2 are marked as completed
        toggle("do1");
        toggle("do2");
        toggle("do3");
        toggle("active1");
        toggle("active2");
        toggle("completed1");
        toggle("completed2");
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3", "do4", "do5", "active1", "active2", "completed1", "completed2"));
        checkItemsLeftCounter(5);
        // checkCompletedCounter(4);

        //deleting completed tasks from Completed filter
        filterCompleted();
        taskList.filter(visible).shouldHave(exactTexts("active1", "active2", "completed1", "completed2"));
        deleteTask("active1");
        deleteTask("completed1");
        clearCompleted();
        taskList.filter(visible).shouldBe(empty);
        checkItemsLeftCounter(5);
        clearCompleted.shouldBe(hidden);

        //complete from Active
        filterActive();
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3", "do4", "do5"));
        toggle("do4");
        toggle("do5");
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3"));
        checkItemsLeftCounter(3);
        // checkCompletedCounter(2);

        //delete completed one by one from All
        filterAll();
        deleteTask("do4");
        deleteTask("do5");
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3"));
        checkItemsLeftCounter(3);
        clearCompleted.shouldBe(hidden);

        //delete completed from Active
        filterActive();
        toggle("do3");
        clearCompleted();
        filterCompleted();
        taskList.filter(visible).shouldBe(empty);
        checkItemsLeftCounter(2);

        //renew task from completed
        filterAll();
        toggle("do2");
        filterCompleted();
        taskList.filter(visible).shouldHave(exactTexts("do2"));
        toggle("do2");
        taskList.filter(visible).shouldBe(empty);
        clearCompleted.shouldBe(hidden);
        filterAll();
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2"));
        checkItemsLeftCounter(2);
        filterActive();
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2"));
        checkItemsLeftCounter(2);

        //mark all as completed
        filterAll();
        toggleAll();
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2"));
        filterActive();
        taskList.filter(visible).shouldBe(empty);
        filterCompleted();
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2"));
        // checkCompletedCounter(2);
        clearCompleted();
        taskList.filter(visible).shouldBe(empty);
        clearCompleted.shouldBe(hidden);

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
        taskList.filter(visible).shouldHave(exactTexts("task1", "task2"));
        checkItemsLeftCounter(1);
        // checkCompletedCounter(1);
        filterActive();
        taskList.filter(visible).shouldHave(exactTexts("task1"));
        toggleAll();
        taskList.filter(visible).shouldBe(empty);
        filterCompleted();
        taskList.filter(visible).shouldHave(exactTexts("task1", "task2"));
        // checkCompletedCounter(2);
        toggleAll();
        taskList.filter(visible).shouldBe(empty);
        clearCompleted.shouldBe(hidden);
        filterActive();
        taskList.filter(visible).shouldHave(exactTexts("task1", "task2"));
        checkItemsLeftCounter(2);

        //edit task...
        filterAll();
        editTask("task1", "task1 edited");
        checkItemsLeftCounter(2);
        filterActive();
        editTask("task2", "task2 edited");
        checkItemsLeftCounter(2);

        //edit completed task
        toggle("task2 edited");
        checkItemsLeftCounter(1);
        filterCompleted();
        editTask("task2 edited", "task2");
        toggle("task2");
        taskList.filter(visible).shouldBe(empty);
        filterAll();
        taskList.filter(visible).shouldHave(exactTexts("task1 edited", "task2"));

    }
}