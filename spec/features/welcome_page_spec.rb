feature 'Pages' do
  let(:post) { Post.create title: "The truth is in the code", content: "Code mode #{rand}" }

  it 'homepage shows content' do
    post
    visit '/'
    expect(page).to have_content(post.content)
  end
end
