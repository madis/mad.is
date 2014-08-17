require 'capistrano/setup'
require 'rvm1/capistrano3'

require 'capistrano/deploy'
require 'capistrano/bundler'
require 'capistrano/rails/assets'
require 'capistrano/rails/migrations'

import 'lib/capistrano/tasks/setup.cap'
Dir.glob('lib/capistrano/tasks/*.cap').each { |r| import r }
