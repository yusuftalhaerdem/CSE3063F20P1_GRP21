from input import *
from output import Output


students = []
polls = []
answers = []
unassigned_answers = []
Input.read(Input(), 'inputs/poll_answers.xlsx', 'inputs/CES3063_Fall2020_rptSinifListesi.XLS',
           'inputs/CSE3063_20201123_Mon_zoom_PollReport.csv', polls, students, answers, unassigned_answers)

Output.create_outputs(Output(), students, answers, polls)
