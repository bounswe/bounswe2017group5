# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from rest_framework.response import Response

class ErrorResponse(Response):
    """
    A unified way to return error messages from the api
    """
    def __init__(self, message, status=None):
        super(ErrorResponse, self).__init__(data={'message' : message}, status=status)
