class Answer:
    def __init__(self, poll, date, answers, isCorrect):
        self.poll = poll
        self.date = date
        # self.questions = questions
        self.answers = answers
        self.isCorrect = isCorrect

    def __repr__(self):
        return 'Poll = ' + self.poll + " Student = " + self.student.first_name + self.student.last_name