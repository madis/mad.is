require 'rails_helper'

RSpec.feature 'landing page' do
  it 'welcomes user' do
    visit '/'
    expect(page).to have_content 'Welcome to mad.is'
  end
end
