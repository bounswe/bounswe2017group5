from django import template
from api.models import Post, Vote

register = template.Library()

@register.filter
def vote_of_user(post, user):
    vote = Vote.objects.filter(owner__pk=user, post__pk=post.id).first()
    return vote.id if vote else -1

    # qs = post.votes.filter(owner__pk=user)
    # return qs.first().id if qs.exists() else -1
