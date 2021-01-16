import json

class Poll:
    choice = []
    date = None

    def __init__(self, poll_name, poll_type, questions):
        self.poll_name = poll_name
        self.poll_type = poll_type  # attendance or quiz
        self.questions = questions  # questions in poll will be kept here


    @classmethod
    def attendence_from_json(cls, json_string):
        json_dict = json.loads(json_str)
        return cls(**json_dict)

    @classmethod
    def questions_from_json(cls, json_string):
        json_dict = json.loads(json_string)
        return cls(**json_dict)


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