# Testing scope

This package mocks anything that is not at the [controller level](https://rieckpil.de/how-to-test-spring-boot-web-controller-the-right-way/).

It does not recognize configuration folders by itself, use <mark>@Import</mark> for including necessary classes.

