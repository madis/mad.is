class PostsController < ApplicationController
  def index
    @posts = [Post.new(title: 'Ja ja ja', content: IO.read(Rails.root + 'README.md'), updated_at: Time.now)]
  end
end
