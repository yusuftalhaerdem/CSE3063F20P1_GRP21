import json


class Poll:
    def __init__(self, poll_name, poll_type, date, questions, answers):
        self.poll_name = poll_name
        self.poll_type = poll_type  # attendance or quiz
        self.date = date            # we need this kind of variable to check whatever we are using a pole again or not.
        self.questions = questions  # questions in poll will be kept here
        self.answers = answers      # right answers of questions will be kept here

    @classmethod
    def attendence_from_json(cls, json_string):
        json_dict = json.loads(json_str)
        return cls(**json_dict)

    @classmethod
    def questions_from_json(cls, json_string):
        json_dict = json.loads(json_string)
        return cls(**json_dict)
