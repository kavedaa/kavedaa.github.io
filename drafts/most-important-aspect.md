# The most important aspect of programming

When you write a computer program, you usually start with a specification, or a list of features, or at least some rough idea of what the program is supposed to do. And then when all features are implemented and there are no errors, the program is finished, and you can congratulate yourself on a job well done.

Except that a couple of days later, your client calls and complains that "well the calculations are correct but they take several days to complete". And so you go back and optimize the program so that the calculations are completed instantly. And then the client is happy, and you can congratulate yourself on a job well done.

Except that some years later, the client asks you to add some additional features to the program. Or worse, they ask someone else to add some additional features. Or worse, that someone else is a violent psychopath who knows where you live. And that someone calls you and yells "is it you who wrote this ugly mess?!".

There are three important aspects of a computer program:

1. Correctness
2. Performance
3. Maintainability

The first two are sort of obvious because they are *required*. If the program is not correct, it's more or less useless. And if it is correct, but takes forever to do what it's supposed to do, it's still relatively useless. Maintainability however, isn't actually required. And that's why I will argue that it's the most important.

Maintainability can be defined as: how easy is it, for anyone, at any time, to make a change, add a feature, or fix a bug in the program?

* availability and compatibility of tools
* safety of refactoring
* readability

Readability can be defined as: how easy is it, for anyone, at any time, to read the program code and understand what it does?

* programming language
* frameworks
* architecture
* domain modeling / topology
* programming style (imperative vs. functional)
* level of abstraction
* complexity
* terseness
* formatting