import json
from poll import Poll

class QuestionPoll(Poll):
    def __init__(self, poll_name, poll_type, questions):
        super().__init__(poll_name,poll_type)
        self.poll_name = poll_name
        self.poll_type = poll_type  # attendance or quiz
        self.questions = questions  # questions in poll will be kept here
        self.student_answers = {}
        self.date = None
        self.absences = []
        self.attended_students = []
        self.anomalies = []

    def to_dict(self):
        return {
            'poll name': self.poll_name,
            'self.questions': self.questions,
            'attended students': self.attended_students
        }

'''
Student
    name
    surname
    email
    student_no
    no
    answers (kesin değil)

Poll
    name
    type
    date (reporttan gelicek)
    soru cevaplar

cevaplamış öğrenciler
doğru cevaplamışlar(optional)
eşlenmemişler
şıklar = (unique answers)
şık_işaretleme_sayısı

birden çok şık olabilir

Output
    student bilgiler, attendence %, sorular doğru mu yanlış mı ile beraber,doğrı soru %
    chartlar
    her poll için output

'''