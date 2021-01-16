class Input:
    def __init__(self):
        pass

    def read_polls(self, polls_location, polls):
        # read all poll objects and add them into polls array

        pass

    def read_students(self, students_location, students):
        # read all student objects and add them into students array

        pass

    def read_answers(self, answers_location, answers):
        # read all answer objects and add them into answer array

        pass

    def matching(self, polls, students, answers, unassigned_answers):
        # connect answers.poll with one poll, if date does not match, create a new poll object and add it into polls
        # CAUTION!! only bind poll and answer when all the questions are matched.

        # try to match answers with student objects

        # if there is some unmatched students add them into unassigned_answers.

        pass

    def extreme_matching(self, polls, students, answers, unassigned_answers):
        """  low priority  """
        # try matching in a way which is extreme. we may change its class in latter

        pass

    def read(self, polls_location, students_location, answers_location, polls, students, answers, unassigned_answers):
        self.read_polls(polls_location, polls)
        self.read_students(students_location, students)
        self.read_answers(answers_location, answers)
        self.matching(polls, students, answers, unassigned_answers)
        self.extreme_matching(polls, students, answers, unassigned_answers)
