feature 'Pages' do
  let(:post) { Post.new title: 'The truth is in the code', content: 'Code mode'}

  it 'homepage shows content' do
    visit '/'
    expect(page).to have_content(post.content)
    expect(page).to have_title(post.title)
  end
end
