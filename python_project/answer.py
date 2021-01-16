class Answer:
    def __init__(self, poll, student, date, questions, answers, isCorrect, file_name):
        self.poll = poll
        self.student = student
        self.date = date
        self.questions = questions
        self.answers = answers
        self.isCorrect = isCorrect
        self.file_name = file_name

    def __repr__(self):
        return 'Poll = ' + self.poll + " Student = " + self.student.first_name + self.student.last_name
