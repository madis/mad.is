class PostsController < ApplicationController
  def index
    @posts = [Post.new(title: 'First post', content: IO.read(Rails.root + 'README.md'), updated_at: Time.now)]
  end
end
