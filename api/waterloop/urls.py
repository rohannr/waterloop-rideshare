from django.conf.urls.defaults import patterns, include, url

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    url(r'^users/$', 'waterloop_app.views.user_index'),
    url(r'^users/(?P<userId>\d+)/$', 'waterloop_app.views.user_detail'),
    url(r'^search/$', 'waterloop_app.views.search'),
    url(r'^rides/$', 'waterloop_app.views.rides'),
    # Examples:
    # url(r'^$', 'waterloop.views.home', name='home'),
    # url(r'^waterloop/', include('waterloop.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    url(r'^admin/', include(admin.site.urls)),
)
