package com.project.test.homework3;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;
import static com.project.test.TestMethods.*;



public class TodoSeparateFilters {
    @BeforeClass
    public static void clearCache() {
        open("http://todomvc.com/examples/troopjs_require/#/");
    }

    @Before
    public void clearData() {
        executeJavaScript("localStorage.clear()");
        open("http://google.com/");
        open("http://todomvc.com/examples/troopjs_require/#/");
    }

    @Test
    public void testAtAllFilter() {
        //adding tasks
        addTask("do1");
        addTask("do2");
        addTask("do3");
        addTask("do4");
        addTask("do5");

        //checking condition after adding tasks
        filterAll();
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3", "do4", "do5"));
        checkItemsLeftCounter(5);
        clearCompleted.shouldBe(hidden);
        itemsLeft.shouldBe(visible);

        //marking task 1 and 2 as completed and checking that total number of tasks on filter All is 5
        toggle("do1");
        toggle("do2");
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3", "do4", "do5"));
        checkItemsLeftCounter(3);
        clearCompleted.shouldBe(visible);
        checkCompletedCounter(2);
        itemsLeft.shouldBe(visible);

        //toggle do3
        toggle("do3");
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3", "do4", "do5"));
        checkItemsLeftCounter(2);
        checkCompletedCounter(3);

        //toggling all
        toggleAll();
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3", "do4", "do5"));
        checkCompletedCounter(5);
        checkItemsLeftCounter(0);
        toggleAll();

        //edit task
        editTask("do3", "do3 edited");
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3 edited", "do4", "do5"));

        //delete task
        deleteTask("do5");
        deleteTask("do4");
        checkItemsLeftCounter(3);
        clearCompleted.shouldBe(hidden);

        //clear completed
        toggleAll();
        clearCompleted();
        itemsLeft.shouldBe(hidden);
        clearCompleted.shouldBe(hidden);

    }

    @Test
    public void testAtActiveFilter() {
        //adding tasks
        addTask("do1");
        addTask("do2");
        addTask("do3");
        addTask("do4");
        addTask("do5");

        //checking active tasks on Active filter
        filterActive();
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3", "do4", "do5"));
        checkItemsLeftCounter(5);
        clearCompleted.shouldBe(hidden);

        //marking task 1 and 2 as completed and checking that total number of active tasks on filter is 3
        toggle("do1");
        toggle("do2");
        taskList.filter(visible).shouldHave(exactTexts("do3", "do4", "do5"));
        checkItemsLeftCounter(3);
        clearCompleted.shouldBe(visible);
        checkCompletedCounter(2);

        //toggle do3
        toggle("do3");
        taskList.filter(visible).shouldHave(exactTexts("do4", "do5"));
        checkItemsLeftCounter(2);
        checkCompletedCounter(3);

        //adding tasks on Active filter page
        addTask("active1");
        addTask("active2");
        taskList.filter(visible).shouldHave(exactTexts("do4", "do5", "active1", "active2"));
        checkItemsLeftCounter(4);
        checkCompletedCounter(3);

        //edit task
        editTask("active1", "active1 edited");
        checkCompletedCounter(3);
        checkItemsLeftCounter(4);

        //delete task
        deleteTask("active1 edited");
        checkCompletedCounter(3);
        checkItemsLeftCounter(3);

        //toggle all
        toggleAll();
        taskList.filter(visible).shouldBe(empty);
        checkItemsLeftCounter(0);
        checkCompletedCounter(6);

        //clear all
        clearCompleted();
        taskList.filter(visible).shouldBe(empty);

    }

    @Test
    public void testAtCompletedFilter() {
        //adding tasks
        addTask("do1");
        addTask("do2");
        addTask("do3");
        addTask("do4");
        addTask("do5");
        filterAll();
        toggle("do1");
        toggle("do2");

        //checking completed tasks on Completed filter
        filterCompleted();
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2"));
        checkItemsLeftCounter(3);
        checkCompletedCounter(2);

        //marking do3 as complete
        filterAll();
        toggle("do3");
        filterCompleted();
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2", "do3"));
        checkItemsLeftCounter(2);
        checkCompletedCounter(3);

        //unmarking do3
        toggle("do3");
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2"));
        checkCompletedCounter(2);
        checkItemsLeftCounter(3);

        //edit task
        editTask("do2", "do2 edited");
        taskList.filter(visible).shouldHave(exactTexts("do1", "do2 edited"));
        checkCompletedCounter(2);
        checkItemsLeftCounter(3);

        //delete task
        deleteTask("do2 edited");
        checkCompletedCounter(1);
        checkItemsLeftCounter(3);

        //clear completed
        filterActive();
        toggleAll();
        checkItemsLeftCounter(0);
        checkCompletedCounter(4);
        filterCompleted();
        clearCompleted();
        clearCompleted.shouldBe(hidden);
        itemsLeft.shouldBe(hidden);
    }
}
