import json
from poll import Poll
from datetime import datetime

class AttendencePoll(Poll):
    total_number = 0
    def __init__(self, poll_name):
        super().__init__(poll_name,'attendence')
        self.poll_name = poll_name
        self.last_date = datetime(1900,5,5,00,00,00)
        self.finished = False
