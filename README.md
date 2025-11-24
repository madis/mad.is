## https://mad.is web page built with [Babashka](https://babashka.org/), [Bulma CSS](https://bulma.io/) and [Datastar](https://data-star.dev/)

### Running

1. REPL workflow: run from the command line `bb nrepl`
  - afterwards run `(blog.main/start!)`
  - and to reload code changes & restart the http-kit server `(user/go!)`
2. To start without the REPL: `bb blog.main/-main`

### Structure

The posts are created by adding them under `resources/public/posts` folder with a naming scheme `YYYY-MM-DD-<post-name>.md`. If post requires assets (images, downloads, etc), they should be put under `resources/public/posts/<post-name>/` subfolder and referred with `/assets/<asset-file-name>`. The server will use the _referer_ header to determine the post folder and will look the asset up and return it.
