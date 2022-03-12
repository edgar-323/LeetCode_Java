#!/usr/bin/python3


from argparse import ArgumentParser
from exercise import Exercise
from file_utils import FileUtils
from logger import Logger
from playground_builder import PlaygroundBuilder


HOME_DIR = FileUtils().get_home_dir()


arg_parser = ArgumentParser(
        description=f'Setup {HOME_DIR}/GitHub/playground/')
arg_parser.add_argument(
        '--exercise',
        type=str,
        help='Leetcode exercise title')


def log(msg):
    logger = Logger.get_logger()
    logger.log(f'setup.py: {msg}')


def setup_playground(title=None):
    log(f'Initializing Exercise module ...')
    exercise = Exercise(title=args.exercise)
    log(f'Initializing PlaygroundBuilder module ...')
    builder = PlaygroundBuilder(exercise=exercise)
    log(f'Building playground ...')
    builder.build_playground()
    log('Operation successfully completed.')


if __name__ == '__main__':
    args = arg_parser.parse_args()
    setup_playground(title=args.exercise)
