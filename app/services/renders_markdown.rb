require 'rouge/plugins/redcarpet'

class RendersMarkdown
  class Renderer < Redcarpet::Render::HTML
    include Sprockets::Rails::Helper
    include ActionView::Helpers::UrlHelper
    include Rouge::Plugins::Redcarpet

    def parse_media_link(link)
      matches = link.match(/^(?<classes>.+)\|(?<url>.+)/)
      {
        :classes => matches[:classes],
        :url => matches[:url]
      } if matches
    end

      def image(link, alt_text, title)
        link_options = parse_media_link(link)
        url = link_options[:url] || ''
        class_names = link_options[:classes]
        image_tag(url, title: title, alt: alt_text, class: class_names)
      end

      def header(text, header_level)
        puts "tuli header #{text} #{header_level}"
        "<h1>#{text}</h1>"
      end
    end

  attr_reader :markdown

  def self.call(markdown)
    new(markdown).call
  end

  def initialize(markdown)
    @markdown = markdown
  end

  def call
    options = {autolink: true, tables: true, fenced_code_blocks: true}
    Redcarpet::Markdown.new(Renderer, options).render(@markdown)
  end
end
