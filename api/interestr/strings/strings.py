LANG_TR = {
    'token_auth_error' : "Invalid Login",
}

# TODO: Dynamically determine this later
LOCALE = LANG_TR

def get(key):
    return LOCALE[key]
