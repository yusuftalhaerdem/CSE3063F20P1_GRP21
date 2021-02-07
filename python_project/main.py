from config import Config
from input import Input
import logging
logger = logging.getLogger('logger1')
logger.setLevel(logging.INFO)
handler = logging.FileHandler('app.log',mode='w')
formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s', datefmt='%Y-%m-%d %H:%M:%S')
handler.setLevel(logging.INFO)
handler.setFormatter(formatter)
logger.addHandler(handler)
logger.info('Program is starting')

class Main(object):
    def __init__(self):
        config = Config()
        
        report_files_path, answer_files_path,student_list, glob_file_path,output_path = config.inputs()

        input_o = Input()

        input_o.read(report_files_path, answer_files_path,student_list, glob_file_path,output_path)
    

main = Main()