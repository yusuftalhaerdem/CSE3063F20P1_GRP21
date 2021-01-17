import json
from poll import Poll
from datetime import datetime

class AttendencePoll(Poll):
    total_number = 0
    def __init__(self, poll_name):
        super().__init__(poll_name,'attendence')
        self.poll_name = poll_name
        self.last_date = datetime(1900,5,5,00,00,00)


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