from django.conf.urls import url, include

from . import views


app_name = 'website'

urlpatterns = [
    url(r'^$', views.HomePageView.as_view(), name='index'),
    url(r'^login/$', views.UserLoginView.as_view(), name='login'),
    url(r'^register/$', views.UserRegisterView.as_view(), name='register'),
    url(r'^groups/$', views.GroupView.as_view(), name='groups'),
    url(r'^logout/$', views.LogoutView.as_view(), name='logout'),
    url(r'^create-group/$', views.CreateGroupView.as_view(), name='create-group'),
    url(r'^groups/(?P<pk>\d+)/$', views.GroupDetailsView.as_view(), name='group-details'),
]
