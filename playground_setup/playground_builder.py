from exercise import Exercise
from file_utils import FileUtils
from logger import Logger

import sys


class PlaygroundBuilder(object):

    TESTER_CONTENT = (
            'package playground;\n'
            '\n'
            'public class Tester {\n'
            '     public static void main(String[] unusedArgs) {\n'
            '         System.out.println(\"Hello, world!\");\n'
            '     }\n'
            '}')

    def __init__(self, exercise=None):
        assert type(exercise) is Exercise
        self.exercise = exercise
        self.file_utils = FileUtils()
        self.logger = Logger.get_logger()
        self.playground_dir = f'{self.file_utils.get_home_dir()}/GitHub/playground'
        self.tester_file = f'{self.playground_dir}/Tester.java'
        self.solution_file = f'{self.playground_dir}/Solution.java'

    def log(self, msg):
        self.logger.log(f'PlaygroundBuilder -> {msg}')

    def build_playground(self):
        try:
            if not self.file_utils.is_dir(self.playground_dir):
                self.log(f'Creating dir: \"{self.playground_dir}\"')
                self.file_utils.create_dir(self.playground_dir)
            self.log(f'Creating (or clearing) file: \"{self.tester_file}\"')
            self.file_utils.create_file(self.tester_file)
            self.log(f'Appending PlaygroundBuilder.TESTER_CONTENT to: \"{self.tester_file}\"')
            self.file_utils.append_to_file(
                    self.tester_file, PlaygroundBuilder.TESTER_CONTENT)
            self.log(f'Copying:\nsrc={self.exercise.get_exercise()}\ndst={self.solution_file}\n')
            self.file_utils.copy_file(
                    src=self.exercise.get_exercise(),
                    dst=self.solution_file)
        except Exception as e:
            self.log(f'Failed to build playground: {e}')
            sys.exit()   
