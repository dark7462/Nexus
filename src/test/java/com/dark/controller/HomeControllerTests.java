package com.dark.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeControllerTests {

    @Test
    void rootShouldReturnExpectedMessage() {
        HomeController controller = new HomeController();
        assertEquals("This is the home Contoller", controller.root());
    }
}
