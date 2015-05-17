package com.project.test;

import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class TodoTest {

    @Test
    public void testCreateTask() {

        open("http://todomvc.com/examples/troopjs_require/#/");
        $("#new-todo").setValue("do1").pressEnter();
        $("#new-todo").setValue("do2").pressEnter();
        $("#new-todo").setValue("do3").pressEnter();
        $("#new-todo").setValue("do4").pressEnter();

        $$("#todo-list>li").shouldHaveSize(4);
        $$("#todo-list>li").get(0).shouldHave(text("do1"));
        $$("#todo-list>li").get(1).shouldHave(text("do2"));
        $$("#todo-list>li").get(2).shouldHave(text("do3"));
        $$("#todo-list>li").get(3).shouldHave(text("do4"));

        $$("#todo-list>li").findBy(text("do2")).hover();
        $$("#todo-list>li").findBy(text("do2")).find(".destroy").click();

        $$("#todo-list>li").shouldHaveSize(3);
        $$("#todo-list>li").get(0).shouldHave(text("do1"));
        $$("#todo-list>li").get(1).shouldHave(text("do3"));
        $$("#todo-list>li").get(2).shouldHave(text("do4"));

        $$("#todo-list>li").find(text("do4")).find(".toggle").click();

        $("#clear-completed").click();

        $("#toggle-all").click();
        $("#clear-completed").click();
    }
}
