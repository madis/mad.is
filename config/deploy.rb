# config valid only for Capistrano 3.1
lock '3.2.1'

set :user, 'deployer'
set :application, 'mad.is'
set :deploy_to, "/home/#{fetch(:user)}/apps/#{fetch(:application)}"
set :app_root_folder, "#{fetch(:deploy_to)}/current"
set :pty, true
set :keep_releases, 8

set :repo_url, "git@github.com:madis/#{fetch(:application)}.git"
set :branch, `git branch`.match(/\* (\S+)\s/m)[1]

set :bundle_gemfile, -> { release_path.join('Gemfile') }
set :unicorn_workers_count, 2

set :ssh_options, {
  auth_methods: %w(publickey),
  forward_agent: true,
  compression: false,
  keepalive: true
}

# set :log_level, :debug
set :linked_files, [
  "config/database.yml",
  "config/unicorn.rb",
  "config/secrets.yml"
]

set :linked_dirs, [
  "public/assets",
  "tmp/pids",
  "tmp/cache",
  "log"
]
