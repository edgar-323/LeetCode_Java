#!/usr/bin/env python3


from math import inf as INFINITY
from statistics import mean, median


def calculateTotalDistanceToClosestMailbox(houses, mailboxes):
    totalDist = 0
    for house in houses:
        minDist = INFINITY
        for mailbox in mailboxes:
            minDist = min(minDist, abs(house - mailbox))
        totalDist += minDist
    return totalDist


def simulateExample1WithOneMailBox():
    houses = [2, 3, 5, 12, 18]
    print(f'\nSimulation for: ( houses={houses}, k=1 )\n')
    minTotalDist = INFINITY
    minLocations = []
    for i in range(max(houses) + 1):
        totalDist = calculateTotalDistanceToClosestMailbox(houses, [i])
        #print(f'Placing mailbox at location {i} yields totalDist={totalDist}')
        if totalDist < minTotalDist:
            minTotalDist = totalDist
            minLocations = [i]
        elif totalDist == minTotalDist:
            minLocations.append(i)
    print(f'\nOptimal mailbox locations: {minLocations}\n')


def simulateExample1WithTwoMailBoxes():
    houses = [2, 3, 5, 12, 18]
    print(f'\nSimulation for: ( houses={houses}, k=2 )\n')
    minTotalDist = INFINITY
    minLocations = []
    for i in range(max(houses) + 1):
        for j in range(i+1, max(houses) + 1):
            totalDist = calculateTotalDistanceToClosestMailbox(houses, [i, j])
            #print(f'Placing mailboxes at locations {[i, j]} yields totalDist={totalDist}')
            if totalDist < minTotalDist:
                minTotalDist = totalDist
                minLocations = [(i, j)]
            elif totalDist == minTotalDist:
                minLocations.append((i, j))
    print(f'\nOptimal mailbox locations: {minLocations}\n')


def simulateExample1WithThreeMailBoxes():
    houses = [2, 3, 5, 12, 18]
    print(f'\nSimulation for: ( houses={houses}, k=3 )\n')
    minTotalDist = INFINITY
    minLocations = []
    for i in range(max(houses) + 1):
        for j in range(i+1, max(houses) + 1):
            for k in range(j+1, max(houses) + 1):
                totalDist = calculateTotalDistanceToClosestMailbox(houses, [i, j, k])
                #print(f'Placing mailboxes at locations {[i, j, k]} yields totalDist={totalDist}')
                if totalDist < minTotalDist:
                    minTotalDist = totalDist
                    minLocations = [(i, j, k)]
                elif totalDist == minTotalDist:
                    minLocations.append((i, j, k))
    print(f'\nOptimal mailbox locations: {minLocations}\n')


if __name__ == "__main__":
    simulateExample1WithOneMailBox()
    simulateExample1WithTwoMailBoxes()
    simulateExample1WithThreeMailBoxes()
