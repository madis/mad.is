---
title: Iterating fast for more Clojure productivity
description: This is a first in a series of posts that describes a process how to increase your Clojure development workflow. You can pick the parts you like or discover your own process. Here's what I did.
tags: [clojure technology]
---
# Iterating fast for more Clojure productivity

This is a first in a series of posts that describes a process how to increase your Clojure development workflow. You can pick the parts you like or discover your own process. Here's what I did.

## Overview

These will be the topics this series covers

1. The idea behind iterative improvement
2. Gaining deep understanding by building from simple to more complex
3. Quality through quantity
4. Abstract the repetitive and irrelevant parts - project templates
   - get faster to the interesting parts
   - fast development cycle through REPL & reload
   - deploy to get the feedback loop going
5. Clojure is ideal for AI assisted development
   - fast feedback loop through REPL evaluation
   - stateless design for faster reloads
   - benefits of simple syntax and AI structured editing
6. How to keep improving

## The idea behind iterative improvement

I think the answer to the question whether it's worthwhile to improve is self-evident. Yes. We get paid based on our productivity - if you can do more in the same amount of time or to produce more quality with the same effort, you have created more value. The alternative is to work more, but as everybody has 24 hours in a day, we'll quickly hit the hard limit. Considering we need to sleep, eat and do some other things in our lives in order to stay sane and healthy, we are left with maybe 8-12 hours per day for work. But that is still too optimistic - as software development is mainly mental activity, once the fatigue starts setting in, the productivity goes down fast, error rates increase and satisfaction reduces. So for me the solution is to do maximally productive work in . Analogy for physical training - instead of working (training) light for 3 hours, better results can be achieved by 30 minutes of very intense and exhaustive training. While this is true for physical exercise, it's more so for mental exercise. Software developer needs to  fit the mental model of the system they're working on in their head. A distraction or cognitive overload can break down the mental model and seriously harm the productivity. Who from us hasn't experienced that after an interruption, they first need to start reconstructing the problem in their heads before they can get to creative problem solving. You do this on a complex issue couple times in one day and you're done for the day.

So hopefully I've convinced you that working more has serious limits so working better and smarter is more practical solution. As there is no one way for everybody nor is there a fixed higher productivity level for which there's a constant formula, my finding is to iterate - make small improvements in the process. Keep the good parts, throw away the useless ones, repeat. The useful parts today could be replaced next week for even better ones.

For this approach to work efficiently, the useless or slow repetitive parts of the process should constantly be eliminated or abstracted away. Some examples: setting up a new project, configuring a CI pipeline, configuring test runner or REPL server, repeatedly adding the dependencies of your favourite tools. Solution for this is to use project templates and code snippets of which will be topic of the article [Abstract the repetitive and irrelevant parts - project templates](./project-templates.md)

## Gaining deep understanding by building from simple to more complex

The cognitive capacity of human beings is seriously limited. We're able to keep between 3-7 "things" (units of information) in our short term memory. The secret to working with bigger and more complex systems is to make each unit more significant. Make it contain more information i.e. replace details with abstractions and to choose the parts to have most meaningful semantic relationships between them. That way each of them carries information but also a group of them (depending on the order and positioning) conveys information. Same number of pieces but more information contained.

Luckily for us Clojure developers, this mentality seems to be quite wide-spread in the community. Take small pieces (functions, libraries) and combine them for more useful things. As a developer in order to stay and progress on the path of improvement, it's good idea to get rid of as much of friction and unnecessary overhead as possible.

The process is basically - get started quickly, bring in a tool (library, API), try it out from different angles in the REPL (based on the code you or AI wrote), when satisfied, write tests and satisfy them by adding working code.

## Quality through quantity

There's some evidence from Art & Fear (Bayles & Orland, 1993), [James Clear’s article](jamesclear.com/repetitions) or the book Atomic Habits - that by producing more (and thus getting more experience from both good and bad results) leads to more quality. I would add that it's also more fun. Clojure lends exceptionally well to that approach - no compilation, no process restarting, just eval and see the results.

Combine this with good tooling and project setup and you're in for some fun times and learning.

## Putting it in practice

Tools:

| Tool                                                  | Use                                                          |
| ----------------------------------------------------- | ------------------------------------------------------------ |
| [clj-reload](https://github.com/tonsky/clj-reload)    | Reloading updated code without restarting the JVM. Alternative: [tools.namespace](https://github.com/clojure/tools.namespace) |
| [donut.system](https://github.com/donut-party/system) | Dependency injection, components. Reliably starting & stopping the system. Popular alternative [integrant](https://github.com/weavejester/integrant) |
| [nREPL](https://nrepl.org/nrepl/index.html)           | REPL over network - so your editor can speak code to the JVM |
| [Conjure](https://github.com/olical/conjure)          | Vim plugin for interactive evaluation                        |
| [Clojure LSP](https://clojure-lsp.io/)                | For easier code navigation, refactoring and other goodies    |

### Example project

[clojure-starter](https://github.com/madis/clojure-starter)

> TODO: describe the project details and workflow
