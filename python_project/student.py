import json

class Student:

    attendence = []
    polls = []

    def __init__(self, student_no, first_name, last_name,):
        self.student_no = student_no
        self.first_name = first_name
        self.last_name = last_name    

    @classmethod
    def from_json(cls, json_str):
        json_dict = json.loads(json_str)
        return cls(**json_dict)

    def __repr__(self):
        return f'<Student {self.student_no}>'
