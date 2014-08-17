if Rails.env.production?
  GA_TRACKER = 'UA-53654616-1'
  GA_DOMAIN = 'mad.is'
else
  GA_TRACKER = 'UA-53654616-2'
  GA_DOMAIN = 'staging.mad.is'
end
