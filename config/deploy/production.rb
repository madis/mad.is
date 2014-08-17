# Production settings
set :rails_env, 'production'
set :port, '80'

server 'garage', user: 'deployer', roles: %w{web app db}, primary: :true
