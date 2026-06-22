## [https://mad.is](https://mad.is) web page

Built with [Babashka](https://babashka.org/)

### Running

1. REPL workflow: run from the command line `bb nrepl`
  - afterwards run `(blog.main/start!)`
  - and to reload code changes & restart the http-kit server `(user/go!)`
2. To start without the REPL: `bb blog.main/-main`
