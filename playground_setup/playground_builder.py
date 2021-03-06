from exercise import Exercise
from file_utils import FileUtils
from logger import Logger

import sys

class PlaygroundBuilder(object):
    '''
    Generates:
                    ~
                    |
                  GitHub
                    |
                playground
                ... | ...
            <LeetCode_Problem>
             |              |
        Solution.java   Tester.java
    '''

    LOGGING_PREFIX = 'PlaygroundBuilder'

    def __init__(self, exercise=None):
        assert type(exercise) is Exercise
        self.exercise = exercise
        self.file_utils = FileUtils()
        self.logger = Logger.get_logger()
        self.playground_dir = (
                f'{self.file_utils.get_home_dir()}/'
                'GitHub/playground')
        self.title_dir = (
                f'{self.playground_dir}/'
                f'{self.exercise.get_title().lower()}')
        self.tester_file = (
                f'{self.title_dir}/Tester.java')
        self.solution_file = (
                f'{self.title_dir}/Solution.java')

    def log(self, msg=None):
        self.logger.log_with_prefix(
                PlaygroundBuilder.LOGGING_PREFIX, msg)

    def build_playground(self):
        try:
            # Create playground dir (if it doesn't exist).
            if not self.file_utils.is_dir(self.playground_dir):
                self.log('Creating dir: '
                         f'{self.playground_dir}')
                self.file_utils.create_dir(self.playground_dir)
            # Create title dir (if it doesn't exist).
            if not self.file_utils.is_dir(self.title_dir):
                self.log('Creating dir: '
                         f'{self.title_dir}')
                self.file_utils.create_dir(self.title_dir)
            # Create/clear Tester.java.
            self.log('Creating (or clearing) file: '
                     f'{self.tester_file}')
            self.file_utils.create_file(self.tester_file)
            # Fill Tester.java with base info.
            self.log('Appending self.get_tester_file_content() to: '
                     f'{self.tester_file}')
            self.file_utils.append_to_file(
                    self.tester_file,
                    self.get_tester_file_content())
            # Copy LeetCode problem to playground dir.
            self.log('Copying:\n'
                     f'src={self.exercise.get_exercise()}\n'
                     f'dst={self.solution_file}\n')
            self.file_utils.copy_file(
                    src=self.exercise.get_exercise(),
                    dst=self.solution_file)
        except Exception as e:
            self.log(f'Failed to build playground: {e}')
            sys.exit()

    def get_tester_file_content(self):
        return (
            f'package playground.{self.exercise.get_title().lower()};\n'
            '\n'
            '\n'
            '// TODO: Add the necessary imports to Solution.java\n'
            '\n'
            '/********************************************************************\n'
            '\n'
            'TO COMPILE:\n'
            '$ cd ~/GitHub && \\\n'
            f'javac -g playground/{self.exercise.get_title().lower()}/Tester.java;\n'
            '\n'
            'TO RUN main():\n'
            f'$ java playground.{self.exercise.get_title().lower()}.Tester;\n'
            '\n'
            'TO RUN DEBUGGER:\n'
            f'$ jdb playground.{self.exercise.get_title().lower()}.Tester;\n'
            '\n'
            '********************************************************************/\n'
            '\n'
            '\n'
            'public class Tester {\n'
            '\n'
            '    private Solution solution;\n'
            '\n'
            '    Tester() {\n'
            '         solution = new Solution();\n'
            '    }\n'
            '\n'
            '    public static void main(String[] unusedArgs) {\n'
            '         Tester tester = new Tester();\n'
            '         tester.runTests();\n'
            '    }\n'
            '\n'
            '    private void runTests() {\n'
            '         // TODO: Implement.\n'
            '         System.out.println(\"Initializing all tests ...\");\n'
            '         System.out.println(\"Testing complete.\");\n'
            '    }\n'
            '}');
