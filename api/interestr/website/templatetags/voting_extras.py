from django import template

register = template.Library()

@register.filter
def vote_of_user(post, user):
    qs = post.votes.filter(owner=user)
    return qs.first().id if qs.exists() else -1
