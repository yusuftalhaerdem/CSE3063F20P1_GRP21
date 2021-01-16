def check_poll_type(question):
    if 'are you attending' in question:
        return 'attendence'
    else:
        return 'poll'

def find_student(student_list,name):
    name = ''.join([i for i in name if not i.isdigit()]).upper()
    student = [s for s in student_list if name in s.full_name][0]
    return student
