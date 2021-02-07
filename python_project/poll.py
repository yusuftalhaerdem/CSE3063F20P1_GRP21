import json

class Poll:
    def __init__(self, poll_name, poll_type):
        self.poll_name = poll_name
        self.poll_type = poll_type  # attendance or quiz
        self.date = None
