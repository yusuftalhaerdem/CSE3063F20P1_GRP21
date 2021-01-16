class Output:
    def __init__(self):
        pass

    def create_results(self, students, answers):
        # this will probably will be long, you will need to declare new variables etc.

        # if there is no report file in output location, create a new json file

        # if there is already one report file, append new poll evaluations to it.
        # such as Q5 Q6 Q7

        # then return.
        pass

    def create_statistics(self, answers, polls):
        # create histogram.

        # create some visual things to show     a)  b)  c)  d)  e)
        #                                   Q1  %11 %13 %17 %19 %40     true answer will be highlighted.

        pass

    def create_outputs(self, students, answers, polls):
        self.create_results(students, answers)
        self.create_statistics(answers, polls)
