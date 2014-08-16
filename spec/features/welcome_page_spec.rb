feature 'Pages' do
  it 'has homepage' do
    visit '/'
    expect(page.status_code).to be 200
  end
end
