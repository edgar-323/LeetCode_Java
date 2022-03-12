#!/usr/bin/python3


from argparse import ArgumentParser
from exercise import Exercise
from logger import Logger
from playground_builder import PlaygroundBuilder


arg_parser = ArgumentParser(description=f'Setup {GITHUB_DIR}/Practice')
arg_parser.add_argument(
        '--exercise',
        type=str,
        default=None,
        help='Leetcode exercise title')


def setup_playground(title=None):
    logger = Logger.get_logger()
    logger.log(f'Initializing Exercise module ...')
    exercise = Exercise(title=args.exercise)
    logger.log(f'Initializing PlaygroundBuilder module ...')
    builder = PlaygroundBuilder(exercise=exercise)
    logger.log(f'Building playground ...')
    builder.build_playground()
    logger.log('Operation successfully completed.')


if __name__ == '__main__':
    args = arg_parser.parse_args()
    setup_playground(title=args.exercise)
