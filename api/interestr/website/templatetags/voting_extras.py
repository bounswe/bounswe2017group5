from django import template
from api.models import Post, Vote

register = template.Library()

@register.filter
def vote_of_user(post, user):
    vote = Vote.objects.filter(owner__pk=user, post__pk=post['id'] if isinstance(post, dict) else post.id).first()
    return vote.id if vote else -1

@register.filter
def vote_sum(post):
    return len(filter(lambda v: v['up'], post['votes'])) - len(filter(lambda v: not v['up'], post['votes']))
