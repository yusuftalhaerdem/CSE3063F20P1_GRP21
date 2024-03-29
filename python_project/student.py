import json

class Student:

    def __init__(self, no, student_no, first_name, last_name):
        self.no = no
        self.student_no = student_no
        self.first_name = first_name
        self.last_name = last_name
        self.attendence = 0
        # self.answers = []
        self.email = ''
        
    @property
    def full_name(self):
        return self.first_name + ' ' + self.last_name

    @classmethod
    def from_df(cls,no,student_no,first_name,last_name,attendence,email):
        student = cls(no,student_no,first_name,last_name)
        student.attendence = attendence
        student.email = email
        return student

    def to_dict(self):
        return {
            'no':self.no+1,
            'student no':self.student_no,
            'first name':self.first_name,
            'last name':self.last_name,
            'email':self.email,
            'attendence':self.attendence,
        }
    
    def to_dict_short(self):
        return {
            'no':self.no+1,
            'student no':self.student_no,
            'full name':self.full_name,
        }

    def __repr__(self):
        return f'<Student {self.student_no}>'
