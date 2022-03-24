/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        return Solution1.mergeKLists(lists);
    }

    static class Solution1 {
        ListNode mergedList;
        ListNode[] lists;

        static ListNode mergeKLists(ListNode[] lists) {
            return new Solution1(lists).generateMergedList();
        }

        ListNode generateMergedList() {
            /**
             * TimeComplexity:  O(K + N*log(K))
             * SpaceComplexity: O(K)
             *
             * Where:
             *      K = lists.length
             *      N = sum([list.length for list in lists])
             */

            if (lists.length == 0) {
                // No linked-lists, so output will be empty.
                return null;
            }

            // Create a min-priority-queue based on node values of the head of each list.
            // This will ensure that at any given time, you will have the smallest possible
            // value present in the lists (this is because all lists are sorted in ascending
            // order).
            // You can construct the resulting linked-list in this manner, however, you
            // still have the reverse the linked-list at the end because it will be in
            // descending order.

            // PQ construction time (and space) complexity: O(K)
            PriorityQueue<ListNode> PQ = new PriorityQueue<>(
                    lists.length,
                    (ListNode v1, ListNode v2) -> Integer.compare(v1.val, v2.val));

            for (ListNode list : lists) {
                // For whatever reason, there  can be `null` linked-lists so we have
                // to gaurd for this.
                if (list != null) {
                    PQ.add(list);
                }
            }

            // For each element in the lists (plural!), we will pop a list from the queue and,
            // possibly, re-queue the list: O(N * log(K))
            while (!PQ.isEmpty()) {
                ListNode head = PQ.poll();
                ListNode next = addToList(head);
                // Only re-queue a list if it still has nodes (ie, the list is not `null`).
                if (next != null) {
                    PQ.add(next);
                }
            }

            // O(N)
            return reverseList(mergedList);
        }

        /**
         * Makes input node the head of `mergedList` and removes it from input list.
         *
         * @param v node to add to `mergedList`.
         * @returns the next node in the input list.
         */
        ListNode addToList(ListNode v) {
            /**
             * TimeComplexity:  O(1)
             * SpaceComplexity: O(1)
             */
            ListNode next = v.next;
            v.next = mergedList;
            mergedList = v;

            return next;
        }

        /**
         * Reverses a linked-list (iteravely).
         *
         * @param v linked list to reverse.
         * @returns the reversed list.
         */
        ListNode reverseList(ListNode v) {
            /**
             * TimeComplexity:  O(L)
             * SpaceComplexity: O(1)
             *
             * Where:
             *      L = len(v)
             */
            if (v == null) {
                return null;
            }
            // Let's do it via iterations?
            ListNode head = v;
            ListNode next = v.next;
            head.next = null;
            // At the current node, we want to make the "next" node the owner of current node.
            while (next != null) {
                ListNode newNext = next.next;
                next.next = head;
                head = next;
                next = newNext;
            }

            return head;
        }

        Solution1(ListNode[] lists) {
            this.lists = lists;
        }
    }
}
