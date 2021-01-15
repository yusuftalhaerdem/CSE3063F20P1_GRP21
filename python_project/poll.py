import json

class Poll:
    def __init__(self,user_name,user_email,submitted_date,questions):
        self.name = user_name
        self.email = user_email
        self.date = submitted_date
        self.questions = questions
    
    @classmethod
    def attendence_from_json(cls, json_string):
        json_dict = json.loads(json_str)
        return cls(**json_dict)

    @classmethod
    def questions_from_json(cls, json_string):
        json_dict = json.loads(json_string)
        return cls(**json_dict)