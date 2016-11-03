class ApplicationController < ActionController::Base
  protect_from_forgery with: :exception

  def landing
    render plain: 'Welcome to mad.is'
  end
end
